package net.ds3783.chatserver.communicate.protocol;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import net.ds3783.chatserver.messages.FlashAuthMessage;
import net.ds3783.chatserver.messages.Message;
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
 * Amf3协议解码器
 */
public class UnZippedAmf3InputProtocal extends InputProtocal {
    private static Log logger = LogFactory.getLog(UnZippedAmf3InputProtocal.class);

    /**
     * 解码
     */
    @Override
    public void unmarshal() throws UnmarshalException {
        if (this.data == null || this.data.length < 4) {
            return;
        }
        String strTester = new String(data, 0, "<policy-file-request/>".length());
        if ("<policy-file-request/>".equals(strTester)) {
            FlashAuthMessage authMessage = new FlashAuthMessage();
            authMessage.setContent("<policy-file-request/>");
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

            this.messages.add((Message) obj);
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


}
