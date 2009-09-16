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
}
