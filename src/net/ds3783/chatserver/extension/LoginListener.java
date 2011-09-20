package net.ds3783.chatserver.extension;

import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.Channel;
import net.ds3783.chatserver.delivery.Event;
import net.ds3783.chatserver.delivery.EventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ����11:07
 * To change this template use File | Settings | File Templates.
 */
public class LoginListener extends AbstractDefaultListener implements EventListener {
    private static Log logger = LogFactory.getLog(LoginListener.class);
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        Message reply = event.getMessage().simpleClone();
        //��¼
        reply.getDestUserUids().add(reply.getUserUuid());
        //��¼�ɹ�
        if (clientDao.getClientByName(reply.getContent()) != null) {
            //֪ͨ��������������������
            reply.setDropClientAfterReply(true);
            reply.setType(MessageType.CHAT_MESSAGE);
            reply.setChannel(Channel.SYSTEM.getCode());
            reply.setContent("��ǰ�������û�");
            reply.setAuthCode("false");
            logger.info("��ǰ�������û�:"+reply.getContent());
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            clientDao.updateClientName(reply.getUserUuid(), reply.getContent());
            clientDao.updateClientToken(reply.getUserUuid(), reply.getAuthCode());
            clientDao.updateClientLogined(reply.getUserUuid(), true);
            reply.setAuthCode("true");
            Client client = clientDao.getClient(reply.getUserUuid());
            logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") �ɹ���¼��");
            outputerSwitcher.switchTo(reply);

            //ȫ�ֹ㲥ĳ������
            Message broadCast = reply.simpleClone();
            broadCast.setDestUserUids(new HashSet<String>(clientDao.getLoginClientUids()));
            broadCast.setType(MessageType.CHAT_MESSAGE);
            broadCast.setChannel(Channel.SYSTEM.getCode());
            broadCast.setContent(client.getName() + " �ɹ���¼");
            outputerSwitcher.switchTo(broadCast);
            logger.debug(reply.getContent() + " online");
            return true;
        }

    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void init() {
        messageDispatcher.addListener(MessageType.LOGIN_MESSAGE, this);
    }
}
