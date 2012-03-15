package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.SystemReplyMessage;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ����11:45
 * ������������
 */
public class BlackListListener extends DefaultCoreListener implements EventListener {
    private ClientDao clientDao;
    private ContextHelper contextHelper;

    public boolean onEvent(Event event) {
        Message msg = event.getMessage();
        MessageContext context = contextHelper.getContext(msg);
        Client client = context.getSender();
        if (clientDao.isInBlackList(client.getName(), System.currentTimeMillis())) {
            //����������
            SystemReplyMessage reply = new SystemReplyMessage();
            reply.setCode(SystemReplyMessage.CODE_ERROR_BLACKLIST);
            reply.setContent("�û��ѱ�����");
            contextHelper.registerMessage(reply, client);
            MessageContext context2 = contextHelper.getContext(reply);
            context2.getReceivers().add(client);
            outputerSwitcher.switchTo(reply);
            return false;
        }
        return true;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.CHAT_MESSAGE, MessageDispatcher.PRIORITY_HIGHEST, this);
    }
}
