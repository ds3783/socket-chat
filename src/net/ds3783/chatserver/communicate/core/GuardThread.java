package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:21:16
 */
public class GuardThread extends CommonRunnable {
    private ThreadResource threadResource;
    private Log logger = LogFactory.getLog(GuardThread.class);
    private long unLoginExpireTime = 60000;
    private long loginExpireTime = 15 * 60000;
    private long gcTimeCycle = 30 * 60000;
    private long cleanNotLoginClientCycle = 60000;
    private long cleanExpireClientCycle = 10 * 60000;
    private long lastGcTime = 0;
    private long lastCleanExpireClientTime = 0;
    private long lastCleanUnloginClientTime = 0;
    private long contextHelperCleanCycle = 30000;
    private long lastContextHelperCleanTime = 0;
    private ClientDao clientDao;

    private ContextHelper contextHelper;
    private ClientService clientService;


    public void doRun() throws Exception {
        try {
            //在系统最初启动的10秒内守护线程不工作
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        while (true) {
            //线程守护
            /*Collection<CommonRunnable> servers = threadResource.getThreads(ThreadResourceType.SERVER_THREAD);
            for (CommonRunnable server : servers) {
                try {
                    checkServer(server);
                } catch (ThreadStartException e) {
                    logger.fatal(e.getMessage(), e);
                }
            }*/
            Collection<CommonRunnable> readers = threadResource.getThreads(ThreadResourceType.INPUT_THREAD);
            for (CommonRunnable server : readers) {
                try {
                    checkServer(server);
                } catch (ThreadStartException e) {
                    logger.fatal(e.getMessage(), e);
                }
            }
            Collection<CommonRunnable> writers = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
            for (CommonRunnable server : writers) {
                try {
                    checkServer(server);
                } catch (ThreadStartException e) {
                    logger.fatal(e.getMessage(), e);
                }
            }
            Collection<CommonRunnable> processers = threadResource.getThreads(ThreadResourceType.PROCESS_THREAD);
            for (CommonRunnable server : processers) {
                try {
                    checkServer(server);
                } catch (ThreadStartException e) {
                    logger.fatal(e.getMessage(), e);
                }
            }

            //处理过期用户

            long now = System.currentTimeMillis();
            if (now - lastCleanUnloginClientTime > cleanNotLoginClientCycle) {
                Collection<Client> notLoginedClients = clientDao.getNotLoginedClients();
                for (Client client : notLoginedClients) {
                    if (client == null) continue;
                    long minus = now - client.getConnectTime();
                    if (!client.isLogined()) {
                        if (minus > unLoginExpireTime) {
                            clientService.clientOffline(client);
                        }
                    }
                }
                lastCleanUnloginClientTime = now;
            }
            if (now - lastCleanExpireClientTime > cleanExpireClientCycle) {
                Collection<Client> allClients = clientDao.getAllClients();
                for (Client client : allClients) {
                    if (client == null) continue;
                    //查询该客户端最后一次向服务器放送数据的时间与当前时间之差
                    long minus = now - client.getLastMessageTime();
                    if (minus > loginExpireTime && loginExpireTime > 0) {
                        clientService.clientOffline(client);
                    }
                }
                lastCleanExpireClientTime = now;
            }
            if (now - lastContextHelperCleanTime > contextHelperCleanCycle) {
                contextHelper.clean();
                lastContextHelperCleanTime = now;
            }

            //GC处理
            now = System.currentTimeMillis();
            if (lastGcTime == 0) {
                lastGcTime = now;
            } else {
                if (gcTimeCycle > 0 && now - lastGcTime > gcTimeCycle) {
                    lastGcTime = now;
                    try {
                        logger.info("before gc: total:" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M "
                                + "free:" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M "
                                + "max:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M "
                                + "used:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M ");
                        System.gc();
                        logger.info("after gc: total:" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M "
                                + "free:" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M "
                                + "max:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M "
                                + "used:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M ");

                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void checkServer(CommonRunnable server) throws ThreadStartException {
        if (server.isRunning()) {
            return;
        }

        if (server.getWrapThread().isAlive()) {
            Thread t = server.getWrapThread();
            t.interrupt();
        }

        //Server is down,restart it
        try {
            server.cleanuUp();
        } catch (Exception e) {
            logger.fatal(e.getMessage(), e);
            throw new ThreadStartException(e.getMessage(), e);
        }
        server.setWrapThread(null);
        Thread newThread = new Thread(server);
        server.setWrapThread(newThread);
        newThread.start();

    }

    public void destroy() throws Exception {

    }

    public void cleanuUp() {

    }

    public void setThreadResource(ThreadResource threadResource) {
        this.threadResource = threadResource;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }


    public void setUnLoginExpireTime(long unLoginExpireTime) {
        this.unLoginExpireTime = unLoginExpireTime;
    }

    public void setLoginExpireTime(long loginExpireTime) {
        this.loginExpireTime = loginExpireTime;
    }

    public void setGcTimeCycle(long gcTimeCycle) {
        this.gcTimeCycle = gcTimeCycle;
    }

    public void setCleanExpireClientCycle(long cleanExpireClientCycle) {
        this.cleanExpireClientCycle = cleanExpireClientCycle;
    }

    public void setCleanNotLoginClientCycle(long cleanNotLoginClientCycle) {
        this.cleanNotLoginClientCycle = cleanNotLoginClientCycle;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setContextHelperCleanCycle(long contextHelperCleanCycle) {
        this.contextHelperCleanCycle = contextHelperCleanCycle;
    }

    public void setLastContextHelperCleanTime(long lastContextHelperCleanTime) {
        this.lastContextHelperCleanTime = lastContextHelperCleanTime;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }
}
