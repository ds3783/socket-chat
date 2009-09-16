package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Message;
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
import java.util.Vector;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 18:41:10
 */
public class OutputThread extends SlaveThread implements Runnable {
    private Log logger = LogFactory.getLog(InputThread.class);
    private List<Message> toSendMessages = new Vector<Message>();
    protected List<OutputFilter> filters = new ArrayList<OutputFilter>();

    public void assign(SocketChannel client, String uuid) throws ClosedChannelException {
        SelectionKey key = client.register(channelSelector, SelectionKey.OP_WRITE);
        userKeys.put(uuid, key);
        keyUsers.put(key, uuid);
    }

    public void send(Message message) {
        if (message != null) {
            toSendMessages.add(message);
        }
    }


    public void doRun() throws Exception {
        while (true) {
            try {
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
            for (Iterator<Message> iterator = toSendMessages.iterator(); iterator.hasNext();) {
                Message message = iterator.next();
                String data = "";
                if (filters != null) {
                    for (OutputFilter filter : filters) {
                        data = filter.marshal(message, data);
                        data = filter.filte(data);
                    }
                }
                SocketChannel channel = (SocketChannel) userKeys.get(message.getUserUuid()).channel();
                ByteBuffer writeBuffer = ByteBuffer.wrap(data.getBytes(charset));
                try {
                    channel.write(writeBuffer);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    //TODO::用户已断线，清除该用户
                }
                iterator.remove();
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

    public void initialize() throws IOException {
        SelectorProvider provider = SelectorProvider.provider();
        channelSelector = provider.openSelector();
    }

}
