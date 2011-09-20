package net.ds3783.chatserver.extension;

import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.Channel;
import net.ds3783.chatserver.delivery.Event;
import net.ds3783.chatserver.delivery.EventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public class LoginListener extends AbstractDefaultListener implements EventListener {
    private static Log logger = LogFactory.getLog(LoginListener.class);
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        Message reply = event.getMessage().simpleClone();
        //登录
        reply.getDestUserUids().add(reply.getUserUuid());
        //登录成功
        if (clientDao.getClientByName(reply.getContent()) != null) {
            //通知此人有重名，并踢下线
            reply.setDropClientAfterReply(true);
            reply.setType(MessageType.CHAT_MESSAGE);
            reply.setChannel(Channel.SYSTEM.getCode());
            reply.setContent("当前有重名用户");
            reply.setAuthCode("false");
            logger.info("当前有重名用户:"+reply.getContent());
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            clientDao.updateClientName(reply.getUserUuid(), reply.getContent());
            clientDao.updateClientToken(reply.getUserUuid(), reply.getAuthCode());
            clientDao.updateClientLogined(reply.getUserUuid(), true);
            reply.setAuthCode("true");
            Client client = clientDao.getClient(reply.getUserUuid());
            logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") 成功登录。");
            outputerSwitcher.switchTo(reply);

            //全局广播某人上线
            Message broadCast = reply.simpleClone();
            broadCast.setDestUserUids(new HashSet<String>(clientDao.getLoginClientUids()));
            broadCast.setType(MessageType.CHAT_MESSAGE);
            broadCast.setChannel(Channel.SYSTEM.getCode());
            broadCast.setContent(client.getName() + " 成功登录");
            outputerSwitcher.switchTo(broadCast);
            logger.debug(reply.getContent() + " online");
            return true;
        }

    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.LOGIN_MESSAGE, this);
    }
}
