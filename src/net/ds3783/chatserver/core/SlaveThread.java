package net.ds3783.chatserver.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

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
    protected LinkedBlockingQueue<String> toRemove = new LinkedBlockingQueue<String>();
    private Log logger = LogFactory.getLog(SlaveThread.class);


    public abstract void assign(SocketChannel client, String uuid) throws ClosedChannelException;


    public void remove(String uuid) {
        if (StringUtils.isEmpty(uuid)) return;
        try {
            toRemove.put(uuid);
        } catch (InterruptedException e) {
            logger.fatal(e.getMessage(), e);
        }
    }

    protected void doRemove() throws IOException {
        while (!toRemove.isEmpty()) {
            String uuid = toRemove.poll();
            SelectionKey key = userKeys.get(uuid);
            if (key != null) {
                userKeys.remove(uuid);
                keyUsers.remove(key);
                try {
                    key.channel().close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
                try {
                    key.cancel();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
                /*try {
                    channelSelector.keys().remove(key);
                } catch (Exception e) {

                }*/
            }
        }
    }

    public int getClients() {
        return clients;
    }

}
