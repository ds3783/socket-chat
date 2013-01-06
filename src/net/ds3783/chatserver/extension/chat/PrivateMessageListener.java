package net.ds3783.chatserver.extension.chat;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.*;
import net.ds3783.chatserver.extension.ClientException;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.PrivateMessage;
import net.ds3783.chatserver.messages.PublicMessage;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: 下午10:28
 * To change this template use File | Settings | File Templates.
 */
public class PrivateMessageListener extends DefaultChatListener implements EventListener {
    private ContextHelper contextHelper;
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        if (event.getName().equals(PrivateMessage.class.getSimpleName())) {
            PrivateMessage message = (PrivateMessage) event.getMessage();
            MessageContext context = contextHelper.getContext(message);
            Client target = clientDao.getClient(message.getReveiverId());
            if (target == null) {
                throw new ClientException(message.getReveiverName() + "或已离线！");
            }
            PrivateMessage reply = new PrivateMessage();
            reply.setSenderId(context.getSender().getUid());
            reply.setSenderName(context.getSender().getName());
            reply.setTimestamp(new Date());
            reply.setContent(message.getContent());
            MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
            replyContext.getReceivers().add(target);
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            return true;
        }
    }


    public void init() {
        chatDispatcher.addListener(PrivateMessage.class.getSimpleName(), this);
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }


    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
