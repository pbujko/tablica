/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.bujko.tablica.be.dao.AdDao;
import net.bujko.tablica.be.search.ISearcherDao;

import net.bujko.tablica.be.model.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author pbujko
 */
@Service
public class AdRegistryService {

    Logger logger = LoggerFactory.getLogger(AdRegistryService.class);
    private AdDao adRepository;
    private ISearcherDao searchRepository;

    public void saveAd(Map<String, String> adProps) throws Exception {
        if (adProps == null) {
            throw new Exception();
        }
        Ad ad = new Ad();
        //persist
        adRepository.save(ad);

        //add to lucenee
        searchRepository.add(ad);
    }

    public List<Ad> search(Map<String, String> params) {
        try {
            return searchRepository.search(searchRepository.buildQuery(params));
        } catch (Exception ex) {
            logger.error("SRCH", ex);
            return new ArrayList<Ad>();
        }

    }

    @Autowired
    @Qualifier("adDao")
    public void setAdRepository(AdDao adRepository) {
        this.adRepository = adRepository;
    }

    @Autowired
    @Qualifier("searchDao")
    public void setSearchRepository(ISearcherDao searchRepository) {
        this.searchRepository = searchRepository;
    }

}
