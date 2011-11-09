package net.ds3783.chatserver.dao;

import net.ds3783.chatserver.communicate.delivery.Channel;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:21:29
 */
public class Client {
    private String uid;
    private String token;
    private String name;
    private String type;
    private String ip;
    private Integer port;
    private String readThread;
    private String writeThread;
    private boolean authed;
    private boolean logined;
    private long connectTime;
    private long lastMessageTime;

    private String channel;

    public Client() {
        token = "";
        name = "";
        type = ClientType.USER;
        ip = "";
        port = 0;
        readThread = "";
        writeThread = "";
        authed = false;
        logined = false;
        connectTime = 0;
        lastMessageTime = 0;

        channel = Channel.PUBLIC.getCode();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getReadThread() {
        return readThread;
    }

    public void setReadThread(String readThread) {
        this.readThread = readThread;
    }

    public String getWriteThread() {
        return writeThread;
    }

    public void setWriteThread(String writeThread) {
        this.writeThread = writeThread;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return !(name != null ? !name.equals(client.name) : client.name != null) && !(type != null ? !type.equals(client.type) : client.type != null) && !(uid != null ? !uid.equals(client.uid) : client.uid != null);

    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
