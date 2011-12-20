/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.Random;
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
import org.junit.Ignore;
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

    Category c1, c2;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
        for (Ad a : adDao.listAll()) {            
            adDao.delete(a);
        }        
        
        c1 = new Category(new Random().nextInt(100)+"");
        c1.setLabel("label_" + c1.getId());
        
        c2 = new Category(new Random().nextInt(100)+"");
        c2.setLabel("label_" + c2.getId());        
    }

    /**
     * Test of save method, of class AdDaoImpl.
     */
    @Test
    public void testSave() throws Exception {
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
        
        assertEquals(1, adDao.listAll().size());
    }
}
