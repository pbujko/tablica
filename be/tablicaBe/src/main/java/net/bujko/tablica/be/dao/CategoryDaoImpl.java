/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import net.bujko.tablica.be.model.Category;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("categoryDao")
public class CategoryDaoImpl extends CustomHibernateDaoSupport implements CategoryDao{

    @Override
    public void save(Category c) {
        getHibernateTemplate().save(c);
    }

    @Override
    public void update(Category c) {
//        getHibernateTemplate().saveOrUpdate(c);
    }

    @Override
    public void delete(Category c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveOrUpdate(Category c) {
        getHibernateTemplate().saveOrUpdate(c);
    }
    
}
