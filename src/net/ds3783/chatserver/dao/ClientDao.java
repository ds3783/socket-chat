package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.Client;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 15:42:53
 */
public class ClientDao {
    private Hashtable<String, Client> clients = new Hashtable<String, Client>();
    private Hashtable<String, Client> clientsName = new Hashtable<String, Client>();

    public void addClient(Client client) {
        clients.put(client.getUid(), client);
        clientsName.put(client.getName(), client);
    }

    public void removeClient(String uuid) {
        Client client = clients.get(uuid);
        if (client != null) {
            clients.remove(uuid);
            clientsName.remove(client.getName());
        }
    }

    public Client getClient(String uuid) {
        return clients.get(uuid);
    }

    public Client getClientByName(String name) {
        return clientsName.get(name);
    }

    public List<Client> getAllClients() {
        return new ArrayList<Client>(clients.values());
    }

    public List<Client> getClientsByParty(String party) {
        //TODO:
        return null;
    }
}
