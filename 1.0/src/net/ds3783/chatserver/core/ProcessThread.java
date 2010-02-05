package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.MessageProcessor;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:48
 */
public class ProcessThread extends CommonRunnable implements Runnable {
    private Log logger = LogFactory.getLog(ProcessThread.class);
    private LinkedBlockingQueue<Message> receivedMessages = new LinkedBlockingQueue<Message>();
    private LinkedBlockingQueue<Message> enmergencyMessages = new LinkedBlockingQueue<Message>();
    private LinkedBlockingQueue<Client> toKickClient = new LinkedBlockingQueue<Client>();


    private MessageProcessor messageProcessor;
    private ClientDao clientDao;
    private ThreadResource threadResource;
    private long maxMessageInQueue = 100;
    private int maxMessagePerTime = 100;

    public void addMessage(Message message) {
        try {
            if ((MessageType.AUTH_MESSAGE.equals(message.getType()) || MessageType.LOGIN_MESSAGE.equals(message.getType()))) {
                enmergencyMessages.put(message);
            } else if (receivedMessages.size() > maxMessageInQueue) {
                //超出最大消息数量限制
                logger.fatal("系统负载大,待处理消息数量:" + receivedMessages.size());
                logger.warn("Dropped Message:" + Utils.describeBean(message));
            } else {
                receivedMessages.put(message);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void doRun() throws Exception {
        HashMap<String, Client> kickedClent = new HashMap<String, Client>();
        while (true) {
            boolean nothingtodo = true;
            kickedClent.clear();

            if (!toKickClient.isEmpty()) nothingtodo = false;
            while (!toKickClient.isEmpty()) {
                Client client = toKickClient.poll();
                kickedClent.put(client.getName(), client);

                //清除服务线程资源
                clientDao.removeClient(client.getUid());
                SlaveThread writeThread = (SlaveThread) threadResource.getThread(client.getWriteThread());
                SlaveThread readThread = (SlaveThread) threadResource.getThread(client.getReadThread());
                try {
                    if (readThread != null)
                        readThread.remove(client.getUid());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                try {
                    if (writeThread != null)
                        writeThread.remove(client.getUid());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                if (client.isLogined()) {
                    logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") 断开连接。");
                }
            }

            if (!enmergencyMessages.isEmpty()) nothingtodo = false;
            long now = System.currentTimeMillis();
            while (!enmergencyMessages.isEmpty()) {
                Message msg = enmergencyMessages.poll();
                deliverMessage(msg, now, kickedClent);
            }


            if (!receivedMessages.isEmpty()) nothingtodo = false;
            now = System.currentTimeMillis();
            int counter = 0;
            while (!receivedMessages.isEmpty()) {
                Message msg = receivedMessages.poll();
                deliverMessage(msg, now, kickedClent);
                counter++;
                if (counter > maxMessagePerTime) {
                    break;
                }
            }
            if (nothingtodo) {
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                    break;
                }
            }

        }

    }

    public void deliverMessage(Message msg, long now, Map<String, Client> kickedClent) {
        if (msg == null) return;
        List<Message> toDeliver = messageProcessor.processMsg(msg, now);
        if (toDeliver != null) {
            for (Message message : toDeliver) {
                if (MessageType.CHAT_MESSAGE.equals(message.getType()) && "BROADCAST".equals(message.getSubType())) {
                    List<CommonRunnable> outputs = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
                    for (CommonRunnable output : outputs) {
                        OutputThread opThread = (OutputThread) output;
                        opThread.send(message);
                    }
                } else {
                    for (String destUserId : message.getDestUserUids().keySet()) {
                        Client client = clientDao.getClient(destUserId);
                        if (client != null) {
                            if (kickedClent.containsKey(client.getName())) continue;
                            OutputThread writethread = (OutputThread) threadResource.getThread(client.getWriteThread());
                            if (writethread == null) continue;
                            Message mm = message.simpleClone();
                            mm.setDestUid(client.getUid());
                            writethread.send(mm);
                        }
                    }
                }
            }
        }
    }


    public void sendEchoMessage(Client client) {
        Message msg = new Message();
        msg.setUserUuid(client.getUid());
        msg.setType(MessageType.COMMAND_MESSAGE);
        msg.setContent("echo");
        msg.setSubType("ECHO");
        msg.setDestUid(client.getUid());
        this.addMessage(msg);
    }

    public void addOfflineUser(Client client) {
        try {
            toKickClient.put(client);
        } catch (InterruptedException e) {
            logger.fatal(e.getMessage(), e);
        }
    }

    public void destroy() throws Exception {

    }

    public void cleanuUp() throws Exception {

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setThreadResource(ThreadResource threadResource) {
        this.threadResource = threadResource;
    }

    public void setMaxMessageInQueue(long maxMessageInQueue) {
        this.maxMessageInQueue = maxMessageInQueue;
    }
}
