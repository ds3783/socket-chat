package net.ds3783.chatserver.dao;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-7-2
 * Time: ÏÂÎç9:41
 * To change this template use File | Settings | File Templates.
 */

public class ClientDaoTest extends AbstractDependencyInjectionSpringContextTests {
    private ClientDao clientDao;

    public void testAddClient() throws Exception {
        assertNotNull(clientDao);
        Client c1 = new Client();
        c1.setName("Ds.3783");
        clientDao.addClient(c1);
        Client c2 = clientDao.getClientByName("Ds.3783");
        assertFalse(c1 == c2);
        assertEquals(c1.getUid(), c2.getUid());
    }

    public void testGetClient() throws Exception {

    }

    public void testGetClintAmount() throws Exception {
        Client c1 = new Client();
        c1.setName("Ds.3783");
        clientDao.addClient(c1);
        long result = clientDao.getClintAmount();
        assertEquals(result, 1);
    }

    public void testGetLoginClientUids() {
        Client c1 = new Client();
        c1.setName("Ds.3783");
        clientDao.addClient(c1);
        clientDao.updateClientLogined(c1.getUid(), true);
        Set<String> result = clientDao.getLoginClientUids();
        assertEquals(result.size(), 1);
        assertEquals(result.iterator().next(), c1.getUid());
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }


    @Override
    protected String[] getConfigPaths() {
        return new String[]{"/core-beans.xml"};
    }
}
