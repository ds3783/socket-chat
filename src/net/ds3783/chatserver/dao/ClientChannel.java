package net.ds3783.chatserver.dao;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-31
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class ClientChannel implements Serializable {
    private Long id;
    private Long channelId;
    private String clientId;
    private boolean current;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
