/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import java.util.LinkedHashMap;
import javax.sql.DataSource;
import net.bujko.tablica.be.categs.CategoryManager;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import net.bujko.tablica.be.model.Ad;
import java.util.List;
import net.bujko.tablica.be.model.AttributeChoiceEntity;
import net.bujko.tablica.be.model.Category;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class LuceneSearcherDaoImplTest {

    @Autowired
    @Qualifier("searchDao")
    LuceneSearcherDaoImpl searchDaoInstance;
    @Autowired
    DataSource ds;
    @Autowired
    CategoryManager cm;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        ds.getConnection().createStatement().executeUpdate("truncate table ad_categs;");
        ds.getConnection().createStatement().executeUpdate("truncate table ad;");
    }

    /**
     * Test of add method, of class LuceneSearcherDaoImpl.
     */
    @Test
    public void testAddSearch() throws Exception {
        String adId1 = new Random().nextInt(5678) + "";
        String hashedId1 = "hashedId_" + adId1;
        String title1 = "title_" + adId1;
        String description1 = "description for " + adId1;

        Category c1 = cm.getCategoryById("1");
        assertNotNull(c1);


        Ad ad1 = new Ad();
        ad1.setId(adId1);
        ad1.addCategory(c1);
        ad1.setHashedId(hashedId1);
        ad1.setTitle(title1);
        ad1.setDescription(description1);
        String att11Id = "cat2_1", att12Id = "cat2_2";
        String choice1 = "2_1_1", choice2 = "2_2_1";
        ad1.addChoice(att11Id, choice1);
        ad1.addChoice(att12Id, choice2);

        searchDaoInstance.add(ad1);
        System.out.println("saved ad1: " + ad1);


        Category c2 = cm.getCategoryById("2");
        String itemId2 = new Random().nextInt(1234) + "";
        String hashedId2 = "hashedId2_" + itemId2;
        String title2 = "title_" + itemId2;
        String description2 = "description2 for " + itemId2;

        Ad item2 = new Ad();
        item2.setId(itemId2);
        item2.addCategory(c2);
        item2.setHashedId(hashedId2);
        item2.setTitle(title2);
        item2.setDescription(description2);

        searchDaoInstance.add(item2);



        //szukanie
        //search by catId1 resturns ad1
        Map<String, String> m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        List<Ad> res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        Ad adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());

        //szukanie po catId1 i po att1 zwraca ad1        
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        m.put("attChoice1", att11Id + "|" + choice1);
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());

        //szukanie po catId1 i po att2 zwraca tez ad1
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        m.put("attChoice1", att12Id + "|" + choice2);
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());


        //szukanie po catId1 i po att1 i po att2 tez zwraca ad1
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        m.put("attChoice1", att12Id + "|" + choice2);
        m.put("attChoice2", att12Id + "|" + choice2);
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());





        //finds nothing
        res = searchDaoInstance.search(c1.getId() + " AND " + c2.getId());
        assertEquals(0, res.size());


        //searching by cart2 returns item2
        res = searchDaoInstance.search(c2.getId());
        assertEquals(1, res.size());
        Ad si2 = res.get(0);
        assertEquals(itemId2, si2.getId());

        //returns two items
        res = searchDaoInstance.search(c1.getId() + " OR " + c2.getId());
        assertEquals(2, res.size());

        assertEquals(adId1, res.get(0).getId());
        assertEquals(itemId2, res.get(1).getId());

//        searchDaoInstance.rebuild();

        /**
         * dodac ad3,
         * jest w tej samej kategorii co ad1 ale ma wybrany inny att niz ad1
         * 
        
         * szukanie po cat1 i att1 - zwraca tylko ad1
         * szukanie po cat1 i att3 - zwraca tylko ad3
         */
        String adId3 = new Random().nextInt(5678) + "";
        Ad ad3 = new Ad();
        ad3.setId(adId3);
        ad3.addCategory(c1);
        ad3.setHashedId("hid3");
        ad3.setTitle("t33");
        ad3.setDescription("d3");
        String att31Id = "cat2_1", choice3 = "2_1_3";

        ad3.addChoice(att31Id, choice3);

        searchDaoInstance.add(ad3);
        //        szukanie po cat 1 zwraca teraz 2 
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(2, res.size());
        adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());
        adFound1 = res.get(1);
        assertEquals(adId3, adFound1.getId());

        //szukanie po catId1 i po att1 zwraca ad1        
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        m.put("attChoice1", att11Id + "|" + choice1);
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        adFound1 = res.get(0);
        assertEquals(adId1, adFound1.getId());

        //szukanie po catId1 i po att3 zwraca ad1        
        m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        m.put("attChoice1", att31Id + "|" + choice3);
        res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        adFound1 = res.get(0);
        assertEquals(adId3, adFound1.getId());


    }

    @Test
    public void testBuildQueryCat() {
        Map<String, String> params = new HashMap<String, String>();
        String catName = UUID.randomUUID() + "";
        params.put(ISearcherDao.FIELD_CAT_NAME, catName);

        assertEquals(ISearcherDao.FIELD_CAT_NAME + ":\"" + catName + "\"", searchDaoInstance.buildQuery(params));
    }

    @Test
    public void testuildQueryCatAndAtts() {
        Map<String, String> params = new LinkedHashMap<String, String>();
        String catName = UUID.randomUUID() + "";
        params.put(ISearcherDao.FIELD_CAT_NAME, catName);


        String attChoice1 = "attId1|choiceId1";
        params.put("attChoice1", attChoice1);

        String attChoice2 = "attId2|choiceId2";
        params.put("attChoice2", attChoice2);
        assertEquals(ISearcherDao.FIELD_CAT_NAME + ":\"" + catName + "\" "
                + "AND attChoice:\"" + attChoice1 + "\" AND attChoice:\"" + attChoice2 + "\"",
                searchDaoInstance.buildQuery(params));
    }
}
