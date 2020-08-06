import org.apache.log4j.Logger;
import org.jsmpp.PDUStringException;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SMPPServerClass {
    private static int port;
    private static SMPPServerSessionListener sessionListener;
    private SMPPServerSession session;
    private boolean running;

    static final Logger LOGGER = Logger.getLogger(SMPPServerClass.class);

    public SMPPServerClass(int port) throws IOException {
        this.port = port;
        running = true;

        sessionListener = initSessionListener(port);

        startWaitingForClientsThread();
    }

    private SMPPServerSessionListener initSessionListener(int port) throws IOException {
        SMPPServerSessionListener serverSessionListener = new SMPPServerSessionListener(port);

        // prepare the message receiver
        ServerMessageReceiverListener messageReceiverListener = new ServerMessageReceiverListenerImp();
        serverSessionListener.setMessageReceiverListener(messageReceiverListener);

        return serverSessionListener;
    }

    public void startWaitingForClientsThread() {
        new Thread(() -> {
            while (running) {
                try {
                    session = sessionListener.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BindRequest request = session.waitForBind(5000);
                    LOGGER.info("Waiting for connection");

                    if("ben".equals(request.getSystemId())) {
                        request.accept(request.getSystemId());

                        LOGGER.info("Accepted bind");
                    }
                    else {
                        request.reject(ErrorCode.INVALID_SYSTEM_ID.getErrorCode());
                        LOGGER.error("INVALID_SYSTEM_ID");
                    }
                } catch (TimeoutException | PDUStringException | IOException e) {
                    e.printStackTrace();
                }

//                session.unbindAndClose();
//                sessionListener.close();
            }
        }).start();
    }
}
