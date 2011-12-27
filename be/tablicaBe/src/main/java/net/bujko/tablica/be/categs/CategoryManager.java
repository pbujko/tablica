/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.categs;

import net.bujko.tablica.be.categs.binding.categs.AllNodes;
import net.bujko.tablica.be.categs.binding.categs.Node;
import java.util.Collection;
import net.bujko.tablica.be.model.Category;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.bujko.tablica.be.categs.binding.atts.AllAtts;
import net.bujko.tablica.be.categs.binding.atts.Attribute;
import net.bujko.tablica.be.dao.CategoryDao;
import net.bujko.tablica.be.model.AttributeChoiceEntity;
import net.bujko.tablica.be.model.AttributeEntity;
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
    // catId -> Category

    private Map<String, Category> allCategsById;
    // catCode -> Category
    private Map<String, Category> allCategsByCode;
    private Set<Category> topLevelCats = new TreeSet<Category>();
    // attributeId -> attributeEntity
    private Map<String, AttributeEntity> allAttEntityById;
    private Map<String, AttributeChoiceEntity> allAttChoiceEntById;
    Logger logger = LoggerFactory.getLogger(CategoryManager.class);
    @Autowired
    CategoryDao categoryDao;

    public void init() throws Exception {

        allCategsById = new HashMap<String, Category>();
        allCategsByCode = new HashMap<String, Category>();
        allAttEntityById = new HashMap<String, AttributeEntity>();
        allAttChoiceEntById = new HashMap<String, AttributeChoiceEntity>();
        JAXBContext context = JAXBContext.newInstance(
                "net.bujko.tablica.be.categs.binding.categs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        //load nodes from config, build in memory Category tree
        //AllNodes allnodes = (AllNodes) unmarshaller.unmarshal(new FileReader("/Users/pbujko/NetBeansProjects/tablica/be/tablicaBe/src/test/resources/nodesData.xml"));
        AllNodes allnodes = (AllNodes) unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("nodesData.xml"));
        List<Node> listOfNodes = allnodes.getNode();

        for (Node node : listOfNodes) {
            processNode(null, node);
        }

        for (Category c : allCategsById.values()) {
            categoryDao.saveOrUpdate(c);
            if (c.getParent() == null) {
                topLevelCats.add(c);
            }
        }


        context = JAXBContext.newInstance(
                "net.bujko.tablica.be.categs.binding.atts");
        unmarshaller = context.createUnmarshaller();
        AllAtts allAtts = (AllAtts) unmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("attributes.xml"));

        for (Attribute tmpA : allAtts.getAttribute()) {

            AttributeEntity ae = new AttributeEntity(tmpA);
            logger.trace("processing {}", ae);
            logger.trace("allAttEntityById is: {}", allAttEntityById);
            if (allAttEntityById.containsValue(ae)) {
                throw new Exception("DUPLICATED_ATTENT: " + ae);
            }

            for (AttributeChoiceEntity ache : ae.getChoices()) {
                if (this.allAttChoiceEntById.containsValue(ache)) {
                    throw new Exception("DUPL_ATTCHOENT: " + ache);
                }
                allAttChoiceEntById.put(ache.getId(), ache);
            }

            allAttEntityById.put(ae.getId(), ae);

            Category tmpC = this.allCategsById.get(tmpA.getCatId());
            if (tmpC == null) {
                throw new Exception("UNKATTRCAT: " + tmpA.getCatId());
            }
            ae.setParentCat(tmpC);
            ae.getParentCat().addAttribute(ae);
            logger.trace("adding to allAttEntityById: {}", ae);
        }

        logger.info("{} is up. Categories loaded: {}", this.getClass().getName(), allCategsById.size());
    }

    private void processNode(Category parentC, Node n) throws Exception {

        Category c = new Category(n);

        if (allCategsById.containsKey(c.getId())) {
            throw new Exception("DUPLICATED_CATID: " + c.getId());
        }

        if (allCategsByCode.containsKey(c.getCode())) {
            throw new Exception("DUPLICATED_CATCODE: " + c.getCode());
        }

        if (parentC != null) {
            c.setParent(parentC);
            c.setDepth(parentC.getDepth() + 1);
            parentC.addChild(c);
        } else {
            c.setDepth(1);
        }

        allCategsById.put(c.getId(), c);
        allCategsByCode.put(c.getCode(), c);

        for (Node tmpN : n.getNode()) {
            processNode(c, tmpN);
        }
    }

    public Collection<Category> getAllCategories() {
        return this.allCategsById.values();
    }

    public int getCategoryCount() {
        return this.allCategsById.size();
    }

    public Category getCategoryByCode(String code) {
        return this.allCategsByCode.get(code);
    }

    public Category getCategoryById(String id) {
        return this.allCategsById.get(id);
    }

    public Category getParentCategory(String id) {

        Category c = allCategsById.get(id);
        if (c != null) {
            return c.getParent();
        } else {
            return null;
        }
    }

    public List<Category> getChildCategories(String id) {

        Category c = allCategsById.get(id);
        if (c != null) {
            return c.getChildCategories();
        } else {
            return null;
        }
    }

    public Set<Category> getTopLevelCategories() {

        return topLevelCats;
    }

    /**
     * depth - top level is level 1
     * @param depth
     * @return 
     */
    public Set<Category> getCatsByDepth(int depth) {
        Set<Category> retS = new HashSet<Category>();

        for (Category c : allCategsById.values()) {

            if (c.getDepth() == depth) {
                retS.add(c);
            }
        }

        return retS;
    }

    public AttributeChoiceEntity getChoiceById(String choiceId) {
        return this.allAttChoiceEntById.get(choiceId);
    }

    public AttributeChoiceEntity getChoiceByCode(String choiceCode) {
        for (AttributeChoiceEntity ch : this.allAttChoiceEntById.values()) {
            if (ch.getCode().equals(choiceCode)) {
                return ch;
            }
        }

        return null;
    }

    public AttributeEntity getAttributeByCode(String code) {
        for (AttributeEntity ea : this.allAttEntityById.values()) {
            if (ea.getCode().equals(code)) {
                return ea;
            }
        }
        return null;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
