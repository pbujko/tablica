/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import net.bujko.tablica.be.model.Ad;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pbujko
 */
public interface ISearcherDao {
    final String FIELD_CAT_NAME = "cat";
    void add(Ad item) throws Exception;
    void delete(Ad item) throws Exception;
    void update(Ad ad) throws Exception;
    List<Ad> search (String searchQ) throws Exception;
    
    void rebuild() throws Exception;
    
    String buildQuery(Map<String, String> params);
    
}
