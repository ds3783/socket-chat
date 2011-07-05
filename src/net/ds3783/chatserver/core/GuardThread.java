package net.ds3783.chatserver.core;

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
    private long lastGcTime = 0;
    private ClientDao clientDao;
    private ProcessThread processThread;


    public void doRun() throws Exception {
        try {
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
            Collection<Client> allClients = clientDao.getAllClients();
            long now = System.currentTimeMillis();
            for (Client client : allClients) {
                if (client == null) continue;
                long minus = now - client.getLastMessageTime();
                if (!client.isLogined()) {
                    if (minus > unLoginExpireTime && unLoginExpireTime > 0) {
                        processThread.addOfflineUser(client);
                    }
                } else {
                    if (minus > loginExpireTime && loginExpireTime > 0) {
                        processThread.sendEchoMessage(client);
                    }
                }
            }

            //GC处理
            now = System.currentTimeMillis();
            if (lastGcTime == 0) {
                lastGcTime = now;
            } else {
                if (gcTimeCycle > 0 && now - lastGcTime > gcTimeCycle) {
                    lastGcTime = now;
                    try {
                        /*
                        System.out.println("before gc: total:" + Runtime.getRuntime().totalMemory() / 1024 / 1024
                                + "free:" + Runtime.getRuntime().freeMemory() / 1024 / 1024
                                + "max:" + Runtime.getRuntime().maxMemory() / 1024 / 1024
                                + "used:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
                        */
                        System.gc();
                        /*
                        System.out.println("after gc: total:" + Runtime.getRuntime().totalMemory() / 1024 / 1024
                                + "free:" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "max:" + Runtime.getRuntime().maxMemory() / 1024 / 1024
                                + "used:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
                        */

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

    public void setProcessThread(ProcessThread processThread) {
        this.processThread = processThread;
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
}
