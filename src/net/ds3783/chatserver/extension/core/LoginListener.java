package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.core.ClientService;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageEvent;
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
 * Time: 上午11:07
 */
public class LoginListener extends DefaultCoreListener implements EventListener {
    private static Log logger = LogFactory.getLog(LoginListener.class);
    private ClientDao clientDao;
    private ClientService clientService;
    private ContextHelper contextHelper;

    public boolean onEvent(Event messageEvent) {
        MessageEvent event = (MessageEvent) messageEvent;
        if (!EventConstant.LOGIN_MESSAGE.equals(event.getMessage().getType())) {
            return true;
        }
        LoginMessage login = (LoginMessage) event.getMessage();
        //登录
        MessageContext context = contextHelper.getContext(login);
        SystemReplyMessage reply = new SystemReplyMessage();
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        //登录成功
        if (clientDao.getClientByName(login.getUsername()) != null) {
            //通知此人有重名，并踢下线
            replyContext.setDropClientAfterReply(true);
            reply.setContent("当前有重名用户");
            reply.setCode(SystemReplyMessage.CODE_ERROR_WRONG_PASSWORD);
            replyContext.getReceivers().add(context.getSender());
            logger.info("当前有重名用户:" + reply.getContent());
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            context.getSender().setName(login.getUsername());
            SystemReplyMessage reply2 = new SystemReplyMessage();
            //全局广播某人上线
            reply2.setContent(context.getSender().getName() + " 成功登录");
            MessageContext replyContext2 = contextHelper.registerMessage(reply2, context.getSender());
            replyContext2.getReceivers().addAll(clientDao.getAllClients());
            reply2.setCode(SystemReplyMessage.CODE_OTHER_USER_ONLINE);
            outputerSwitcher.switchTo(reply2);

            Client client = clientService.clientLogin(context.getSender().getUid(), login.getUsername(), Utils.newUuid());
            reply.setCode(SystemReplyMessage.CODE_LOGIN_SUCCESS);
            replyContext.getReceivers().add(client);
            logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") 成功登录。");
            outputerSwitcher.switchTo(reply);

            logger.debug(login.getUsername() + " online");
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
        messageDispatcher.addListener(EventConstant.LOGIN_MESSAGE, this);
    }
}
