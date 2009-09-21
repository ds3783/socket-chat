package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.MessageProcessor;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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
    private LinkedBlockingQueue<Message> toSendMessages = new LinkedBlockingQueue<Message>();
    private Hashtable<String, Client> toKickClient = new Hashtable<String, Client>();


    private MessageProcessor messageProcessor;
    private ClientDao clientDao;
    private ThreadResource threadResource;
    private long maxMessageInQueue=100;

    public void addMessage(Message message) {
        try {
            if (receivedMessages.size() > maxMessageInQueue) {
                //TODO:超出最大消息数量限制
                logger.fatal("系统负载大,待处理消息数量:" + receivedMessages.size() + " 待发送消息数量:" + toSendMessages.size());
            }else{
                receivedMessages.put(message);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void doRun() throws Exception {
        while (true) {
            boolean nothingtodo = receivedMessages.isEmpty();
            while (!receivedMessages.isEmpty()) {
                Message msg = receivedMessages.poll();
                if (msg == null) break;
                List<Message> toDeliver = messageProcessor.processMsg(msg);
                if (toDeliver != null) {
                    for (Message message : toDeliver) {
                        toSendMessages.put(message);
                    }
                }
            }

            HashMap<String, Client> kickedClent = new HashMap<String, Client>();
            if (!toKickClient.isEmpty()) nothingtodo = false;
            for (String uuid : toKickClient.keySet()) {
                Client client = toKickClient.get(uuid);
                kickedClent.put(client.getName(), client);

                //清除服务线程资源
                clientDao.removeClient(client.getUid());
                SlaveThread writeThread = (SlaveThread) threadResource.getThread(client.getWriteThread());
                SlaveThread readThread = (SlaveThread) threadResource.getThread(client.getReadThread());
                try {
                    readThread.remove(client.getUid());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                try {
                    writeThread.remove(client.getUid());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            toKickClient.clear();

            if (!toSendMessages.isEmpty()) nothingtodo = false;
            int messageSendCounter = 0;
            while (!toSendMessages.isEmpty()) {
                Message toSendMessage = toSendMessages.poll();

                for (String destUserName : toSendMessage.getDestUserName()) {
                    if (kickedClent.containsKey(destUserName)) continue;
                    Client client = clientDao.getClientByName(destUserName);
                    if (client != null) {
                        OutputThread writethread = (OutputThread) threadResource.getThread(client.getWriteThread());
                        toSendMessage = Utils.clone(toSendMessage);
                        toSendMessage.setDestName(client.getName());
                        writethread.send(toSendMessage);
                    }
                    messageSendCounter++;
                }
                if (messageSendCounter > 500) {
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

    public void addOfflineUser(Client client) {
        toKickClient.put(client.getUid(), client);
    }

    public void destroy() throws Exception {

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
