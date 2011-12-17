/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.categs;

import net.bujko.tablica.be.model.Category;
import junit.framework.TestCase;
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
 * 
 * 
 * 
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<allNodes xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
xsi:noNamespaceSchemaLocation='test.xsd'> 
<node label="NODE_1" id="1">
<node label="NODE_11" id="11"/>
<node label="NODE_12" id="12">
<node label="NODE_121" id="121"/>
</node>
</node>
<node label="NODE_2" id="2"/>
<node label="NODE_3" id="3"/>
</allNodes>

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
     cm.init();
    }

    @Test
    public void testLoadedData() throws Exception {

        assertTrue(cm.getCategoryCount() == 6);

        //ensure category 1 is top level
        assertNull(
                cm.getParentCategory("1"));

        //ensure category 11 has parent (id:1) and no child

        assertEquals(new Category("1"), cm.getParentCategory("11"));

        //ensure category 12 has parent (id:1) and one child (id: 121)
        assertEquals(new Category("1"), cm.getParentCategory("12"));

        assertEquals(1, cm.getChildCategories("12").size());
        assertEquals(new Category("121"), cm.getChildCategories("12").get(0));

    }
}
