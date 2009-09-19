package net.ds3783.chatserver.tools;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.*;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 17:24:24
 */
public class Utils {

    /**
     * 生成新的uuid字符串
     *
     * @return
     */
    public static String newUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String describeBean(Serializable bean) {
//        ToStringBuilder builder=new ToStringBuilder(bean,ToStringBuilder.getDefaultStyle());
        return ToStringBuilder.reflectionToString(bean);
    }

    public static <T extends Serializable> T clone(T bean) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        Object newOb = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(bean);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            newOb = (oi.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) newOb;
    }
}
