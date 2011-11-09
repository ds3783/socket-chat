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
     * 向缓存末尾添加数据
     *
     * @param clientId 客户端ID
     * @param data     数据
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
     * 从缓存中获取数据，一旦获取，缓存中的数据将被清除
     *
     * @param clientId 客户端ID
     * @return 缓存中的数据
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
     * 从缓存中获取固定长度数据，一旦获取，缓存中的数据将被清除
     * 如果缓存中数据长度不足要求长度，则返回缓存中所有剩余数据。
     * 注意：返回结果的length属性不一定和传入参数的length一致
     *
     * @param clientId 客户端ID
     * @param length   待获取数据长度
     * @return 缓存中的数据
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
     * 修改缓存数据
     *
     * @param clientId 客户端ID
     * @param data     数据
     */
    public void setBytes(String clientId, byte[] data) {
        synchronized (buffers) {
            buffers.put(clientId, new Buffer(data));
        }
    }

    /**
     * 取得缓存数据，但并不清除缓存
     *
     * @param clientId 客户端ID
     * @return 缓存数据
     */
    public byte[] getBytes(String clientId) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        return buffer.getAll();
    }

    /**
     * 取得固定长度的缓存数据，但并不清除缓存
     * 如果缓存中数据长度不足要求长度，则返回缓存中所有剩余数据。
     * 注意：返回结果的length属性不一定和传入参数的length一致
     *
     * @param clientId 客户端ID
     * @param length   待获取数据长度
     * @return 缓存数据
     */
    public byte[] getBytes(String clientId, int length) {
        Buffer buffer = buffers.get(clientId);
        if (buffer == null) {
            return new byte[0];
        }
        return buffer.get(length);
    }

    /**
     * 取得缓存数据的长度
     *
     * @param clientId 客户端ID
     * @return 缓存长度
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
