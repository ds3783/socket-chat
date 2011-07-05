package net.ds3783.chatserver.core;

import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:41:02
 */
public class ServerThread extends CommonRunnable implements Runnable {
    private static Log logger = LogFactory.getLog(ServerThread.class);
    private String address;
    private int port;
    private int backlog;
    private ThreadResource threadResource;
    private Selector clientAcceptSelector;
    private ServerSocketChannel serverChannel;
    private SelectionKey serverSocketSelectKey;
    private ClientDao clientDao;

    public void doRun() throws Exception {
        InetAddress addr = null;
        if (StringUtils.isNotEmpty(address)) {
            if ("*".equals(StringUtils.trim(address))) {
                addr = null;
            } else {
                int add = -1;
                try {
                    add = Integer.parseInt(StringUtils.trim(address));
                } catch (NumberFormatException e) {
                    //�����쳣
                }
                if (add == 0) {
                    addr = null;
                } else {
                    addr = InetAddress.getByName(StringUtils.trim(address));
                }
            }

        }
        if (backlog == 0) {
            backlog = 50;
        }
        //��������
        SelectorProvider provider = SelectorProvider.provider();
        serverChannel = provider.openServerSocketChannel();
        clientAcceptSelector = provider.openSelector();
        serverChannel.socket().bind(new InetSocketAddress(addr, port));
        serverChannel.configureBlocking(false);
        serverSocketSelectKey = serverChannel.register(clientAcceptSelector, SelectionKey.OP_ACCEPT);

        //��������
        while (true) {
            try {
                clientAcceptSelector.select();
                for (SelectionKey key : clientAcceptSelector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel socketserver = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = socketserver.accept();
                        socketChannel.configureBlocking(false);


                        String uuid = Utils.newUuid();
                        //ȡ���߳���Դ
                        //��ȡ�߳�
                        List<CommonRunnable> inputThreads = threadResource.getThreads(ThreadResourceType.INPUT_THREAD);
                        SlaveThread readSlave = null;
                        //�����ҵ��ͻ������ٵ��̣߳�Ĭ��ԭ�򣺿ͻ�������=ѹ����С=�����ٶ���죩
                        for (CommonRunnable inputThread : inputThreads) {
                            if (inputThread != null && inputThread instanceof SlaveThread) {
                                SlaveThread slave = (SlaveThread) inputThread;
                                if (readSlave != null) {
                                    if (readSlave.getClients() > slave.getClients()) {
                                        readSlave = slave;
                                    }
                                } else {
                                    readSlave = slave;
                                }
                            }
                        }
                        //д���߳�
                        List<CommonRunnable> outputThreads = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
                        SlaveThread writeSlave = null;
                        //�����ҵ��ͻ������ٵ��̣߳�Ĭ��ԭ�򣺿ͻ�������=ѹ����С=�����ٶ���죩
                        for (CommonRunnable outputThread : outputThreads) {
                            if (outputThread != null && outputThread instanceof SlaveThread) {
                                SlaveThread slave = (SlaveThread) outputThread;
                                if (writeSlave != null) {
                                    if (writeSlave.getClients() > slave.getClients()) {
                                        writeSlave = slave;
                                    }
                                } else {
                                    writeSlave = slave;
                                }
                            }
                        }

                        //�����ȷ���д���̺߳�����ȡ�߳�
                        writeSlave.assign(socketChannel, uuid);

                        readSlave.assign(socketChannel, uuid);

                        Client client = new Client();
                        client.setUid(uuid);
                        client.setIp(socketChannel.socket().getInetAddress().getHostAddress());
                        client.setPort(socketChannel.socket().getPort());
                        client.setLastMessageTime(System.currentTimeMillis());
                        client.setConnectTime(System.currentTimeMillis());
                        client.setReadThread(readSlave.getUuid());
                        client.setWriteThread(writeSlave.getUuid());
                        client.setAuthed(false);
                        client.setLogined(false);
                        client.setName(client.getIp() + ":" + client.getPort());
                        logger.debug(client.getName() + " connected");
                        //����ͻ��˹�����
                        clientDao.addClient(client);
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                if (!serverChannel.isOpen()) {
                    clientAcceptSelector.close();
                    serverChannel.close();
                    break;
                }
            }

        }

    }

    public void destroy() throws Exception {
        if (clientAcceptSelector.isOpen()) {
            clientAcceptSelector.close();
        }
        if (serverChannel.isOpen()) {
            serverChannel.close();
        }
        if (serverSocketSelectKey.isValid()) {
            serverSocketSelectKey.cancel();
        }

    }

    public void cleanuUp() throws Exception {
        this.destroy();
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public void setThreadResource(ThreadResource threadResource) {
        this.threadResource = threadResource;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
