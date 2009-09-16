package net.ds3783.chatserver.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.ds3783.chatserver.tools.Uuid;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 16:11:20
 */
public abstract class CommonRunnable implements Runnable {
    private Log logger = LogFactory.getLog(CommonRunnable.class);
    private boolean running = false;
    private String uuid= Uuid.newUuid();
    protected long sleeptime=100;

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

    public void setLogger(Log logger) {
        this.logger = logger;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
