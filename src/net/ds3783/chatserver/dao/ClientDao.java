package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.tools.Utils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 15:42:53
 */
public class ClientDao extends HibernateDaoSupport {


    public void addClient(Client client) {
        SessionFactory sf = getSessionFactory();
        for (Object s : sf.getAllClassMetadata().keySet()) {
            ClassMetadata m = (ClassMetadata) sf.getAllClassMetadata().get(s);
        }

        getHibernateTemplate().save(client);
        getHibernateTemplate().flush();
    }


    public void removeClient(Client client) {
        getHibernateTemplate().delete(client);
        getHibernateTemplate().flush();
    }

    public Client getClient(String uuid) {
        return (Client) getHibernateTemplate().get(Client.class, uuid);
    }

    public Client getClientByName(String name) {
        List rs = getHibernateTemplate().find("from Client c where c.name=?", name);
        if (rs.size() > 0) {
            return (Client) rs.get(0);
        }
        return null;
    }

    public List<Client> getAllClients() {
        return Utils.castList(getHibernateTemplate().loadAll(Client.class), Client.class);
    }


    public Client getClientByToken(String token) {
        List rs = getHibernateTemplate().find("from Client c where c.token=?", token);
        if (rs.size() > 0) {
            return (Client) rs.get(0);
        }
        return null;
    }

    @Deprecated
    public void updateClientName(String uuid, String newName) {
        Client client = (Client) getHibernateTemplate().load(Client.class, uuid);
        client.setName(newName);
        getHibernateTemplate().saveOrUpdate(client);
        getHibernateTemplate().flush();
    }

    @Deprecated
    public void updateClientToken(String uuid, String token) {
        Client client = (Client) getHibernateTemplate().load(Client.class, uuid);
        client.setToken(token);
        getHibernateTemplate().saveOrUpdate(client);
        getHibernateTemplate().flush();
    }

    public long getClintAmount() {
        List rs = getHibernateTemplate().find("select count(*) as CT from Client");
        return (Long) rs.get(0);
    }

    public void addToBlackList(String uuid, long timelong) {
        BlackList blackList = getBlackList(uuid);
        long now = System.currentTimeMillis();
        if (blackList == null) {
            blackList = new BlackList();
            blackList.setUid(uuid);
            blackList.setBlackTime(now);
        }
        blackList.setExpiredTime(now + timelong);
        getHibernateTemplate().saveOrUpdate(blackList);
        getHibernateTemplate().flush();
    }

    private BlackList getBlackList(String uuid) {
        List rs = getHibernateTemplate().find("from BlackList where uid=?", uuid);
        if (rs.size() > 0) {
            return (BlackList) rs.get(0);
        } else {
            return null;
        }
    }

    public void removeFromBlackList(String uuid) {
        BlackList blackList = getBlackList(uuid);
        if (blackList != null) {
            getHibernateTemplate().delete(blackList);
            getHibernateTemplate().flush();
        }
    }

    public boolean isInBlackList(String userName, long now) {
        Client client = getClientByName(userName);
        BlackList blackList = getBlackList(client.getUid());
        if (blackList != null) {
            if (now < blackList.getExpiredTime()) {
                return true;
            } else {
                getHibernateTemplate().delete(blackList);
                getHibernateTemplate().flush();
            }
        }
        return false;
    }

    @Deprecated
    public void updateClientLogined(String uuid, boolean b) {
        Client client = getClient(uuid);
        client.setLogined(b);
        getHibernateTemplate().update(client);
        getHibernateTemplate().flush();
    }


    public Set<String> getLoginClientUids() {
        List rs = getHibernateTemplate().find("select uid from Client where logined=true");
        Set<String> result = new HashSet<String>();
        for (Object r : rs) {
            result.add((String) r);
        }
        return result;
    }

    public void updateClient(Client client) {
        getHibernateTemplate().update(client);
        getHibernateTemplate().flush();
    }

    public Collection<Client> getNotLoginedClients() {
        return Utils.castList(getHibernateTemplate().find("from Client where logined=?", false), Client.class);
    }
}
