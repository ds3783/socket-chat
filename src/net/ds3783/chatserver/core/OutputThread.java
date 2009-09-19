package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 18:41:10
 */
public class OutputThread extends SlaveThread implements Runnable {
    private Log logger = LogFactory.getLog(InputThread.class);
    private LinkedBlockingQueue<Message> toSendMessages = new LinkedBlockingQueue<Message>();
    protected List<OutputFilter> filters = new ArrayList<OutputFilter>();
    private ClientDao clientDao;
    private ProcessThread processThread;

    public void assign(SocketChannel client, String uuid) throws ClosedChannelException {
        SelectionKey key = client.register(channelSelector, SelectionKey.OP_WRITE);
        userKeys.put(uuid, key);
        keyUsers.put(key, uuid);
    }

    public void send(Message message) {
        if (message != null) {
            try {
                toSendMessages.put(message);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    public void doRun() throws Exception {
        while (true) {
            doRemove();
            //向网络流中写入数据
            if (toSendMessages.isEmpty()) {
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
                continue;
            }
            while (!toSendMessages.isEmpty()) {
                Message message = toSendMessages.poll();
                String data = "";
                Client client = clientDao.getClientByName(message.getDestName());
                if (client==null) continue;
                SelectionKey key = userKeys.get(client.getUid());
                 if (key==null||!key.isValid()) continue;
                if (filters != null) {
                    for (OutputFilter filter : filters) {
                        data = filter.marshal(client, message, data);
                        data = filter.filte(client, data);
                    }
                }
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer writeBuffer = ByteBuffer.wrap(data.getBytes(charset));
                try {
                    channel.write(writeBuffer);
                } catch (IOException e) {
                    logger.warn(client.getName() + ":" + e.getMessage());
                    //用户已断线，清除该用户
                    this.remove(client.getUid());
                    processThread.addOfflineUser(client);
                }
            }

        }
    }

    public void destroy() throws Exception {
        userKeys.clear();
        channelSelector.close();
    }

    public void initialize() throws IOException {
        SelectorProvider provider = SelectorProvider.provider();
        channelSelector = provider.openSelector();
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setProcessThread(ProcessThread processThread) {
        this.processThread = processThread;
    }

    public void setFilters(List<OutputFilter> filters) {
        this.filters = filters;
    }
}
