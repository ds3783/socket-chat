package net.ds3783.chatserver.messages.model;

import net.ds3783.chatserver.dao.Channel;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: ÏÂÎç1:59
 * To change this template use File | Settings | File Templates.
 */
public class ChannelModel implements Serializable {
    private Long id;
    private String name;
    private boolean internal;
    private int maxMember;


    public ChannelModel(Channel channel) {
        id = channel.getId();
        name = channel.getName();
        internal = channel.isInternal();
        maxMember = channel.getMaxMember();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }
}
