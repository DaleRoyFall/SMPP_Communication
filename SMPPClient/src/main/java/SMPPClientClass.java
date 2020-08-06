import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.io.IOException;
import java.util.Date;

public class SMPPClientClass {
    private String ip;
    private int port;
    protected SMPPSession session;
    private boolean running;

    static final Logger LOGGER = Logger.getLogger(SMPPClientClass.class);

    public SMPPClientClass(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        running = true;

        session = initSession(ip, port, username, password);

        new Thread(() -> {
            while(running) {
                sendMessage("Hello from me");

                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private SMPPSession initSession(String ip, int port, String username, String password) {
        SMPPSession session = new SMPPSession();
        session.setPduProcessorDegree(1);

        MessageReceiverListener messageReceiverListener = new MessageReceiverListenerImp();
        session.setMessageReceiverListener(messageReceiverListener);

        try {
             String systemId = session.connectAndBind(
                     ip,
                     port,
                     new BindParameter(
                            BindType.BIND_TRX,
                            username,
                            password,
                            "cp",
                            TypeOfNumber.UNKNOWN,
                            NumberingPlanIndicator.UNKNOWN,
                            null)
             );

             session.addSessionStateListener(new StateSessionListenerImp());

             LOGGER.info("Connected with system id: " + systemId);

        } catch (IOException e) {
            LOGGER.error("I/O error occurred" +  e);
        }

        return session;
    }

    private void sendMessage(String message) {
        // send Message
        try {
            // set RegisteredDelivery
            RegisteredDelivery registeredDelivery = new RegisteredDelivery();
            registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
            TimeFormatter timeFormatter = new RelativeTimeFormatter();

            String messageId = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.UNKNOWN, "2216", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN,
                    "858176504657", new ESMClass(), (byte)0, (byte)1, timeFormatter.format(new Date()), null,
                    registeredDelivery, (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1,
                             false), (byte)0, message.getBytes());

            System.out.println("Message submitted, message_id is " + messageId);

        } catch (PDUException e) {
            // Invalid PDU parameter
            System.err.println("Invalid PDU parameter");
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            // Response timeout
            System.err.println("Response timeout");
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            // Invalid response
            System.err.println("Receive invalid respose");
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            System.err.println("Receive negative response");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO error occur");
            e.printStackTrace();
        }
    }
}
