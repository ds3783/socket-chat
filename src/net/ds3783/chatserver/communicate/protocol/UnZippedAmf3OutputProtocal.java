package net.ds3783.chatserver.communicate.protocol;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

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
        byte[] result = new byte[0];
        for (Iterator<Message> messageIterator = messages.iterator(); messageIterator.hasNext(); ) {
            Message message = messageIterator.next();


            try {
                if (!message.isSerializable()) {
                    byte[] data = message.getContent().getBytes();
                    byte[] buffer = result;
                    result = new byte[buffer.length + data.length];
                    System.arraycopy(buffer, 0, result, 0, buffer.length);
                    System.arraycopy(data, 0, result, buffer.length, data.length);
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    SerializationContext sc = new SerializationContext();
                    SerializationContext.setSerializationContext(sc);
                    Amf3Output amf3Output = new Amf3Output(sc);
                    amf3Output.setOutputStream(out);
                    amf3Output.writeObject(message);
                    amf3Output.flush();

                    byte[] data = out.toByteArray();
                    byte[] length = Utils.intToByte(data.length);
                    byte[] buffer = result;
                    result = new byte[buffer.length + length.length + data.length];
                    logger.debug("buffer.len:" + buffer.length);
                    logger.debug("length.len:" + length.length);
                    logger.debug("data.len:" + data.length);
                    System.arraycopy(buffer, 0, result, 0, buffer.length);
                    System.arraycopy(length, 0, result, buffer.length, length.length);
                    System.arraycopy(data, 0, result, buffer.length + length.length, data.length);
                }
                messageIterator.remove();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new MarshalException(e.getMessage(), e);
            }
        }

        logger.debug("result.len:" + result.length);
        return result;
    }
}
