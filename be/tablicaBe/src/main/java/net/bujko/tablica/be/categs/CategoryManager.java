/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.categs;

import net.bujko.tablica.be.model.Category;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.bujko.tablica.be.dao.CategoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pbujko
 */
@Service
public class CategoryManager implements InitializingBean {

    private Map<String, Category> flattened;
    private Set<Category> topLevelCats = new HashSet<Category>();
    Logger logger = LoggerFactory.getLogger(CategoryManager.class);
    @Autowired
    CategoryDao categoryDao;

    public void init() throws Exception {
        
        
        flattened  = new HashMap<String, Category>();

        JAXBContext context = JAXBContext.newInstance(
                "net.bujko.tablica.be.categs");
        Unmarshaller unmarshaller = context.createUnmarshaller();


        //load nodes from config, build in memory Category tree
        //AllNodes allnodes = (AllNodes) unmarshaller.unmarshal(new FileReader("/Users/pbujko/NetBeansProjects/tablica/be/tablicaBe/src/test/resources/nodesData.xml"));
        AllNodes allnodes = (AllNodes) unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("nodesData.xml"));
        List<Node> listOfNodes = allnodes.getNode();

        for (Node node : listOfNodes) {
            processNode(null, node);
        }

        for (Category c : flattened.values()) {
            categoryDao.saveOrUpdate(c);
            if (c.getParent() == null) {
                topLevelCats.add(c);
            }
        }

        logger.info("{} is up. Categories loaded: {}", this.getClass().getName(), flattened.size());
    }

    private void processNode(Category parentC, Node n) throws Exception {

        Category c = new Category(n);

        if (flattened.containsKey(c.getId())) {
            throw new Exception("DUPLICATED_CATEGORY: " + c.getId());
        }

        if (parentC != null) {
            c.setParent(parentC);
            parentC.addChild(c);
        }

        flattened.put(c.getId(), c);

        for (Node tmpN : n.getNode()) {
            processNode(c, tmpN);
        }
    }

    public int getCategoryCount() {

        return this.flattened.size();
    }

    public Category getCategoryById(String id) {

        return this.flattened.get(id);
    }

    public Category getParentCategory(String id) {


        Category c = flattened.get(id);
        if (c != null) {
            return c.getParent();
        } else {
            return null;
        }
    }

    public List<Category> getChildCategories(String id) {

        Category c = flattened.get(id);
        if (c != null) {
            return c.getChildCategories();
        } else {
            return null;
        }
    }

    public Set<Category> getTopLevelCategories() {

        return topLevelCats;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
