/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import net.bujko.tablica.be.model.Ad;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("adDao")
public class AdDaoImpl extends CustomHibernateDaoSupport implements AdDao {
    
    @Override
    public void save(Ad ad) {
        getHibernateTemplate().save(ad);
    }
    
    @Override
    public void update(Ad ad) {
        getHibernateTemplate().update(ad);
    }
    
    @Override
    public void delete(Ad ad) {
        getHibernateTemplate().delete(ad);
    }

    @Override
    public Ad findById(String id) {
return (Ad) getHibernateTemplate().get(Ad.class, id);
    }
}
