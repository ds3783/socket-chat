package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.messages.FlashAuthMessage;
import net.ds3783.chatserver.messages.MessageContext;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午1:38
 * 处理Flash 访问授权验证
 */
public class FlashAuthListener extends DefaultCoreListener implements EventListener {
    private Configuration config;
    private ContextHelper contextHelper;

    public boolean onEvent(Event event) {
        net.ds3783.chatserver.messages.Message msg = event.getMessage();
        if (MessageType.AUTH_MESSAGE.equals(msg.getType())) {
            FlashAuthMessage reply = new FlashAuthMessage();
            reply.setContent("<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"" + config.getAddress() + "\" to-ports=\"" + config.getPort() + "\"/></cross-domain-policy>\r\n");

            MessageContext context = contextHelper.getContext(msg);
            context.getSender().setAuthed(true);
            context = contextHelper.registerMessage(reply, context.getSender());
            context.getReceivers().add(context.getSender());
            context.setDropClientAfterReply(true);
            outputerSwitcher.switchTo(reply);
            return false;
        }
        return true;

    }


    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.AUTH_MESSAGE, this);
    }
}
