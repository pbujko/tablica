/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.List;
import java.util.UUID;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import net.bujko.tablica.be.model.Ad;
import net.bujko.tablica.be.model.Category;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class AdDaoImplTest {

    @Autowired
    AdDao adDao;
    @Autowired
    CategoryDao catDao;
    Category c1, c2;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        List<Category> l =
                catDao.getAll();
        for (Category c : l) {
            catDao.delete(c);
        }

        c1 = new Category("1");
        c1.setLabel("c1");
        catDao.save(c1);

        c2 = new Category("2");
        c2.setLabel("c2");
        catDao.save(c2);

        
        catDao.update(c2);

    }

    /**
     * Test of save method, of class AdDaoImpl.
     */
    @Test
    public void testSave() {
        String hashId = UUID.randomUUID().toString();
        Ad ad = new Ad();
        ad.setHashedId(hashId);
        ad.setTitle("title_" + hashId);
        ad.setDescription("description_" + hashId);
        ad.addCategory(c1);
        ad.addCategory(c2);
        adDao.save(ad);


        System.out.println(String.format("saved, id %s", ad.getId()));

        Ad res =
                adDao.findById(ad.getId());
        assertNotNull(res);
        assertEquals(res, ad);
    }
}
