/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.categs;

import org.junit.Ignore;
import net.bujko.tablica.be.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 * 
 * XML config file:
 * see attached nodesData.xml
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class CategoryManagerTest {

    @Autowired
    CategoryManager cm;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testLoadedData() throws Exception {

        assertEquals(9, cm.getCategoryCount());
        //ensure category 1 is not top level
        assertNotNull(
                cm.getParentCategory("1"));

        //ensure category 11 has parent (id:1) and no child

        assertEquals(cm.getCategoryById("1"), cm.getParentCategory("11"));

        //ensure category 12 has parent (id:1) and one child (id: 121)
        assertEquals(cm.getCategoryById("1"), cm.getParentCategory("12"));

        assertEquals(1, cm.getChildCategories("12").size());
        assertEquals(cm.getCategoryById("121"), cm.getChildCategories("12").get(0));

        assertEquals(3, cm.getTopLevelCategories().size());

        //top level cats are 0.1, 0.2, 0.3
        assertTrue(cm.getTopLevelCategories().contains( cm.getCategoryById("0.1") ));
        assertTrue(cm.getTopLevelCategories().contains(cm.getCategoryById("0.2")));
        assertTrue(cm.getTopLevelCategories().contains(cm.getCategoryById("0.3")));

        //second level cats are 1,2,3
        assertEquals(cm.getCatsByDepth(2).size(), 3);
        assertEquals(cm.getCatsByDepth(3).size(), 2);
        assertEquals(cm.getCatsByDepth(4).size(), 1);

        assertNull(
                cm.getCategoryByCode("aaaaa"));

        assertEquals(
                cm.getCategoryByCode("node-0.3"),
                cm.getCategoryById("0.3"));

        assertEquals(
                cm.getCategoryByCode("node-11"),
                cm.getCategoryById("11"));
    }

    @Test
    @Ignore
    public void testExampleData() {
        assertNull(cm.getCategoryById("9999999"));

        assertNull(cm.getCategoryById("0.1").getParent());

        assertEquals(cm.getCategoryById("0.1"), cm.getCategoryById("1").getParent());

        assertEquals(cm.getCategoryById("1"), cm.getCategoryById("12").getParent());


    }
}
