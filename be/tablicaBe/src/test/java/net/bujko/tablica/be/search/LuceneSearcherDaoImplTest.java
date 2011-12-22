/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

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
        String itemId1 = new Random().nextInt(5678) + "";
        String hashedId1 = "hashedId_" + itemId1;
        String title1 = "title_" + itemId1;
        String description1 = "description for " + itemId1;

        Category c1 = cm.getCategoryById("1");
        assertNotNull(c1);

        Ad item1 = new Ad();
        item1.setId(itemId1);
        item1.addCategory(c1);
        item1.setHashedId(hashedId1);
        item1.setTitle(title1);
        item1.setDescription(description1);

        searchDaoInstance.add(item1);

        Category c2 = cm.getCategoryById("2");

        String itemId2 = new Random().nextInt(1234) + "";
        String catId2 = "cat2_" + itemId2;
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

        //search by catId1 resturns item1
        Map<String, String> m = new HashMap<String, String>();
        m.put("cat", c1.getId());
        List<Ad> res = searchDaoInstance.search(searchDaoInstance.buildQuery(m));
        assertEquals(1, res.size());
        Ad si1 = res.get(0);
        assertEquals(itemId1, si1.getId());


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

        assertEquals(itemId1, res.get(0).getId());
        assertEquals(itemId2, res.get(1).getId());

        searchDaoInstance.rebuild();
    }

    @Test
    public void testBuildQuery() {
        Map<String, String> params = new HashMap<String, String>();
        String catName = UUID.randomUUID() + "";
        params.put(ISearcherDao.FIELD_CAT_NAME, catName);

        assertEquals(ISearcherDao.FIELD_CAT_NAME + ":\"" + catName + "\"", searchDaoInstance.buildQuery(params));

    }
}
