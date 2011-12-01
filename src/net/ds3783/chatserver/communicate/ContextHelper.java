package net.ds3783.chatserver.communicate;

import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-30
 * Time: ÏÂÎç11:53
 * To change this template use File | Settings | File Templates.
 */
public class ContextHelper {
    private Map<Message, MessageContext> contextMap;
    private final List<MessageContext> allContext;

    public ContextHelper() {
        contextMap = new HashMap<Message, MessageContext>();
        allContext = Collections.synchronizedList(new ArrayList<MessageContext>());
    }

    public MessageContext registerMessage(Message message, Client sender) {
        MessageContext context = new MessageContext();
        context.setSender(sender);
        context.setMessage(message);
        synchronized (allContext) {
            contextMap.put(message, context);
            allContext.add(context);
        }
        return context;
    }

    public MessageContext getContext(Message msg) {
        return contextMap.get(msg);
    }

    public void forget(MessageContext context) {
        synchronized (allContext) {
            contextMap.remove(context.getMessage());
            allContext.remove(context);
        }
    }

    public void clean() {
        synchronized (allContext) {
            long now = System.currentTimeMillis();
            List<MessageContext> toForget = new ArrayList<MessageContext>();
            for (MessageContext context : allContext) {
                if (context.getReceivers().size() == 0) {
                    toForget.add(context);
                }
                if (now - context.getCreateTime() > 30000) {
                    toForget.add(context);
                }
            }
            for (MessageContext context : toForget) {
                contextMap.remove(context.getMessage());
                allContext.remove(context);
            }

        }
    }

    public void removeReceiver(MessageContext context, Client dest) {
        context.getReceivers().remove(dest);
        if (context.getReceivers().size() == 0) {
            forget(context);
        }
    }
}
