import org.apache.log4j.Logger;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

public class StateSessionListenerImp implements SessionStateListener {
    static final Logger LOGGER = Logger.getLogger(StateSessionListenerImp.class);

    @Override
    public void onStateChange(SessionState currSessionState, SessionState lastSessionState, Session session) {
        if(!currSessionState.isBound()) {
            LOGGER.trace(String.format("SmppSession changed status from {%s} to {%s}. {%s}", currSessionState, lastSessionState, session.getSessionId()));
        }
    }
}
