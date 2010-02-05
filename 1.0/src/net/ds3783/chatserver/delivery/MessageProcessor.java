package net.ds3783.chatserver.delivery;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.core.ProcessThread;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 17:48:52
 */
public class MessageProcessor {
    private ClientDao clientDao;
    private Configuration config;
    private String publicKey;
    private ProcessThread processThread;
    private Log logger = LogFactory.getLog(ClientDao.class);

    public List<Message> processMsg(Message msg, long now) {
        HashMap<String, String> destUsers = new HashMap<String, String>();
        List<Message> result = new ArrayList<Message>();
        Client client = clientDao.getClient(msg.getUserUuid());

        //��Ȩ
        if (MessageType.AUTH_MESSAGE.equals(msg.getType())) {
            msg.setContent("<cross-domain-policy><allow-access-from domain=\"" + config.getAddress() + "\" to-ports=\"" + config.getPort() + "\" /></cross-domain-policy>");
            client.setAuthed(true);
            destUsers.put(client.getUid(), client.getUid());
            msg.setDestUserUids(destUsers);
            result.add(msg);
            return result;
        } else if (MessageType.LOGIN_MESSAGE.equals(msg.getType())) {
            //��¼
            destUsers.put(client.getUid(), client.getUid());
            msg.setContent("true");
            //��¼�ɹ�
            if (clientDao.getClientByName(msg.getSubType()) != null) {
                //֪ͨ��������������������
                msg.setDropClientAfterReply(true);
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setSubType("SYSTEM");
                msg.setContent("��ǰ�������û�");
                destUsers.put(client.getName(), client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
                return result;
            }
            clientDao.updateClientName(msg.getUserUuid(), msg.getSubType());
            clientDao.updateClientToken(msg.getUserUuid(), msg.getAuthCode());
            clientDao.updateClientLogined(msg.getUserUuid(), true);
            logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") �ɹ���¼��");
            msg.setDestUserUids(destUsers);
            result.add(msg);
            //ȫ�ֹ㲥ĳ������
            Message broadCast = msg.simpleClone();
            broadCast.setDestUserUids(new HashMap<String, String>(clientDao.getLoginClientUids()));
            broadCast.setType(MessageType.CHAT_MESSAGE);
            broadCast.setSubType("SYSTEM");
            broadCast.setContent(client.getName() + " �ɹ���¼");
            result.add(broadCast);
            return result;
        } else if (MessageType.CHAT_MESSAGE.equals(msg.getType())) {
            if (clientDao.isInBlackList(client.getName(), config.getBlackListKeepTime(), now)) {
                //����������
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setSubType("SYSTEM");
                msg.setContent("�û��ѱ�����");
                destUsers.put(client.getName(), client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
                return result;
            }
            if ("BROADCAST".equals(msg.getSubType())) {
                if (publicKey.equals(msg.getAuthCode())) {
                    destUsers = new HashMap<String, String>(clientDao.getLoginClientUids());
                    msg.setDestUserUids(destUsers);
                    result.add(msg);
                }
                processThread.addOfflineUser(client);
            }
        } else if (MessageType.COMMAND_MESSAGE.equals(msg.getType())) {
            if (clientDao.isInBlackList(client.getName(), config.getBlackListKeepTime(), now)) {
                //����������
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setSubType("SYSTEM");
                msg.setContent("�û��ѱ�����");
                destUsers.put(client.getName(), client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
                return result;
            }
            if ("CHANGENAME".equals(msg.getSubType())) {
                if (clientDao.getClientByName(msg.getSubType()) != null) {
                    //֪ͨ����������ʾ����ʧ��
                    msg.setType(MessageType.CHAT_MESSAGE);
                    msg.setSubType("SYSTEM");
                    msg.setContent("���������û�����ʧ��");
                    destUsers.put(client.getName(), client.getUid());
                    msg.setDestUserUids(destUsers);
                    result.add(msg);
                    return result;
                } else {
                    String oldName = client.getName();
                    clientDao.updateClientName(msg.getUserUuid(), msg.getContent());
                    msg.setType(MessageType.CHAT_MESSAGE);
                    msg.setSubType("SYSTEM");
                    msg.setContent("��¼������ɹ�");
                    destUsers.put(client.getName(), client.getUid());
                    msg.setDestUserUids(destUsers);
                    result.add(msg);
                    //ȫ�ֹ㲥ĳ�˸���
                    Message broadCast = msg.simpleClone();
                    broadCast.setDestUserUids(new HashMap<String, String>(clientDao.getLoginClientUids()));
                    broadCast.setType(MessageType.CHAT_MESSAGE);
                    broadCast.setSubType("SYSTEM");
                    broadCast.setContent(oldName + " �Ѿ�ʹ�������� " + msg.getContent());
                    result.add(broadCast);
                    return result;
                }
            }
        }

        return result;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setProcessThread(ProcessThread processThread) {
        this.processThread = processThread;
    }
}
