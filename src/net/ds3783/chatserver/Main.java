package net.ds3783.chatserver;

import net.ds3783.chatserver.core.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: Pihy
 * Date: 2009-7-10
 * Time: 17:53:40
 */
public class Main {
    private static ApplicationContext context = null;


    public static void main(String[] args) throws Exception {
        Main instance = new Main();
        //������Դ
        instance.initialize();
        //��ȡ����
        Configuration config = (Configuration) context.getBean("config");
        //�߳���Դ����
        ThreadResource threadResource = (ThreadResource) context.getBean("threadResource");
        //��ʼ����ȡ�߳�
        for (int i = 0; i < config.getReadThread(); i++) {
            InputThread inputThread = (InputThread) context.getBean("inputThread");
            threadResource.register(inputThread.getUuid(), ThreadResourceType.INPUT_THREAD, inputThread);
            Thread t = new Thread(inputThread);
            t.start();
        }
        //��ʼ��д���߳�
        for (int i = 0; i < config.getWriteThread(); i++) {
            OutputThread outputThread = (OutputThread) context.getBean("outputThread");
            threadResource.register(outputThread.getUuid(), ThreadResourceType.OUTPUT_THREAD, outputThread);
            Thread t = new Thread(outputThread);
            t.start();
        }
        //��ʼ��д���߳�
        //������������
        ServerThread server = (ServerThread) context.getBean("serverThread");
        threadResource.register(server.getUuid(), ThreadResourceType.SERVER_THREAD, server);
        Thread t = new Thread(server);
        t.start();
    }

    private void initialize() {
        context = new ClassPathXmlApplicationContext("core-beans.xml");

    }
}
