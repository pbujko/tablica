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

    public void add(Ad item) throws Exception;

    public void delete(Ad item) throws Exception;

    public void update(Ad ad) throws Exception;

    public List<Ad> search(String searchQ) throws Exception;

    public void rebuild() throws Exception;

    public String buildQuery(Map<String, String> params);
}
