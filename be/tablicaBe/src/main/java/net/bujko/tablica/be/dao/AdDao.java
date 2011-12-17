/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import net.bujko.tablica.be.model.Ad;

/**
 *
 * @author pbujko
 */
public interface AdDao {

    void save(Ad ad);

    void update(Ad ad);

    void delete(Ad ad);
    
    Ad findById(String id);
}
