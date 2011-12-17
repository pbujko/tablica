/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import net.bujko.tablica.be.model.Ad;
import java.util.Random;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import net.bujko.tablica.be.model.Category;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 */
public class LuceneSearcherDaoImplTest {

    LuceneSearcherDaoImpl instance;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

        instance = new LuceneSearcherDaoImpl();
    }

    /**
     * Test of add method, of class LuceneSearcherDaoImpl.
     */
    @Test
    public void testAddSearch() throws Exception {
        String itemId1 = UUID.randomUUID().toString();
        String catId1 = UUID.randomUUID().toString();
        String catId2 = UUID.randomUUID().toString();

        System.out.println("add");
        Ad item1 = new Ad();
        item1.setId(itemId1);
        item1.addCategory(new Category(catId1));
        item1.addCategory(new Category(catId2));

        instance.add(item1);

        
        String itemId2 = UUID.randomUUID().toString();
        String catId21 = UUID.randomUUID().toString();
        String catId22 = UUID.randomUUID().toString();
        
        Ad item2 = new Ad();
        item2.setId(itemId2);
        item2.addCategory(new Category(catId21));
        item2.addCategory(new Category(catId22));

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
        res = instance.search(catId21 +" OR "+catId1);
        assertEquals(2, res.size());
        
        assertEquals(itemId1, res.get(0).getId());
        assertEquals(itemId2, res.get(1).getId());

                
    }
}
