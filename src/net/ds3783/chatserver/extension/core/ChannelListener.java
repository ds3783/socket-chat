package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午1:32
 * To change this template use File | Settings | File Templates.
 */
public class ChannelListener implements EventListener {

    private ContextHelper contextHelper;
    private MessageDispatcher messageDispatcher;
    private MessageDispatcher channelDispatcher;

    public boolean onEvent(Event event) {
        /*if (MessageType.CHAT_MESSAGE.equals(event.getMessage().getType())) {

            MessageContext context = contextHelper.getContext(event.getMessage());
            Event evt = new Event();
            evt.setName(context.getSender().getChannel());
            evt.setMessage(event.getMessage());
            channelDispatcher.dispatchEvent(evt);
        }*/
        return true;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setChannelDispatcher(MessageDispatcher channelDispatcher) {
        this.channelDispatcher = channelDispatcher;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void init() {
        //messageDispatcher.addListener(MessageType.CHAT_MESSAGE, this);
    }
}
