package net.ds3783.chatserver;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:21:29
 */
public class Client {
    private String uid;
    private String name;
    private String ip;
    private Integer port;
    private String readThread;
    private String writeThread;
    private String type;
    private boolean authed;
    private Date connectTime;
    private Date lastMessageTime;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public Date getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Date connectTime) {
        this.connectTime = connectTime;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }


}
