package x.handler;


import ws.nett.handler.IActionHandler;
import ws.nett.handler.IActionHandlerFactory;

public class XActionHandlerFactory implements IActionHandlerFactory {
    private static IActionHandler actionHandler = new MyWebSocketHandler();

    @Override
    public IActionHandler getActionHandler() {
        return actionHandler;
    }
}
