package net.ds3783.chatserver.protocol;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ASObject;
import flex.messaging.io.amf.Amf3Input;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-5-30
 * Time: 0:30:56
 * Amf3Э�������
 */
public class UnZippedAmf3InputProtocal extends InputProtocal {
    private static Log logger = LogFactory.getLog(UnZippedAmf3InputProtocal.class);

    /**
     * ����
     */
    @Override
    public void unmarshal() throws UnmarshalException {
        if (this.data == null || this.data.length < 4) {
            return;
        }
        String strTester = new String(data, 0, "<policy-file-request/>".length());
        if ("<policy-file-request/>".equals(strTester)) {
            Message authMessage = new Message();
            authMessage.setContent("<policy-file-request/>");
            authMessage.setType(MessageType.AUTH_MESSAGE);
            this.messages.add(authMessage);
            int len = "<policy-file-request/>".length();
            data = new byte[0];
        }
        if (this.data.length < 4) {
            this.remains = this.data;
            this.data = new byte[0];
            return;
        }
        int length = Utils.byteToInteger(this.data);
        while (this.data.length >= (length + 4)) {
            byte[] subdata = new byte[length];
            System.arraycopy(this.data, 4, subdata, 0, length);

            SerializationContext sc = new SerializationContext();
            SerializationContext.setSerializationContext(sc);
            Amf3Input amf3Input = new Amf3Input(sc);
            amf3Input.reset();
            amf3Input.setInputStream(new ByteArrayInputStream(subdata));
            Object obj = null;
            try {
                obj = amf3Input.readObject();
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new UnmarshalException(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new UnmarshalException(e.getMessage(), e);
            }
            if (obj instanceof ASObject) {
                ASObject cMsg = (ASObject) obj;
                Message message = readMessage(cMsg);
                this.messages.add(message);
            }
            byte[] remains = new byte[this.data.length - length - 4];
            System.arraycopy(this.data, 4 + length, remains, 0, this.data.length - length - 4);
            this.data = remains;
            if (this.data.length < 4) {
                this.remains = this.data;
                this.data = new byte[0];
                return;
            }
            length = Utils.byteToInteger(this.data);
        }
        this.remains = this.data;
        this.data = new byte[0];
    }

    private Message readMessage(ASObject cMsg) {
        Message result = new Message();
        result.setDestUid((String) Utils.mutliCast(cMsg.get("destUid"), String.class));
        result.setContent((String) Utils.mutliCast(cMsg.get("content"), String.class));
        result.setAuthCode((String) Utils.mutliCast(cMsg.get("authCode"), String.class));
        result.setChannel((String) Utils.mutliCast(cMsg.get("subType"), String.class));
        if ("LOGIN".equals(cMsg.get("type"))) {
            result.setType(MessageType.LOGIN_MESSAGE);
        } else {
            result.setType(MessageType.CHAT_MESSAGE);
        }
        logger.debug("message received " + Utils.describeBean(result));
        return result;
    }


}
