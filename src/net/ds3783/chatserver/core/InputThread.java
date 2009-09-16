package net.ds3783.chatserver.core;

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

import net.ds3783.chatserver.Message;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:02
 */
public class InputThread extends SlaveThread implements Runnable {

    private String ignoreBytes = "\0";
    private Log logger = LogFactory.getLog(InputThread.class);
    protected List<InputFilter> filters = new ArrayList<InputFilter>();

    public void assign(SocketChannel client, String uuid) throws ClosedChannelException {
        SelectionKey key = client.register(channelSelector, SelectionKey.OP_READ);
        userKeys.put(uuid, key);
        keyUsers.put(key, uuid);
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
            //从网络流中读取数据
            ByteBuffer readBuffer = null;
            byte[] ignoreBytes = this.ignoreBytes.getBytes(charset);
            for (Iterator it = channelSelector.selectedKeys().iterator(); it.hasNext();) {
                SelectionKey key = (SelectionKey) it.next();
                SocketChannel channel = (SocketChannel) key.channel();
                if (key.isReadable()) {
                    if (readBuffer == null) {
                        readBuffer = ByteBuffer.allocate(2048);
                    }
                    try {
                        byte[] messageBuffer = new byte[0];
                        int bytecount;
                        do {
                            readBuffer.clear();
                            bytecount = channel.read(readBuffer);
                            if (bytecount > 0) {
                                readBuffer.rewind();
                                byte[] buf = new byte[bytecount];
                                int counter = 0;
                                for (int i = 0; i < bytecount; i++) {
                                    byte b = readBuffer.get();
                                    boolean ignore = false;
                                    for (byte ignoreByte : ignoreBytes) {
                                        if (ignoreByte == b) {
                                            ignore = true;
                                            break;
                                        }
                                    }
                                    if (!ignore) {
                                        buf[counter++] = b;
                                    }
                                }
                                byte[] newbuf = new byte[messageBuffer.length + counter];
                                System.arraycopy(messageBuffer, 0, newbuf, 0, messageBuffer.length);
                                System.arraycopy(buf, 0, newbuf, messageBuffer.length, counter);
                                messageBuffer = newbuf;
                            }
                        } while (bytecount > 0);
                        if (messageBuffer.length > 0) {
                            String message = new String(messageBuffer, charset);
                            //调用过滤器
                            Message msg=new Message();
                            if (filters!=null){
                                for (InputFilter filter : filters) {
                                    msg=filter.unmarshal(message,msg);
                                    msg=filter.filte(msg);
                                }
                            }
                            //TODO:发送消息
                            System.out.println("收到消息:" + message);
                        }
                    } catch (IOException e) {

                        logger.error(e.getMessage(), e);
                        //TODO::用户已断线，清除该用户
                    }
                    readBuffer.clear();
                }
                it.remove();
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

    public void setIgnoreBytes(String ignoreBytes) {
        this.ignoreBytes = ignoreBytes;
    }

    public void setFilters(List<InputFilter> filters) {
        this.filters = filters;
    }
}
