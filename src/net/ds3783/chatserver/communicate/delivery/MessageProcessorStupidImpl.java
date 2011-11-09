package net.ds3783.chatserver.communicate.delivery;

import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 17:48:52
 */
@Deprecated
public class MessageProcessorStupidImpl implements MessageProcessor {
    private ClientDao clientDao;
    private Configuration config;
    private String publicKey;
    private OutputerSwitcher outputerSwitcher;
    private Log logger = LogFactory.getLog(MessageProcessorStupidImpl.class);

    public void processMsg(Message msg, long now) {
        HashSet<String> destUsers = new HashSet<String>();
        List<Message> result = new ArrayList<Message>();
        Client client = clientDao.getClient(msg.getUserUuid());

        //授权
        if (MessageType.AUTH_MESSAGE.equals(msg.getType())) {
            msg.setContent("<cross-domain-policy><allow-access-from domain=\"" + config.getAddress() + "\" to-ports=\"" + config.getPort() + "\" /></cross-domain-policy>");
            client.setAuthed(true);
            destUsers.add(client.getUid());
            msg.setDestUserUids(destUsers);
            result.add(msg);
        } else if (MessageType.LOGIN_MESSAGE.equals(msg.getType())) {
            //登录
            destUsers.add(client.getUid());
            //登录成功
            if (clientDao.getClientByName(msg.getChannel()) != null) {
                //通知此人有重名，并踢下线
                msg.setDropClientAfterReply(true);
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setChannel("SYSTEM");
                msg.setContent("当前有重名用户");
                destUsers.add(client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
            } else {
                clientDao.updateClientName(msg.getUserUuid(), msg.getContent());
                clientDao.updateClientToken(msg.getUserUuid(), msg.getAuthCode());
                clientDao.updateClientLogined(msg.getUserUuid(), true);
                logger.info(client.getIp() + ":" + client.getPort() + "(" + client.getName() + ") 成功登录。");
                msg.setDestUserUids(destUsers);
                result.add(msg);
                //全局广播某人上线
                Message broadCast = msg.simpleClone();
                broadCast.setDestUserUids(new HashSet<String>(clientDao.getLoginClientUids()));
                broadCast.setType(MessageType.CHAT_MESSAGE);
                broadCast.setChannel("SYSTEM");
                broadCast.setContent(client.getName() + " 成功登录");
                result.add(broadCast);
                logger.debug(msg.getContent() + " online");
            }
        } else if (MessageType.CHAT_MESSAGE.equals(msg.getType())) {
            if (clientDao.isInBlackList(client.getName(), now)) {
                //黑名单处理
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setChannel("SYSTEM");
                msg.setContent("用户已被禁言");
                destUsers.add(client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
            } else {
                if ("BROADCAST".equals(msg.getChannel())) {
                    if (publicKey.equals(msg.getAuthCode())) {
                        destUsers = new HashSet<String>(clientDao.getLoginClientUids());
                        msg.setDestUserUids(destUsers);
                        result.add(msg);
                    }
                    //processThread.addOfflineUser(client);
                }
            }
        } else if (MessageType.COMMAND_MESSAGE.equals(msg.getType())) {
            if (clientDao.isInBlackList(client.getName(), now)) {
                //黑名单处理
                msg.setType(MessageType.CHAT_MESSAGE);
                msg.setChannel("SYSTEM");
                msg.setContent("用户已被禁言");
                destUsers.add(client.getUid());
                msg.setDestUserUids(destUsers);
                result.add(msg);
            } else {
                if ("CHANGENAME".equals(msg.getChannel())) {
                    if (clientDao.getClientByName(msg.getChannel()) != null) {
                        //通知重名，并提示更名失败
                        msg.setType(MessageType.CHAT_MESSAGE);
                        msg.setChannel("SYSTEM");
                        msg.setContent("已有重名用户更名失败");
                        destUsers.add(client.getUid());
                        msg.setDestUserUids(destUsers);
                        result.add(msg);
                    } else {
                        String oldName = client.getName();
                        clientDao.updateClientName(msg.getUserUuid(), msg.getContent());
                        msg.setType(MessageType.CHAT_MESSAGE);
                        msg.setChannel("SYSTEM");
                        msg.setContent("登录名变更成功");
                        destUsers.add(client.getUid());
                        msg.setDestUserUids(destUsers);
                        result.add(msg);
                        //全局广播某人更名
                        Message broadCast = msg.simpleClone();
                        broadCast.setDestUserUids(new HashSet<String>(clientDao.getLoginClientUids()));
                        broadCast.setType(MessageType.CHAT_MESSAGE);
                        broadCast.setChannel("SYSTEM");
                        broadCast.setContent(oldName + " 已经使用新名字 " + msg.getContent());
                        result.add(broadCast);
                    }
                }
            }
        }

        for (Message message : result) {
            outputerSwitcher.switchTo(message);
        }
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

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
