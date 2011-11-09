package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 16:11:20
 */
public abstract class CommonRunnable implements Runnable {
    private Log logger = LogFactory.getLog(CommonRunnable.class);
    private boolean running = false;
    private String uuid = Utils.newUuid();
    protected long sleeptime = 100;
    private Thread wrapThread;

    public void run() {
        running = true;
        try {
            doRun();
        } catch (Throwable e) {
            logger.fatal(e.getMessage(), e);
        } finally {
            try {
                destroy();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
        running = false;

    }

    public abstract void doRun() throws Exception;

    public abstract void destroy() throws Exception;

    public boolean isRunning() {
        return running;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Thread getWrapThread() {
        return wrapThread;
    }

    public void setWrapThread(Thread wrapThread) {
        this.wrapThread = wrapThread;
    }

    public void setSleeptime(long sleeptime) {
        this.sleeptime = sleeptime;
    }

    public abstract void cleanuUp() throws Exception;
}
