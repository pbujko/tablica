/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.ArrayList;
import java.util.List;
import net.bujko.tablica.be.categs.Node;

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
public class Category {

//    @Id
//    @Column(name="cat_id")
    private String id;
//    @Column
    private String label, code;
    private int depth;
//    @Transient
    final private List<Category> childCategories = new ArrayList<Category>();
//    @Transient
    private Category parent;

    public Category() {
    }

    public Category(String id) {
        this.id = id;
    }

    public Category(Node n) {
        this.label = n.getLabel();
        this.id = n.getId();
        this.code = n.getCode();
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void addChild(Category c) {
        this.childCategories.add(c);

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
}
