package net.ds3783.chatserver.extension;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.Event;
import net.ds3783.chatserver.delivery.EventListener;
import net.ds3783.chatserver.delivery.MessageDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class BlackListListener extends AbstractDefaultListener implements EventListener {
    private Configuration config;
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        Message msg = event.getMessage();
        Client client = clientDao.getClient(msg.getUserUuid());
        if (client == null) {
            //TODO::log.warn
            return false;
        }
        if (clientDao.isInBlackList(client.getName(), config.getBlackListKeepTime(), System.currentTimeMillis())) {
            //黑名单处理
            Message reply = msg.simpleClone();
            reply.setType(MessageType.CHAT_MESSAGE);
            reply.setChannel("SYSTEM");
            reply.setContent("用户已被禁言");
            reply.getDestUserUids().put(client.getName(), client.getUid());
            outputerSwitcher.switchTo(reply);
            return false;
        }
        return true;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.CHAT_MESSAGE, MessageDispatcher.PRIORITY_HIGHEST, this);
    }
}
