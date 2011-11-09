package net.ds3783.chatserver;

import net.ds3783.chatserver.communicate.core.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pihy
 * Date: 2009-7-10
 * Time: 17:53:40
 */
public class Main {
    private static ApplicationContext context = null;
    private static Thread mainThread = null;
    private static Log logger = LogFactory.getLog(Main.class);


    public static void main(String[] args) throws Exception {
        logger.info("Server Starting");
        mainThread = Thread.currentThread();
        Main instance = new Main();
        //������Դ
        instance.initialize();
        //��ȡ����
        Configuration config = (Configuration) context.getBean("config");
        //�߳���Դ����
        ThreadResource threadResource = (ThreadResource) context.getBean("threadResource");

        //��ʼ�������߳�
        List<ProcessThread> processThreads = new ArrayList<ProcessThread>();
        for (int i = 0; i < config.getReadThread(); i++) {
            ProcessThread processThread = (ProcessThread) context.getBean("processThread");
            threadResource.register(processThread.getUuid(), ThreadResourceType.PROCESS_THREAD, processThread);
            Thread process = new Thread(processThread);
            processThread.setWrapThread(process);
            process.start();
            processThreads.add(processThread);
        }

        //��ʼ����Ϣ�����ؾ���
        LoadBalanceSwitcher processThreadSwitcher = (LoadBalanceSwitcher) context.getBean("processThreadSwitcher");
        processThreadSwitcher.setTargets(processThreads);
        //��ʼ����ȡ�߳�
        for (int i = 0; i < config.getReadThread(); i++) {
            InputThread inputThread = (InputThread) context.getBean("inputThread");
            threadResource.register(inputThread.getUuid(), ThreadResourceType.INPUT_THREAD, inputThread);
            Thread t = new Thread(inputThread);
            inputThread.setWrapThread(t);
            t.start();
        }
        //��ʼ��д���߳�
        for (int i = 0; i < config.getWriteThread(); i++) {
            OutputThread outputThread = (OutputThread) context.getBean("outputThread");
            threadResource.register(outputThread.getUuid(), ThreadResourceType.OUTPUT_THREAD, outputThread);
            Thread t = new Thread(outputThread);
            outputThread.setWrapThread(t);
            t.start();
        }


        //������������
        ServerThread server = (ServerThread) context.getBean("serverThread");
        server.setAddress(config.getAddress());
        server.setPort(config.getPort());
        threadResource.register(server.getUuid(), ThreadResourceType.SERVER_THREAD, server);
        Thread t = new Thread(server);
        server.setWrapThread(t);
        t.start();

        //�����ػ��߳�
        GuardThread guardThread = (GuardThread) context.getBean("guardThread");
        threadResource.register(guardThread.getUuid(), ThreadResourceType.GUARD_THREAD, guardThread);
        t = new Thread(guardThread);
        guardThread.setWrapThread(t);
        t.start();
        logger.info("Server Started");
    }

    public static void shutdown() {
        if (context == null) {
            try {
                mainThread.interrupt();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return;
        }
        ThreadResource threadResource = (ThreadResource) context.getBean("threadResource");
        if (threadResource == null) {
            try {
                mainThread.interrupt();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return;
        }
        Collection<CommonRunnable> cr = threadResource.getAllThreads();
        if (cr != null) {
            for (CommonRunnable commonRunnable : cr) {
                Thread t = commonRunnable.getWrapThread();
                if (t != null && t.isAlive()) {
                    try {
                        t.interrupt();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        try {
            mainThread.interrupt();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initialize() {
        context = new ClassPathXmlApplicationContext("core-beans.xml");
    }
}
