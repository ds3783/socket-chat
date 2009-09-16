package net.ds3783.chatserver.core;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 17:53:34
 */
public abstract class SlaveThread extends CommonRunnable implements Runnable {
    protected int clients;
    protected Selector channelSelector;
    protected Hashtable<String, SelectionKey> userKeys = new Hashtable<String, SelectionKey>();
    protected Hashtable<SelectionKey, String> keyUsers = new Hashtable<SelectionKey, String>();
    protected String charset = "GBK";


    public abstract void assign(SocketChannel client, String uuid) throws ClosedChannelException;


    public void remove(String uuid) throws IOException {
        if (StringUtils.isEmpty(uuid)) return;
        SelectionKey key = userKeys.get(uuid);
        if (key != null) {
            userKeys.remove(uuid);
            keyUsers.remove(key);
            key.channel().close();
            key.cancel();
        }
    }

    public int getClients() {
        return clients;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
