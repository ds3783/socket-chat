package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.communicate.delivery.MessageEvent;
import net.ds3783.chatserver.messages.CommandMessage;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-2
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
public class CommandListener implements EventListener {
    private MessageDispatcher commandDispatcher;
    private MessageDispatcher messageDispatcher;

    public boolean onEvent(Event messageEvent) {
        MessageEvent event = (MessageEvent) messageEvent;
        if (EventConstant.COMMAND_MESSAGE.equals(event.getMessage().getType())) {
            //拦截所有CommandMessage
            MessageEvent evt = new MessageEvent();
            CommandMessage msg = (CommandMessage) event.getMessage();
            evt.setName(msg.getCommand());
            evt.setMessage(msg);
            commandDispatcher.dispatchEvent(evt);
            return false;
        } else {
            return true;
        }
    }

    public void init() {
        messageDispatcher.addListener(EventConstant.COMMAND_MESSAGE, this);
    }

    public void setCommandDispatcher(MessageDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }
}
