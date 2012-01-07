/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.UUID;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 */
public class AdTest {

    Ad adInstance;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        adInstance = new Ad();
    }

    @Test(expected = java.lang.Exception.class)
    public void testAddCategory() throws Exception {
        String cId = UUID.randomUUID().toString();
        Category c = new Category(cId);

        assertEquals(0,
                adInstance.getAssignedCategories().size());
        adInstance.addCategory(c);

        assertEquals(1,
                adInstance.getAssignedCategories().size());

        adInstance.addCategory(c);
    }

    @Test(expected = java.lang.Exception.class)
    public void testAddChoice() throws Exception {
        String attId = UUID.randomUUID().toString();

        assertEquals(0, adInstance.getChoices().size());
        adInstance.addChoice(attId, "choice_" + attId);

        assertEquals(1, adInstance.getChoices().size());
        adInstance.addChoice(attId, "choice_" + attId);

    }

    @Test
    public void testActive() {
        assertFalse(adInstance.isActive());
        adInstance.setState(Ad.State.PENDING);
        assertFalse(adInstance.isActive());
        adInstance.setState(Ad.State.CONFIRMED);
        assertTrue(adInstance.isActive());
    }
}
