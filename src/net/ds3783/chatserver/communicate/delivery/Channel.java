package net.ds3783.chatserver.communicate.delivery;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 17:41:51
 */
public class Channel implements Serializable {
    public static final Channel PUBLIC = new Channel("PUBLIC_CHANNEL", "公共频道");
    public static final Channel TRANSACTION = new Channel("TRANSACTION_CHANNEL", "交易频道");
    public static final Channel SYSTEM = new Channel("SYSTEM_CHANNEL", "系统频道");

    private Channel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (code != null ? !code.equals(channel.code) : channel.code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
