package net.ds3783.chatserver.tools;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     */
    public static String newUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String describeBean(Serializable bean) {
//        ToStringBuilder builder=new ToStringBuilder(bean,ToStringBuilder.getDefaultStyle());
        return ToStringBuilder.reflectionToString(bean, ToStringStyle.MULTI_LINE_STYLE);
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

    public static String md5(String value, String charSet) {
        if (value == null) return null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            digest.update(value.getBytes(charSet));
            return toHexString(digest.digest()).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            String bStr = Integer.toHexString(b < 0 ? b + 256 : b);
            if (bStr.length() == 1) {
                bStr = "0" + bStr;
            }
            buffer.append(bStr);
        }
        return buffer.toString();

    }

    public static String escape(String s) {
        StringBuffer sb = new StringBuffer();
        for (char c : s.toCharArray()) {
            if (c > 128) {
                sb.append('\\').append("u").append(fillZero(Integer.toHexString(c), 4));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String fillZero(String s, int i) {
        StringBuffer buffer;
        if (StringUtils.isEmpty(s)) {
            buffer = new StringBuffer("");
        } else {
            buffer = new StringBuffer(s);
        }
        buffer.reverse();
        while (buffer.length() < i) {
            buffer.append("0");
        }
        buffer.reverse();
        return buffer.toString();
    }


    public static String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();
        char[] s1 = s.toCharArray();
        for (int i = 0; i < s1.length; i++) {
            char c = s1[i];
            if (c == 'u' && i + 4 < s1.length) {
                char c1 = s1[i + 1];
                char c2 = s1[i + 2];
                char c3 = s1[i + 3];
                char c4 = s1[i + 4];
                String hexVal = "" + c1 + c2 + c3 + c4;
                try {
                    char cc = (char) Integer.parseInt(hexVal, 16);
                    sbuf.append(cc);
                    i += 4;
                } catch (NumberFormatException e) {
                    hexVal = "" + c1 + c2;
                    try {
                        char cc = (char) Integer.parseInt(hexVal, 16);
                        sbuf.append(cc);
                        i += 2;
                    } catch (NumberFormatException e1) {
                        sbuf.append(c);
                    }
                }
            } else if (c == 'u' && i + 2 < s1.length) {
                char c1 = s1[i + 1];
                char c2 = s1[i + 2];
                String hexVal = "" + c1 + c2;
                try {
                    char cc = (char) Integer.parseInt(hexVal, 16);
                    sbuf.append(cc);
                    i += 2;
                } catch (NumberFormatException e1) {
                    sbuf.append(c);
                }
            } else {
                sbuf.append(c);
            }
        }
        return sbuf.toString();
    }

    public static byte[] intToByte(int v) {
        byte[] writeBuffer = new byte[4];
        writeBuffer[0] = (byte) (v >>> 24);
        writeBuffer[1] = (byte) (v >>> 16);
        writeBuffer[2] = (byte) (v >>> 8);
        writeBuffer[3] = (byte) (v);
        return writeBuffer;
    }

    public static int byteToInteger(byte[] readBuffer) {
        if (readBuffer == null || readBuffer.length < 4) {
            return 0;
        } else {
            return ((readBuffer[0] & 255) << 24) +
                    ((readBuffer[1] & 255) << 16) +
                    ((readBuffer[2] & 255) << 8) +
                    ((readBuffer[3] & 255));
        }
    }


    public static boolean isSuperClass(Class type, Class clazz) {
        if (clazz.equals(Object.class)) return true;
        while (!type.equals(Object.class)) {
            if (type.equals(clazz)) {
                return true;
            } else {
                type = type.getSuperclass();
            }
        }
        return false;
    }

    public static Object mutliCast(Object o, Class clazz) {
        if (o == null) {
            return null;
        }
        if (isSuperClass(o.getClass(), clazz)) {
            return o;
        }
        if (isSuperClass(clazz, Integer.class)) {
            return Integer.parseInt(o.toString());
        }
        if (isSuperClass(clazz, Double.class)) {
            return Double.parseDouble(o.toString());
        }
        if (isSuperClass(clazz, Long.class)) {
            return Long.parseLong(o.toString());
        }
        if (isSuperClass(clazz, Boolean.class)) {
            return Boolean.parseBoolean(o.toString());
        }
        if (isSuperClass(clazz, String.class)) {
            return (o.toString());
        }
        return null;
    }
}
