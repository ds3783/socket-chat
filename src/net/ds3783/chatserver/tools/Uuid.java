package net.ds3783.chatserver.tools;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 17:24:24
 */
public class Uuid {

    /**
     * 生成新的uuid字符串
     * @return
     */
    public static String newUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
