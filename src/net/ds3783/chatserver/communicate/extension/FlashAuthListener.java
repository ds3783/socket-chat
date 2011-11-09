package net.ds3783.chatserver.communicate.extension;

import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ����1:38
 * To change this template use File | Settings | File Templates.
 */
public class FlashAuthListener extends AbstractDefaultListener implements EventListener {
    private Configuration config;
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        Message msg = event.getMessage();
        Message reply = new Message();
        reply.setContent("<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"" + config.getAddress() + "\" to-ports=\"" + config.getPort() + "\"/></cross-domain-policy>\r\n");

        Client client = clientDao.getClient(msg.getUserUuid());
        client.setAuthed(true);
        HashSet<String> destUsers = new HashSet<String>();
        destUsers.add(client.getUid());
        reply.setDestUserUids(destUsers);
        reply.setUserUuid(msg.getUserUuid());
        reply.setChannel(msg.getChannel());
        reply.setType(msg.getType());
        reply.setDropClientAfterReply(true);
        outputerSwitcher.switchTo(reply);
        return true;
    }


    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.AUTH_MESSAGE, this);
    }
}