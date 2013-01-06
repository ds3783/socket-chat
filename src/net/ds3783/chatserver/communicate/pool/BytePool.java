package net.ds3783.chatserver.communicate.pool;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 23:06:38
 */
public interface BytePool {

    /**
     * 向缓存末尾添加数据
     *
     * @param clientId 客户端ID
     * @param data     数据
     */
    public void offerBytes(String clientId, byte[] data);

    /**
     * 从缓存中获取数据，一旦获取，缓存中的数据将被清除
     *
     * @param clientId 客户端ID
     * @return 缓存中的数据
     */
    public byte[] poolBytes(String clientId);

    /**
     * 从缓存中获取固定长度数据，一旦获取，缓存中的数据将被清除
     * 如果缓存中数据长度不足要求长度，则返回缓存中所有剩余数据。
     * 注意：返回结果的length属性不一定和传入参数的length一致
     *
     * @param clientId 客户端ID
     * @param length   待获取数据长度
     * @return 缓存中的数据
     */
    public byte[] poolBytes(String clientId, int length);

    /**
     * 修改缓存数据
     *
     * @param clientId 客户端ID
     * @param data     数据
     */
    public void setBytes(String clientId, byte[] data);

    /**
     * 取得缓存数据，但并不清除缓存
     *
     * @param clientId 客户端ID
     * @return 缓存数据
     */
    public byte[] getBytes(String clientId);

    /**
     * 取得固定长度的缓存数据，但并不清除缓存
     * 如果缓存中数据长度不足要求长度，则返回缓存中所有剩余数据。
     * 注意：返回结果的length属性不一定和传入参数的length一致
     *
     * @param clientId 客户端ID
     * @param length   待获取数据长度
     * @return 缓存数据
     */
    public byte[] getBytes(String clientId, int length);

    /**
     * 取得缓存数据的长度
     *
     * @param clientId 客户端ID
     * @return 缓存长度
     */
    public int getCachedSize(String clientId);
}
