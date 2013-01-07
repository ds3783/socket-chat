package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.delivery.InternalEvent;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-10-4
 * Time: 下午10:22
 * ClientService
 */
public class ClientService {
    private static Log logger = LogFactory.getLog(ClientService.class);


    private ThreadResource threadResource;
    private ClientDao clientDao;

    private MessageDispatcher messageDispatcher;

    /**
     * 添加临时用户
     *
     * @param client 客户端
     */
    public void addTempClient(Client client) {
        client.setAuthed(false);
        client.setLogined(false);
        clientDao.addClient(client);

    }

    public Client clientLogin(String uuid, String clientName, String authCode) {
        Client client = clientDao.getClient(uuid);
        client.setName(clientName);
        client.setAuthed(true);
        client.setToken(authCode);
        client.setLogined(true);
        clientDao.updateClient(client);
        return client;
    }

    public void setLastMessageTime(Client client, long now) {
        client.setLastMessageTime(now);
        clientDao.updateClient(client);
    }

    public void clientOffline(Client client) {
        SlaveThread readThread = (SlaveThread) threadResource.getThread(client.getReadThread());
        if (readThread != null) {
            readThread.remove(client.getUid());
        }
        SlaveThread writeThread = (SlaveThread) threadResource.getThread(client.getWriteThread());
        if (writeThread != null) {
            writeThread.remove(client.getUid());
        }
        InternalEvent evt = new InternalEvent();
        evt.setName(EventConstant.EVENT_CLIENT_OFFLINE);
        evt.setClient(client);
        this.messageDispatcher.dispatchEvent(evt);
        clientDao.removeClient(client);
        logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") 断开连接。");

    }


    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setThreadResource(ThreadResource threadResource) {
        this.threadResource = threadResource;
    }
}
