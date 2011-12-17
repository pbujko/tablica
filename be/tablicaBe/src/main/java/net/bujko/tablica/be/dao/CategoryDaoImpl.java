/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.List;
import net.bujko.tablica.be.model.Category;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("categoryDao")
public class CategoryDaoImpl extends CustomHibernateDaoSupport implements CategoryDao {
    
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
        getHibernateTemplate().delete(c);
    }
    
    @Override
    public void saveOrUpdate(Category c) {
        getHibernateTemplate().saveOrUpdate(c);
    }
    
    @Override
    public List<Category> getAll() {
        return getHibernateTemplate().loadAll(Category.class);
    }
}
