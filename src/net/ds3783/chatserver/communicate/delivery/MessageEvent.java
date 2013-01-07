package net.ds3783.chatserver.communicate.delivery;

import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:49
 * To change this template use File | Settings | File Templates.
 */
public class MessageEvent extends Event {
    private MessageContext context;
    private Message message;


    public MessageContext getContext() {
        return context;
    }

    public void setContext(MessageContext context) {
        this.context = context;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
