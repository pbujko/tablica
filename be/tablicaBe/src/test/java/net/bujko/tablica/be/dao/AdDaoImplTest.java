/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import net.bujko.tablica.be.categs.CategoryManager;
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
    CategoryManager cm;
    Category c01, c1, c11;
    String attChoiceId, attChoiceId2, choiceId, choiceId2;

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

        c01 = cm.getCategoryById("0.1");
        c1 = cm.getCategoryById("1");
        c11 = cm.getCategoryById("11");
        attChoiceId = UUID.randomUUID().toString();
        choiceId = "choice_" + attChoiceId;
        attChoiceId2 = UUID.randomUUID().toString();
        choiceId2 = "choice2_" + attChoiceId2;

    }

    /**
     * Test of save method, of class AdDaoImpl.
     */
    @Test
    public void testSave() throws Exception {

        assertNotNull(c11);

        String hashId = UUID.randomUUID().toString();
        Ad ad = new Ad();
        ad.setHashedId(hashId);
        ad.setTitle("title_" + hashId);
        ad.setDescription("description_" + hashId);
        ad.addCategory(c11);

        Map<String, String> mAttChoices = new HashMap<String, String>();
        mAttChoices.put(attChoiceId, choiceId);
        mAttChoices.put(attChoiceId2, choiceId2);
        ad.addChoices(mAttChoices);

        adDao.save(ad);


        System.out.println(String.format("saved, id %s", ad));

        Ad res =
                adDao.findById(ad.getId());
        assertNotNull(res);
        assertEquals(res, ad);
        assertEquals(c11, ad.getCategory());
        assertEquals(3, ad.getAssignedCategories().size());
        assertTrue(ad.getAssignedCategories().contains(c11));
        assertTrue(ad.getAssignedCategories().contains(c1));
        assertTrue(ad.getAssignedCategories().contains(c01));
        assertEquals(mAttChoices, ad.getChoices());

        assertEquals(1, adDao.listAll().size());
    }
}
