package net.ds3783.chatserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-17
 * Time: 17:45:30
 */
public class ClientType implements Serializable {
    public static ClientType GM=new ClientType("GM");
    public static ClientType USER=new ClientType("USER");
    public static ClientType GAMESERVER=new ClientType("GAMESERVER");

    private ClientType(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientType that = (ClientType) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
