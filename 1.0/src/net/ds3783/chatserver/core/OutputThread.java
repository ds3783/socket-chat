package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collection;
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
    private LinkedBlockingQueue<Message> enmergencyMessages = new LinkedBlockingQueue<Message>();
    protected List<OutputFilter> filters = new ArrayList<OutputFilter>();
    private ClientDao clientDao;
    private ProcessThread processThread;
    private int maxQueueLength = 500;
    private int maxMessagePerTime = 100;

    public void assign(SocketChannel client, String uuid) throws ClosedChannelException {
        SelectionKey key = client.register(channelSelector, SelectionKey.OP_WRITE);
        userKeys.put(uuid, key);
        keyUsers.put(key, uuid);
    }

    public void send(Message message) {
        if (message != null) {
            try {
                if (MessageType.AUTH_MESSAGE.equals(message.getType()) || MessageType.LOGIN_MESSAGE.equals(message.getType())) {
                    enmergencyMessages.put(message);
                } else if (toSendMessages.size() < maxQueueLength) {
                    toSendMessages.put(message);
                } else {
                    toSendMessages.put(message);
//                    logger.fatal("dropped Message:" + Utils.describeBean(message));
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    public void doRun() throws Exception {
        while (true) {
            doRemove();
            try {
                channelSelector.selectedKeys().clear();
                channelSelector.select(1000);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                //选择器故障
                try {
                    channelSelector.keys().clear();
                } catch (Exception e1) {
                    logger.error(e.getMessage(), e);
                }
                break;
            }
            //向网络流中写入数据
            if (toSendMessages.isEmpty() && enmergencyMessages.isEmpty()) {
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
                continue;
            }
            long now = System.currentTimeMillis();
            while (!enmergencyMessages.isEmpty()) {
                Message message = enmergencyMessages.poll();
                sendMessage(message, now);
            }
            int counter = 0;
            while (!toSendMessages.isEmpty()) {
                Message message = toSendMessages.poll();
                counter += sendMessage(message, now);
                if (maxMessagePerTime < counter) {
                    break;
                }
            }

        }
    }

    private int sendMessage(Message message, long now) throws UnsupportedEncodingException {
        String data = "";
        int counter = 0;
        if (filters != null) {
            try {
                for (OutputFilter filter : filters) {
                    data = filter.marshal(message, data);
                    data = filter.filte(data);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (message.getDestUserUids() != null && message.getDestUserUids().size() > 0) {
            int destSize = message.getDestUserUids().size();
            int mySize = userKeys.size();
            if (destSize < mySize) {
                for (String destuid : message.getDestUserUids().keySet()) {
                    if (userKeys.containsKey(destuid)) {
                        doSend(destuid, data, now);
                        counter++;
                    }
                }
            } else {
                Collection<String> uuids = new ArrayList<String>(userKeys.keySet());
                for (String uuid : uuids) {
                    if (message.getDestUserUids().containsKey(uuid)) {
                        doSend(uuid, data, now);
                        counter++;
                    }
                }
            }
        } else {
            String destuid = message.getDestUid();
            doSend(destuid, data, now);
            counter++;
        }
        if (message.isDropClientAfterReply()) {
            Client sender = clientDao.getClient(message.getUserUuid());
            if (sender != null && StringUtils.isNotEmpty(sender.getUid())) {
                if (!sender.isLogined()) {
                    processThread.addOfflineUser(sender);
                }
            }
        }
        return counter;
    }

    private void doSend(String destuid, String data, long now) throws UnsupportedEncodingException {

        Client client = clientDao.getClient(destuid);
        if (client == null) return;
        SelectionKey key = userKeys.get(client.getUid());
        if (key == null || !key.isValid()) return;

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer writeBuffer = ByteBuffer.wrap(data.getBytes(charset));
        try {
            logger.debug("say to: " + client.getName() + ":" + data);
            channel.write(writeBuffer);
            client.setLastMessageTime(now);
        } catch (IOException e) {
            logger.warn(client.getName() + ":" + e.getMessage());
            //用户已断线，清除该用户
            this.remove(client.getUid());
            processThread.addOfflineUser(client);
        }
    }


    public void cleanuUp() throws Exception {


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

    public void setMaxQueueLength(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
    }

    public void setMaxMessagePerTime(int maxMessagePerTime) {
        this.maxMessagePerTime = maxMessagePerTime;
    }
}
