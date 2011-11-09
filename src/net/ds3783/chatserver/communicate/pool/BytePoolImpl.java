package net.ds3783.chatserver.communicate.pool;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-4
 * Time: 20:33:53
 * To change this template use File | Settings | File Templates.
 */
public class BytePoolImpl implements BytePool {
    private final Map<String, Buffer> buffers = new Hashtable<String, Buffer>();

    /**
     * �򻺴�ĩβ�������
     *
     * @param clientId �ͻ���ID
     * @param data     ����
     */
    public void offerBytes(String clientId, byte[] data) {
        Buffer buffer;
        if (buffers.containsKey(clientId)) {
            buffer = buffers.get(clientId);
        } else {
            buffer = new Buffer();
            synchronized (buffers) {
                buffers.put(clientId, buffer);
            }
        }
        buffer.append(data);
    }

    /**
     * �ӻ����л�ȡ���ݣ�һ����ȡ�������е����ݽ������
     *
     * @param clientId �ͻ���ID
     * @return �����е�����
     */
    public byte[] poolBytes(String clientId) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        byte[] result = buffer.getAll();
        buffer.clear();
        return result;
    }

    /**
     * �ӻ����л�ȡ�̶��������ݣ�һ����ȡ�������е����ݽ������
     * ������������ݳ��Ȳ���Ҫ�󳤶ȣ��򷵻ػ���������ʣ�����ݡ�
     * ע�⣺���ؽ����length���Բ�һ���ʹ��������lengthһ��
     *
     * @param clientId �ͻ���ID
     * @param length   ����ȡ���ݳ���
     * @return �����е�����
     */
    public byte[] poolBytes(String clientId, int length) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        byte[] result = getBytes(clientId, length);
        buffer.remove(length);

        return result;
    }

    /**
     * �޸Ļ�������
     *
     * @param clientId �ͻ���ID
     * @param data     ����
     */
    public void setBytes(String clientId, byte[] data) {
        synchronized (buffers) {
            buffers.put(clientId, new Buffer(data));
        }
    }

    /**
     * ȡ�û������ݣ��������������
     *
     * @param clientId �ͻ���ID
     * @return ��������
     */
    public byte[] getBytes(String clientId) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        return buffer.getAll();
    }

    /**
     * ȡ�ù̶����ȵĻ������ݣ��������������
     * ������������ݳ��Ȳ���Ҫ�󳤶ȣ��򷵻ػ���������ʣ�����ݡ�
     * ע�⣺���ؽ����length���Բ�һ���ʹ��������lengthһ��
     *
     * @param clientId �ͻ���ID
     * @param length   ����ȡ���ݳ���
     * @return ��������
     */
    public byte[] getBytes(String clientId, int length) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        return buffer.get(length);
    }

    /**
     * ȡ�û������ݵĳ���
     *
     * @param clientId �ͻ���ID
     * @return ���泤��
     */
    public int getCachedSize(String clientId) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return 0;
        } else {
            return buffer.getSize();
        }
    }

    private class Buffer {
        private List<byte[]> data = new ArrayList<byte[]>();
        private int size = 0;

        public Buffer(byte[] data) {
            if (data != null && data.length > 0) {
                this.data.add(data);
                size += data.length;
            }
            //To change body of created methods use File | Settings | File Templates.
        }

        public Buffer() {

        }

        public void append(byte[] bytes) {
            if (bytes != null && bytes.length > 0) {
                size += bytes.length;
                data.add(bytes);
            }
        }

        public byte[] getAll() {
            byte[] result = new byte[size];
            int offset = 0;
            for (byte[] bytes : data) {
                System.arraycopy(bytes, 0, result, offset, bytes.length);
                offset += bytes.length;
            }
            return result;
        }

        public void clear() {
            data.clear();
            size = 0;
        }

        public byte[] get(int length) {
            int bytesLeft = Math.min(size, length);
            byte[] result = new byte[bytesLeft];
            int offset = 0;
            for (byte[] bytes : data) {
                int len = Math.min(bytesLeft, bytes.length);
                System.arraycopy(bytes, 0, result, offset, len);
                offset += len;
                offset -= bytesLeft;
                if (bytesLeft == 0) break;
            }
            return result;
        }

        public void remove(int length) {
            int bytesLeft = Math.min(size, length);
            int offset = 0;
            byte[] newData = new byte[0];
            for (Iterator<byte[]> iterator = data.iterator(); iterator.hasNext(); ) {
                byte[] bytes = iterator.next();
                if (bytes.length <= bytesLeft) {
                    iterator.remove();
                    bytesLeft -= bytes.length;
                } else if (bytesLeft > 0) {
                    newData = new byte[bytes.length - bytesLeft];
                    System.arraycopy(bytes, bytesLeft, newData, 0, newData.length);
                    iterator.remove();
                    break;
                } else {
                    break;
                }
            }
            List<byte[]> newDatas = new ArrayList<byte[]>();
            if (newData.length > 0) {
                newDatas.add(newData);
            }
            newDatas.addAll(data);
            data.clear();
            data.addAll(newDatas);
            size = newData.length;
        }

        public int getSize() {
            return size;
        }
    }
}
