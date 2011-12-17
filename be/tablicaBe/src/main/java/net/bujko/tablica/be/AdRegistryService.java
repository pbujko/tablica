/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be;

import java.util.Map;
import net.bujko.tablica.be.dao.AdDao;
import net.bujko.tablica.be.search.ISearcherDao;

import net.bujko.tablica.be.model.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pbujko
 */
@Service
public class AdRegistryService {

    ISearcherDao searchIndexDao;
    
    @Autowired
    AdDao adRepository;
    
    @Autowired
    ISearcherDao searchRepository;
    public void saveAd(Map<String, String> adProps) throws Exception{
    //to do add loci
        
        Ad ad=new Ad();
        //persist
        adRepository.save(ad);
        
        //add to lucenee
        searchRepository.add(ad);
    }
    
}
