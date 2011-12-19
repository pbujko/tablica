/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

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
    LuceneSearcherDaoImpl instance;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
//        instance = new LuceneSearcherDaoImpl();
    }

    /**
     * Test of add method, of class LuceneSearcherDaoImpl.
     */
    @Test
    public void testAddSearch() throws Exception {
        String itemId1 = new Random().nextInt(5678)+"";

        String catId1 = "cat1_" + itemId1;
        String catId2 = "cat2_" + itemId1;
        String hashedId = "hashedId_" + itemId1;
        String title = "title_" + itemId1;
        String description = "description for " + itemId1;

        System.out.println("add");
        Ad item1 = new Ad();
        item1.setId(itemId1);
        item1.addCategory(new Category(catId1));
        item1.addCategory(new Category(catId2));
        item1.setHashedId(hashedId);
        item1.setTitle(title);
        item1.setDescription(description);


        instance.add(item1);


        String itemId2 = new Random().nextInt(1234)+"";
        String catId21 = "cat21_" + itemId2;
        String catId22 = "cat22_" + itemId2;
        String hashedId2 = "hashedId2_" + itemId2;
        String title2 = "title_" + itemId2;
        String description2 = "description2 for " + itemId2;


        Ad item2 = new Ad();
        item2.setId(itemId2);
        item2.addCategory(new Category(catId21));
        item2.addCategory(new Category(catId22));
        item2.setHashedId(hashedId2);
        item2.setTitle(title2);
        item2.setDescription(description2);

        instance.add(item2);


        //search by catId1 resturns item1 item1
        List<Ad> res = instance.search(catId1);
        assertEquals(1, res.size());
        Ad si1 = res.get(0);
        assertEquals(itemId1, si1.getId());

        //searching by catId2 returns item1
        res = instance.search(catId2);
        assertEquals(1, res.size());
        si1 = res.get(0);
        assertEquals(itemId1, si1.getId());

        //same results
        res = instance.search(catId2 + " AND " + catId1);
        assertEquals(1, res.size());
        si1 = res.get(0);
        assertEquals(itemId1, si1.getId());

        res = instance.search(catId2 + " AND " + catId1 + " AND booo");
        assertEquals(0, res.size());



        //searching by carId2 returns item1
        res = instance.search(catId21);
        assertEquals(1, res.size());
        Ad si2 = res.get(0);
        assertEquals(itemId2, si2.getId());

        //returns two items
        res = instance.search(catId21 + " OR " + catId1);
        assertEquals(2, res.size());

        assertEquals(itemId1, res.get(0).getId());
        assertEquals(itemId2, res.get(1).getId());
        
        instance.rebuild();

    }
    
    @Test
    public void testBuildQuery(){
        Map<String, String> params=new HashMap<String, String>();        
        String catName=UUID.randomUUID()+"";
        params.put(ISearcherDao.FIELD_CAT_NAME, catName);
        
        assertEquals(ISearcherDao.FIELD_CAT_NAME+":"+catName, instance.buildQuery(params));
        
    }
}
