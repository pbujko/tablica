/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.bujko.tablica.be.categs.binding.categs.Node;

/**
 * klasa owija Node, AllNodes ktore sa generowane automatycznie przez JAXB.
 * ta klasa tutaj ma lepsza konwencje nazewnicza itp.
 * 
 * przyklad: kategoria:
 * 
 * Label = Auto i moto
 * Id: (jakis kod)
 * Code: url friendly wersja labela -> auto-i-moto 
 *
 * @author pbujko
 */
public class Category implements Comparable {

    private String id;
    private String label, code, order = "999";
    private int depth;
    final private List<Category> childCategories = new ArrayList<Category>();
    private Category parent;
    final private Collection<AttributeEntity> atts = new HashSet<AttributeEntity>();

    public Category() {
    }

    public Category(String id) {
        this.id = id;
    }

    public Category(Node n) {
        this.label = n.getLabel();
        this.id = n.getId();
        this.code = n.getCode();
        if (n.getOrder() != null) {
            this.order = n.getOrder();
        }
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void addChild(Category c) {
        this.childCategories.add(c);
        Collections.sort(childCategories);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Category getParent() {
        return parent;
    }

    void collectParent(Set memo) {
        memo.add(this);
        if (parent != null) {
            parent.collectParent(memo);
        }

    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int level) {
        this.depth = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void addAttribute(AttributeEntity a) throws Exception {
        if (atts.contains(a)) {
            throw new Exception(String.format("DUPLATTR: {}, {}", a, this));
        }
        atts.add(a);
    }
    
    public Collection<AttributeEntity> getAttributes(){
        return atts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", label=" + label + '}';
    }

    @Override
    public int compareTo(Object t) {
        if (t == null) {
            return -1;
        }

        if (!(t instanceof Category)) {
            return -1;
        }

        if (this.getLabel() == null || ((Category) t).getLabel() == null) {
            return -1;
        }

        int retVal = 0;
        retVal = this.order.compareToIgnoreCase(((Category) t).order);
        if (retVal != 0) {
            return retVal;
        }

        return this.getLabel().compareToIgnoreCase(((Category) t).getLabel());
    }
}
