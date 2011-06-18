package net.ds3783.chatserver.delivery;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç1:32
 * To change this template use File | Settings | File Templates.
 */
public class ChannelListener implements EventListener {
    private MessageDispatcher messageDispatcher;

    public void onEvent(Event event) {
        if (MessageType.CHAT_MESSAGE.equals(event.getMessage().getType())) {
            Event evt = new Event();
            evt.setName(event.getMessage().getChannel());
            evt.setMessage(event.getMessage());
            messageDispatcher.dispatchEvent(evt);
        }
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.CHAT_MESSAGE, this);
    }
}
