/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import net.bujko.tablica.be.model.Category;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pbujko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-context.xml"})
public class CategoryDaoImplTest {

    @Autowired
    CategoryDao catDao;
    Category c1, c2;

    public CategoryDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        for (Category c : catDao.getAll()) {

            catDao.delete(c);
        }

    }

    /**
     * Test of save method, of class CategoryDaoImpl.
     */
    @Test
    public void testSave() throws Exception {

        c1 = new Category("1");
        c1.setLabel("c1");
        catDao.save(c1);

        c2 = new Category("2");
        c2.setLabel("c2");
        catDao.save(c2);
    }

    /**
     * Test of update method, of class CategoryDaoImpl.
     */
    public void testUpdate() throws SQLException {
        System.out.println("update");
        Category c = null;
        CategoryDaoImpl instance = new CategoryDaoImpl();

        instance.update(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveOrUpdate method, of class CategoryDaoImpl.
     */
    public void testSaveOrUpdate() throws Exception {
        System.out.println("saveOrUpdate");
        Category c = null;
        CategoryDaoImpl instance = new CategoryDaoImpl();
        instance.saveOrUpdate(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class CategoryDaoImpl.
     */
    public void testDelete() {
        System.out.println("delete");
        Category c = null;
        CategoryDaoImpl instance = new CategoryDaoImpl();
//        instance.delete(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
