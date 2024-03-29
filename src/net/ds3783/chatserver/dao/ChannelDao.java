package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.tools.Utils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: 锟斤拷锟斤拷2:37
 * To change this template use File | Settings | File Templates.
 */
public class ChannelDao extends HibernateDaoSupport {
    public List<Channel> getChannels() {
        return Utils.castList(getHibernateTemplate().find("from Channel"), Channel.class);
    }

    public void registerChannel(Channel newChannel) {
        getHibernateTemplate().saveOrUpdate(newChannel);
        getHibernateTemplate().flush();
    }

    public List<ClientChannel> getMyChannels(String clientid) {
        return Utils.castList(getHibernateTemplate().find("from ClientChannel where  clientId=?", clientid), ClientChannel.class);
    }


    public List<ClientChannel> getClientsByChannel(Long channelId) {
        return Utils.castList(getHibernateTemplate().find("from ClientChannel where channelId =?", channelId), ClientChannel.class);
    }

    public void removeClientChannel(ClientChannel clientChannel) {
        getHibernateTemplate().delete(clientChannel);
        getHibernateTemplate().flush();
    }

    public void addClientChannel(ClientChannel cc) {
        getHibernateTemplate().save(cc);
        getHibernateTemplate().flush();
    }

    public void deleteClientChannel(ClientChannel inChannel) {
        getHibernateTemplate().delete(inChannel);
        getHibernateTemplate().flush();
    }

    public Channel getChannel(Long channelid) {
        return (Channel) getHibernateTemplate().get(Channel.class, channelid);
    }

    public Channel getChannelByName(String channelName) {
        List channels = getHibernateTemplate().find("from Channel c where c.name = ?", channelName);
        if (channels != null && channels.size() > 0) {
            return (Channel) channels.get(0);
        }
        return null;
    }

    public boolean isInChannel(String uid, Long channelId) {
        Long count = 0L;
        List rs = getHibernateTemplate().find("select count(*) as CONT from ClientChannel c where c.clientId=? and c.channelId=? ", new Object[]{uid, channelId});
        for (Object r : rs) {
            count = (Long) r;
        }
        return count > 0;
    }

    public Long getClientsAmountInChannel(Long channelId) {
        List rs = getHibernateTemplate().find("select count(*) as CT from ClientChannel c where c.channelId=?", channelId);
        return (Long) rs.get(0);
    }

    public void deleteChannel(Channel channel) {
        getHibernateTemplate().delete(channel);
        getHibernateTemplate().flush();
    }
}
