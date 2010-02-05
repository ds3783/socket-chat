package net.ds3783.chatserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 18:16:41
 */
public class Configuration implements Serializable {
    private int readThread = 1;
    private int writeThread = 1;
    private String address;
    private long blackListKeepTime = 10 * 60 * 1000;
    private int port;

    public int getReadThread() {
        return readThread;
    }

    public void setReadThread(int readThread) {
        this.readThread = readThread;
    }

    public int getWriteThread() {
        return writeThread;
    }

    public void setWriteThread(int writeThread) {
        this.writeThread = writeThread;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getBlackListKeepTime() {
        return blackListKeepTime;
    }

    public void setBlackListKeepTime(long blackListKeepTime) {
        this.blackListKeepTime = blackListKeepTime;
    }
}
