package net.ds3783.chatserver.pooltest;

import junit.framework.TestCase;
import net.ds3783.chatserver.communicate.pool.BytePool;
import net.ds3783.chatserver.communicate.pool.BytePoolImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-5
 * Time: 22:37:18
 * BytePool测试类
 */
public class BytePoolTest extends TestCase {
    BytePool subject;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        subject = new BytePoolImpl();
    }

    public void testOfferBytes() throws Exception {
        subject.poolBytes("test");
        byte[] b = "asdf".getBytes();
        subject.offerBytes("test", b);
        b = "fdsa".getBytes();
        subject.offerBytes("test", b);
        String result = new String(subject.getBytes("test"));
        assertTrue("Offered Bytes 没有返回,返回值:" + result, "asdffdsa".equals(result));
        b = "poiu".getBytes();
        subject.offerBytes("test", b);
        result = new String(subject.getBytes("test"));
        assertTrue("Offered Bytes 没有返回,返回值:" + result, "asdffdsapoiu".equals(result));

    }


    public void testPoolBytes() throws Exception {
//        byte[] b = "asdf".getBytes();
    }

    public void testSetBytes() throws Exception {
    }


    public void testGetBytes() throws Exception {
    }

    public void testGetCachedSize() throws Exception {
    }
}
