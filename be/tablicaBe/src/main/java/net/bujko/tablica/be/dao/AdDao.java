/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.List;
import net.bujko.tablica.be.model.Ad;

/**
 *
 * @author pbujko
 */

public interface AdDao {

    void save(Ad ad) throws Exception;

    void update(Ad ad) throws Exception;

    void delete(Ad ad) throws Exception;

    Ad findById(String id) throws Exception;
    
    List<Ad> listAll() throws Exception;
    
    List<Ad> listRecent(int from, int limit) throws Exception;
    
}
