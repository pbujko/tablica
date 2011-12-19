/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be;

import java.util.HashMap;
import net.bujko.tablica.be.model.Ad;
import java.util.Map;
import net.bujko.tablica.be.dao.AdDao;
import net.bujko.tablica.be.search.ISearcherDao;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author pbujko
 */
public class AdRegistryServiceTest {
       
    private AdDao adRepository;
    private ISearcherDao searchRepository;
    AdRegistryService instance;
    
    
    public AdRegistryServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        instance = new AdRegistryService();
        adRepository=mock(AdDao.class);
        instance.setAdRepository(adRepository);
        
        searchRepository=mock(ISearcherDao.class);
        instance.setSearchRepository(searchRepository);
    }

    /**
     * Test of saveAd method, of class AdRegistryService.
     */
    @Test
    public void testSaveAd() throws Exception {
        System.out.println("saveAd");
        Map<String, String> adProps = new HashMap<String, String>();
    
        instance.saveAd(adProps);

        verify(searchRepository).add(any(Ad.class));
        verify(adRepository).save(any(Ad.class));
    }
    
    @Test(expected=java.lang.Exception.class)
    public void testSaveAdNegative() throws Exception{
    
        instance.saveAd(null);
    }
}
