package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.delivery.MessageProcessor;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:48
 */
public class ProcessThread extends CommonRunnable implements Runnable, Switchable<Message> {
    private Log logger = LogFactory.getLog(ProcessThread.class);
    private LinkedBlockingQueue<Message> receivedMessages = new LinkedBlockingQueue<Message>();
    private LinkedBlockingQueue<Message> enmergencyMessages = new LinkedBlockingQueue<Message>();

    private MessageProcessor messageProcessor;
    private long maxMessageInQueue = 100;
    private int maxMessagePerTime = 100;

    public void addMessage(Message message) {
        try {
            if ((MessageType.AUTH_MESSAGE.equals(message.getType()) || MessageType.LOGIN_MESSAGE.equals(message.getType()))) {
                enmergencyMessages.put(message);
            } else if (receivedMessages.size() > maxMessageInQueue) {
                //���������Ϣ��������
                logger.fatal("ϵͳ���ش�,��������Ϣ����:" + receivedMessages.size());
                logger.warn("Dropped Message:" + Utils.describeBean(message));
            } else {
                receivedMessages.put(message);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void doRun() throws Exception {
        while (true) {
            boolean nothingtodo = true;

            if (!enmergencyMessages.isEmpty()) nothingtodo = false;
            long now = System.currentTimeMillis();
            while (!enmergencyMessages.isEmpty()) {
                Message msg = enmergencyMessages.poll();
                deliverMessage(msg, now);
            }


            if (!receivedMessages.isEmpty()) nothingtodo = false;
            now = System.currentTimeMillis();
            int counter = 0;
            while (!receivedMessages.isEmpty()) {
                Message msg = receivedMessages.poll();
                deliverMessage(msg, now);
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

    public void deliverMessage(Message msg, long now) {
        if (msg == null) return;
        messageProcessor.processMsg(msg, now);
    }


    public void sendEchoMessage(Client client) {
        Message msg = new Message();
        msg.setUserUuid(client.getUid());
        msg.setType(MessageType.COMMAND_MESSAGE);
        msg.setContent("echo");
        msg.setChannel("ECHO");
        msg.setDestUid(client.getUid());
        this.addMessage(msg);
    }


    public void destroy() throws Exception {

    }

    public void cleanuUp() throws Exception {

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }


    public void setMaxMessageInQueue(long maxMessageInQueue) {
        this.maxMessageInQueue = maxMessageInQueue;
    }

    public void setMaxMessagePerTime(int maxMessagePerTime) {
        this.maxMessagePerTime = maxMessagePerTime;
    }

    /**
     * ȡ��Ȩ��
     * Ȩ�ع�ϵ��ѡ������ѡ����
     *
     * @return Ȩ��
     */
    public int getWeight() {
        return this.receivedMessages.size();
    }

    /**
     * ��������
     *
     * @param data ��������������Ϣ
     */
    public void receive(Message data) {
        this.addMessage(data);
    }
}
