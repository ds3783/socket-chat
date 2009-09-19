package net.ds3783.chatserver.delivery;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.dao.ClientDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 17:48:52
 */
public class MessageProcessor {
    private ClientDao clientDao;

    public List<Message> processMsg(Message msg) {
        List<Client> clients = clientDao.getAllClients();
        List<String> destUsers = new ArrayList<String>();
        for (Client client : clients) {
            destUsers.add(client.getName());
        }
        msg.setDestUserName(destUsers);
        List<Message> result = new ArrayList<Message>();
        result.add(msg);
        return result;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
