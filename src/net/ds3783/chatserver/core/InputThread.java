package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.pool.BytePool;
import net.ds3783.chatserver.protocol.InputProtocal;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:02
 */
public class InputThread extends SlaveThread implements Runnable {

    private Log logger = LogFactory.getLog(InputThread.class);
    protected List<InputFilter> filters = new ArrayList<InputFilter>();
    private ClientDao clientDao;
    private BytePool pool;
    private InputProtocal protocal;
    private Switcher<Message> processThreadSwitcher;

    public void assign(SocketChannel client, String uuid) throws ClosedChannelException {
        SelectionKey key = client.register(channelSelector, SelectionKey.OP_READ);
        userKeys.put(uuid, key);
        keyUsers.put(key, uuid);
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
            //从网络流中读取数据
            long now = System.currentTimeMillis();
            for (Object o : channelSelector.selectedKeys()) {
                SelectionKey key = (SelectionKey) o;
                SocketChannel channel = (SocketChannel) key.channel();
                String uuid = keyUsers.get(key);
                Client client = clientDao.getClient(uuid);
                ByteBuffer readBuffer = ByteBuffer.allocate(2048);
                if (uuid == null || !key.isValid()) continue;
                if (key.isReadable()) {
                    try {
                        int bytecount;
                        do {
                            readBuffer.reset();
                            bytecount = channel.read(readBuffer);
                            if (bytecount > 0) {
                                byte[] bytes = new byte[bytecount];
                                readBuffer.get(bytes, 0, bytecount);
                                pool.offerBytes(client.getUid(), bytes);
                            }
                        } while (bytecount > 0);
                        if (pool.getCachedSize(client.getUid()) > 0) {
                            //使用协议解码
                            protocal.reset();
                            byte[] data = pool.getBytes(client.getUid());
                            protocal.setData(data);
                            protocal.unmarshal();
                            List<Message> messages = protocal.getMessages();
                            pool.offerBytes(client.getUid(), protocal.getRemains());
                            //调用过滤器
                            for (Message message : messages) {
                                if (filters != null) {
                                    for (InputFilter filter : filters) {
                                        filter.filte(client, message);
                                        logger.debug("Revceived Message:" + Utils.describeBean(message));
                                    }
                                }
                            }
                            processThreadSwitcher.switchData(messages);
                            client.setLastMessageTime(now);
                        }
                    } catch (IOException e) {
                        if (client != null) {
                            logger.warn(client.getName() + ":" + e.getMessage());

                            //用户已断线，清除该用户
                            this.remove(client.getUid());
                            //TODO::processThread.addOfflineUser(client);
                        }
                    }
                    readBuffer.clear();
                }
            }

            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void destroy() throws Exception {
        userKeys.clear();
        channelSelector.close();
    }

    public void cleanuUp() throws Exception {
        if (channelSelector.selectedKeys() != null) {
            Iterator<SelectionKey> iterator = channelSelector.selectedKeys().iterator();
            if (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (keyUsers.get(key) != null) {
                    Client client = clientDao.getClient(keyUsers.get(key));
                    if (client != null) {
                        logger.warn("忽略" + client.getName() + "的一条消息，消息内容未知！");
                    }
                }
                iterator.remove();
            }
        }

    }

    public void initialize() throws IOException {
        SelectorProvider provider = SelectorProvider.provider();
        channelSelector = provider.openSelector();
    }

    public void setFilters(List<InputFilter> filters) {
        this.filters = filters;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setPool(BytePool pool) {
        this.pool = pool;
    }

    public void setProtocal(InputProtocal protocal) {
        this.protocal = protocal;
    }

    public void setProcessThreadSwitcher(Switcher<Message> processThreadSwitcher) {
        this.processThreadSwitcher = processThreadSwitcher;
    }
}
