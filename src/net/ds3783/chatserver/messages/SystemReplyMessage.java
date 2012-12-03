package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-12-1
 * Time: ����12:00
 * To change this template use File | Settings | File Templates.
 */
public class SystemReplyMessage implements Message {
    public static final int CODE_100 = 100;

    public static final int CODE_200 = 200;
    public static final int CODE_LOGIN_SUCCESS = 201;//��¼�ɹ�
    public static final int CODE_CHANNEL_LIST = 202;//Ƶ���б�

    public static final int CODE_USER_ONLINE = 301;//�����û���¼

    public static final int CODE_ERROR_NOT_LOGIN = 401;//��δ��¼
    public static final int CODE_ERROR_WRONG_PASSWORD = 402;//��¼�û������������
    public static final int CODE_ERROR_BLACKLIST = 403;//�û�������
    public static final int CODE_ERROR_USER_CUSTOM = 499;//�û�������

    private int code;
    private String content;

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
