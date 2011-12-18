/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.HashSet;
import java.util.Set;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.persistence.Transient;

/**
 *
 * @author pbujko
 */
//@Entity
//@Table(name = "ad")
public class Ad {

    private Set<Category> assignedCategories = new HashSet<Category>();
    private String description, id, hashedId, title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    @Column(name = "ad_hashed_id", unique = true, nullable = false, length = 50)
    public String getHashedId() {
        return hashedId;
    }

    public void setHashedId(String hashedId) {
        this.hashedId = hashedId;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ad_id", unique = true, nullable = false, columnDefinition = "int")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @Column(name = "ad_title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addCategory(Category c) {
        assignedCategories.add(c);
    }

    public void setAssignedCategories(Set<Category> ac) {
        assignedCategories = ac;
    }

//    @OneToMany
//    @JoinTable(
//            name="ad_categs",
//            joinColumns = @JoinColumn( name="ad_id"),
//            inverseJoinColumns = @JoinColumn( name="cat_id")
//    )
    public Set<Category> getAssignedCategories() {
        return assignedCategories;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ad other = (Ad) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
