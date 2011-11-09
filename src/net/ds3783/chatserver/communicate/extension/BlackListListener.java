package net.ds3783.chatserver.communicate.extension;

import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class BlackListListener extends AbstractDefaultListener implements EventListener {
    private static Log logger = LogFactory.getLog(BlackListListener.class);
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        Message msg = event.getMessage();
        Client client = clientDao.getClient(msg.getUserUuid());
        if (client == null) {
            logger.warn("用户掉线？？:" + msg.getUserUuid());
            return false;
        }
        if (clientDao.isInBlackList(client.getName(), System.currentTimeMillis())) {
            //黑名单处理
            Message reply = msg.simpleClone();
            reply.setType(MessageType.CHAT_MESSAGE);
            reply.setChannel("SYSTEM");
            reply.setContent("用户已被禁言");
            reply.getDestUserUids().add(client.getUid());
            outputerSwitcher.switchTo(reply);
            return false;
        }
        return true;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.CHAT_MESSAGE, MessageDispatcher.PRIORITY_HIGHEST, this);
    }
}
