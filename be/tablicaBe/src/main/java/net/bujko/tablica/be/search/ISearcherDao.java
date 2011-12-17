/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import net.bujko.tablica.be.model.Ad;
import java.util.List;

/**
 *
 * @author pbujko
 */
public interface ISearcherDao {
    void add(Ad item) throws Exception;
    void delete();
    List<Ad> search (String searchQ) throws Exception;
    
}
