package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.core.ClientService;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.messages.LoginMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.SystemReplyMessage;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ����11:07
 */
public class LoginListener extends AbstractDefaultListener implements EventListener {
    private static Log logger = LogFactory.getLog(LoginListener.class);
    private ClientDao clientDao;
    private ClientService clientService;
    private ContextHelper contextHelper;

    public boolean onEvent(Event event) {
        if (!MessageType.LOGIN_MESSAGE.equals(event.getMessage().getType())) {
            return true;
        }
        LoginMessage login = (LoginMessage) event.getMessage();
        //��¼
        MessageContext context = contextHelper.getContext(login);
        SystemReplyMessage reply = new SystemReplyMessage();
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        //��¼�ɹ�
        if (clientDao.getClientByName(reply.getContent()) != null) {
            //֪ͨ��������������������
            replyContext.setDropClientAfterReply(true);
            reply.setContent("��ǰ�������û�");
            reply.setCode(SystemReplyMessage.CODE_ERROR_WRONG_PASSWORD);
            replyContext.getReceivers().add(context.getSender());
            logger.info("��ǰ�������û�:" + reply.getContent());
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            context.getSender().setName(login.getUsername());
            SystemReplyMessage reply2 = new SystemReplyMessage();
            MessageContext replyContext2 = contextHelper.registerMessage(reply2, context.getSender());
            //ȫ�ֹ㲥ĳ������
            replyContext2.setReceivers(clientDao.getAllClients());
            reply2.setContent(context.getSender().getName() + " �ɹ���¼");
            reply2.setCode(SystemReplyMessage.CODE_USER_ONLINE);
            outputerSwitcher.switchTo(reply2);

            Client client = clientService.clientLogin(context.getSender().getUid(), reply.getContent(), Utils.newUuid());
            reply.setCode(SystemReplyMessage.CODE_LOGIN_SUCCESS);
            logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") �ɹ���¼��");
            outputerSwitcher.switchTo(reply);

            logger.debug(reply.getContent() + " online");
            return false;
        }

    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.LOGIN_MESSAGE, this);
    }
}
