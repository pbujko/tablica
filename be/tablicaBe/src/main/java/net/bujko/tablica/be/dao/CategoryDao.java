/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.util.List;
import net.bujko.tablica.be.model.Category;

/**
 *
 * @author pbujko
 */
public interface CategoryDao {
        void save(Category c);

    void update(Category c);
    
    void saveOrUpdate(Category c);

    void delete(Category c);
    
   List<Category> getAll();
   
}
