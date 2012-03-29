package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.tools.Utils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: ÏÂÎç2:37
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
}
