package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.Client;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 15:42:53
 */
public class ClientDao {


    private HashMap<String, Client> clients = new HashMap<String, Client>();
    private HashMap<String, Client> clientsName = new HashMap<String, Client>();
    private HashMap<String, Client> clientsToken = new HashMap<String, Client>();
    private HashMap<String, Long> blackList = new HashMap<String, Long>();
    private HashMap<String, String> loginNames = new HashMap<String, String>();
    private HashMap<String, String> loginUids = new HashMap<String, String>();
    private final ReentrantLock takeLock = new ReentrantLock();

    public void addClient(Client client) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            clients.put(client.getUid(), client);
            clientsName.put(client.getName(), client);
        }
        finally {
            takeLock.unlock();
        }
    }

    /*
    public void gc() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            clients = new HashMap<String, Client>(clients);
            clientsName = new HashMap<String, Client>(clientsName);
            clientsToken = new HashMap<String, Client>(clientsToken);

            HashMap<String, Set<Client>> newclientsParty = new HashMap<String, Set<Client>>();
            for (String key : clientsParty.keySet()) {
                Set<Client> s = clientsParty.get(key);
                newclientsParty.put(key, new HashSet<Client>(s));
            }
            clientsParty = newclientsParty;

            HashMap<String, Set<Client>> newclientsNation = new HashMap<String, Set<Client>>();
            for (String key : clientsNation.keySet()) {
                Set<Client> s = clientsNation.get(key);
                newclientsNation.put(key, new HashSet<Client>(s));
            }
            clientsNation = newclientsNation;

            blackList = new HashMap<String, Long>(blackList);
            loginNames = new HashMap<String, String>(loginNames);
            loginUids = new HashMap<String, String>(loginUids);
        }
        finally {
            takeLock.unlock();
        }
    }
    */

    public void removeClient(String uuid) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null) {
                clients.remove(uuid);
                clientsName.remove(client.getName());
                if (client.getToken() != null && clientsToken.containsKey(client.getToken())) {
                    clientsToken.remove(client.getToken());
                }
                if (loginNames.containsKey(client.getName())) {
                    loginNames.remove(client.getName());
                }
                if (loginUids.containsKey(client.getUid())) {
                    loginUids.remove(client.getUid());
                }
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public Client getClient(String uuid) {
        /*
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
        */
        if (clients.containsKey(uuid)) {
            return clients.get(uuid);
        } else {
            return new Client();
        }
        /*
        }
        finally {
            takeLock.unlock();
        }
        */
    }

    public Client getClientByName(String name) {
        /*
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
        */
        return clientsName.get(name);
        /*
        }
        finally {
            takeLock.unlock();
        }
        */
    }

    public List<Client> getAllClients() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return new ArrayList<Client>(clients.values());
        }
        finally {
            takeLock.unlock();
        }

    }


    public Client getClientByToken(String token) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return clientsToken.get(token);
        }
        finally {
            takeLock.unlock();
        }

    }

    public void updateClientName(String uuid, String newName) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null) {
                clientsName.remove(client.getName());
                client.setName(newName);
                clientsName.put(newName, client);
            }
        }
        finally {
            takeLock.unlock();
        }

    }

    public void updateClientToken(String uuid, String token) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null) {
                if (client.getToken() != null) {
                    if (clientsToken.containsKey(client.getToken())) {
                        clientsToken.remove(client.getToken());
                    }
                }
                client.setToken(token);
                clientsToken.put(token, client);
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public int getClintAmount() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return clients.size();
        }
        finally {
            takeLock.unlock();
        }

    }

    public void addToBlackList(String uuid) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null) {
                blackList.put(client.getName(), System.currentTimeMillis());
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public void removeFromBlackList(String uuid) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null && blackList.containsKey(client.getName())) {
                blackList.remove(client.getName());
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public boolean isInBlackList(String userName, long expireTime, long now) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (StringUtils.isEmpty(userName)) return false;
            if (!blackList.containsKey(userName)) {
                return false;
            }
            long listtime = blackList.get(userName);
            if (now - listtime < expireTime) {
                return true;
            } else {
                if (blackList.containsKey(userName)) {
                    blackList.remove(userName);
                }
                return false;
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public void updateClientLogined(String uuid, boolean b) {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Client client = clients.get(uuid);
            if (client != null) {
                if (client.getNation() != null) {
                    client.setLogined(true);
                    loginNames.put(client.getName(), client.getUid());
                    loginUids.put(client.getUid(), client.getUid());
                }
            }
        }
        finally {
            takeLock.unlock();
        }
    }

    public List<String> getLoginClientNames() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return new ArrayList<String>(loginNames.keySet());
        }
        finally {
            takeLock.unlock();
        }
    }


    public Map<String, String> getLoginClientUids() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            return loginUids;
        }
        finally {
            takeLock.unlock();
        }
    }
}
