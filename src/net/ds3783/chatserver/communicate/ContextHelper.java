package net.ds3783.chatserver.communicate;

import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-30
 * Time: ÏÂÎç11:53
 * To change this template use File | Settings | File Templates.
 */
public class ContextHelper {
    private Map<Message, MessageContext> contextMap;
    private List<MessageContext> allContext;

    public ContextHelper() {
        contextMap = new HashMap<Message, MessageContext>();
        allContext = new ArrayList<MessageContext>();
    }

    public MessageContext registerMessage(Message message, Client sender) {
        MessageContext context = new MessageContext();
        context.setSender(sender);
        context.setMessage(message);
        contextMap.put(message, context);
        allContext.add(context);
        return context;
    }

    public MessageContext getContext(Message msg) {
        return contextMap.get(msg);
    }

    public void forget(MessageContext context) {
        contextMap.remove(context.getMessage());
        allContext.remove(context);

    }

    public void lightWeightClean() {
        for (MessageContext context : allContext) {
            if (context.getReceivers().size() == 0) {
                this.forget(context);
            }
        }
    }
}
