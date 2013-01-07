package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.communicate.delivery.MessageEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: 下午10:11
 * To change this template use File | Settings | File Templates.
 */
public class ChatListener implements EventListener {
    private MessageDispatcher chatDispatcher;
    private MessageDispatcher messageDispatcher;

    public boolean onEvent(Event messageEvent) {
        MessageEvent event = (MessageEvent) messageEvent;
        if (EventConstant.CHAT_MESSAGE.equals(event.getMessage().getType())) {
            //拦截所有CommandMessage
            MessageEvent evt = new MessageEvent();
            evt.setName(event.getMessage().getClass().getSimpleName());
            evt.setMessage(event.getMessage());
            chatDispatcher.dispatchEvent(evt);
            return false;
        } else {
            return true;
        }
    }

    public void init() {
        messageDispatcher.addListener(EventConstant.CHAT_MESSAGE, this);
    }

    public void setChatDispatcher(MessageDispatcher chatDispatcher) {
        this.chatDispatcher = chatDispatcher;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }
}

