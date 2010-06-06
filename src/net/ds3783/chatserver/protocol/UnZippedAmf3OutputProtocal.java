package net.ds3783.chatserver.protocol;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;
import net.ds3783.chatserver.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-6-6
 * Time: 22:37:11
 * To change this template use File | Settings | File Templates.
 */
public class UnZippedAmf3OutputProtocal extends OutputProtocal {
    private static Log logger = LogFactory.getLog(UnZippedAmf3InputProtocal.class);

    /**
     * 编码
     *
     * @return 待输出到网络上的字节数组
     */
    @Override
    public byte[] marshal() throws MarshalException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SerializationContext sc = new SerializationContext();
        SerializationContext.setSerializationContext(sc);
        Amf3Output amf3Output = new Amf3Output(sc);
        amf3Output.setOutputStream(out);
        for (Message message : messages) {
            try {
                amf3Output.writeObject(message);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new MarshalException(e.getMessage(), e);
            }
        }
        try {
            amf3Output.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new MarshalException(e.getMessage(), e);
        }
        return out.toByteArray();
    }
}
