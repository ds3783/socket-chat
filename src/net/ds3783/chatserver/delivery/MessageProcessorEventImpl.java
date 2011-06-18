package net.ds3783.chatserver.delivery;

import net.ds3783.chatserver.Message;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç12:11
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessorEventImpl implements MessageProcessor {
    private MessageDispatcher messageDispatcher;


    public void processMsg(Message msg, long now) {
        Event evt = new Event();
        evt.setName(msg.getType());
        evt.setMessage(msg);
        messageDispatcher.dispatchEvent(evt);
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }
}
