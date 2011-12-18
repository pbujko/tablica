/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.bujko.tablica.be.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("categoryDao")

public class CategoryDaoImpl implements CategoryDao {
    
    @Autowired
    DataSource dataSource;
    
    @Override
    public void save(Category c) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into category(cat_id, label) values(?, ?)");
        ps.setString(1, c.getId());
        ps.setString(2, c.getLabel());
        ps.executeUpdate();
        conn.close();
        
    }
    
    @Override
    public void update(Category c) throws SQLException {
        delete(c);
        save(c);
    }
    
    @Override
    public void saveOrUpdate(Category c) throws Exception {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select count(*) as cc from category where cat_id = ?");
        ps.setString(1, c.getId());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int found = rs.getInt("cc");
        if (found == 0) {
            save(c);
        } else if (found == 1) {

            //update
            update(c);
        } else {
            throw new Exception("found " + found);
        }
        
        conn.close();
    }
    
    @Override
    public void delete(Category c) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from category where cat_id=?");
        ps.setString(1, c.getId());
        ps.executeUpdate();
        conn.close();
    }
    
    @Override
    public List<Category> getAll() throws SQLException {
        Connection conn = dataSource.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("select * from category");
        
        List<Category> retL = new ArrayList<Category>();
        while (rs.next()) {
            Category c = new Category(rs.getString("cat_id"));
            c.setLabel(rs.getString("label"));
            retL.add(c);
        }
        
        conn.close();
        return retL;
    }
//    @Autowired
//    @Qualifier("mySessionFactory")
//    SessionFactory sf;
//
//    @Override
//    public void save(Category c) {
//        sf.getCurrentSession().save(c);
//    }
//
//    @Override
//    public void update(Category c) {
//        sf.getCurrentSession().saveOrUpdate(c);
//    }
//
//    @Override
//    public void saveOrUpdate(Category c) {
//        sf.getCurrentSession().saveOrUpdate(c);
//    }
//
//    @Override
//    public void delete(Category c) {
//        sf.getCurrentSession().delete(c);
//    }
//
//    @Override
//    public List<Category> getAll() {
//        return sf.getCurrentSession().createQuery("from " + Category.class.getName()).list();
//    }
//    @Override
//    public void save(Category c) {
//        getHibernateTemplate().save(c);
//    }
//    
//    @Override
//    public void update(Category c) {
////        getHibernateTemplate().saveOrUpdate(c);
//    }
//    
//    @Override
//    public void delete(Category c) {
//        getHibernateTemplate().delete(c);
//    }
//    
//    @Override
//        @Transactional(readOnly=false)
//    public void saveOrUpdate(Category c) {
//        getHibernateTemplate().saveOrUpdate(c);        
//    }
//    
//    @Override
//    public List<Category> getAll() {
//        return getHibernateTemplate().loadAll(Category.class);
//    }
}
