package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.protocol.OutputProtocal;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.tools.Utils;
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
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 18:41:10
 */
public class OutputThread extends SlaveThread implements Runnable {
    private Log logger = LogFactory.getLog(OutputThread.class);
    private LinkedBlockingQueue<Message> toSendMessages = new LinkedBlockingQueue<Message>();
    private LinkedBlockingQueue<Message> enmergencyMessages = new LinkedBlockingQueue<Message>();
    protected List<OutputFilter> filters = new ArrayList<OutputFilter>();
    private ClientDao clientDao;
    private ContextHelper contextHelper;
    private ClientService clientService;
    private OutputProtocal protocal;
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
        byte[] data = new byte[0];
        int counter = 0;
        if (filters != null) {
            try {
                //输出过滤器
                for (OutputFilter filter : filters) {
                    filter.filte(message);
                }
                //输出协议
                protocal.addMessage(message);
                data = protocal.marshal();

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        MessageContext context = contextHelper.getContext(message);
        if (context.getReceivers() != null && context.getReceivers().size() > 0) {
            int destSize = context.getReceivers().size();
            int mySize = userKeys.size();
            for (Client dest : context.getReceivers()) {
                if (userKeys.containsKey(dest.getUid())) {
                    doSend(dest, data, now);
                    contextHelper.removeReceiver(context, dest);
                    counter++;
                }
            }
        }
        if (context.isDropClientAfterReply()) {
            Client sender = context.getSender();
            if (sender != null && StringUtils.isNotEmpty(sender.getUid())) {
                if (!sender.isLogined()) {
                    clientService.clientOffline(sender);
                }
            }
        }

        return counter;
    }

    private void doSend(Client dest, byte[] data, long now) throws UnsupportedEncodingException {

        if (dest == null) return;
        SelectionKey key = userKeys.get(dest.getUid());
        if (key == null || !key.isValid()) return;

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer writeBuffer = ByteBuffer.wrap(data);
        try {
            logger.debug("say to: " + dest.getName() + ":" + new String(data) + "HEX VAL:" + Utils.toHexString(data));
            channel.write(writeBuffer);
            clientService.setLastMessageTime(dest, now);
        } catch (IOException e) {
            logger.warn(dest.getName() + ":" + e.getMessage());
            //用户已断线，清除该用户
            this.remove(dest.getUid());
            clientService.clientOffline(dest);
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

    public void setFilters(List<OutputFilter> filters) {
        this.filters = filters;
    }

    public void setMaxQueueLength(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
    }

    public void setMaxMessagePerTime(int maxMessagePerTime) {
        this.maxMessagePerTime = maxMessagePerTime;
    }

    public void setProtocal(OutputProtocal protocal) {
        this.protocal = protocal;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }
}
