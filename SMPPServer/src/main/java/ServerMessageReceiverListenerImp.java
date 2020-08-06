import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.*;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;

import java.io.IOException;

public class ServerMessageReceiverListenerImp implements ServerMessageReceiverListener {
    static final Logger LOGGER = Logger.getLogger(ServerMessageReceiverListenerImp.class);
    // prepare generator of Message ID
    final MessageIDGenerator messageIdGenerator = new RandomMessageIDGenerator();

    @Override
    public MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
            LOGGER.info("Message: " + new String(submitSm.getShortMessage()));

        try {
            RegisteredDelivery registeredDelivery = new RegisteredDelivery();
            String message = "Hello you, too";

            smppServerSession.deliverShortMessage("CMT", TypeOfNumber.INTERNATIONAL,
                        NumberingPlanIndicator.UNKNOWN, submitSm.getDestAddress(), TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN,
                        submitSm.getSourceAddr(), new ESMClass(), (byte)0, (byte)1,
                        registeredDelivery,  new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1,
                                false), message.getBytes());
        } catch (PDUException e) {
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // need message_id to response submit_sm
            return messageIdGenerator.newMessageId();
    }

    @Override
    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti, SMPPServerSession smppServerSession) throws ProcessRequestException {
        return null;
    }

    @Override
    public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        return null;
    }

    @Override
    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession smppServerSession) throws ProcessRequestException {

    }

    @Override
    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession smppServerSession) throws ProcessRequestException {

    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) throws ProcessRequestException {
        return null;
    }
}
