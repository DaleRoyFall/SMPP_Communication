import org.apache.log4j.Logger;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;

public class MessageReceiverListenerImp implements MessageReceiverListener {
    static final Logger LOGGER = Logger.getLogger(MessageReceiverListenerImp.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm)   {
        LOGGER.info("Message: " + new String(deliverSm.getShortMessage()));

    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {

    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session)  {
        return null;
    }
}
