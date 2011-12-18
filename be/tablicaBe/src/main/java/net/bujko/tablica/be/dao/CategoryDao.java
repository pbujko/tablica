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

    void save(Category c) throws Exception;

    void update(Category c) throws Exception;

    void saveOrUpdate(Category c) throws Exception;

    void delete(Category c) throws Exception;

    List<Category> getAll() throws Exception;
}
