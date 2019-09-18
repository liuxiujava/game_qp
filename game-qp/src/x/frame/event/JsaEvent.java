package x.frame.event;


import jsa.event.Event;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;

public class JsaEvent extends Event<IoSession> {
    private IMessage cmd = null;

    public JsaEvent(String type, IoSession source) {
        super(type, source);
    }

    public IMessage getMessage() {
        return cmd;
    }

    public void setMessage(IMessage cmd) {
        this.cmd = cmd;
    }
}
