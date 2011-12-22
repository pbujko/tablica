/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pbujko
 */
public class Ad {

    //private Set<Category> assignedCategories = new HashSet<Category>();
    private Category category;
    private String description, id, hashedId, title;

    public Ad(){
    }
    
    public Ad(Map<String, String> m){
        if(m.containsKey("description"))
            setDescription(m.get("description"));
    }
    
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashedId() {
        return hashedId;
    }

    public void setHashedId(String hashedId) {
        this.hashedId = hashedId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * dodany limit - przypisujemy tylko jedna kategorie
     * @param c
     * @throws Exception 
     */
    public void addCategory(Category c) throws Exception {
        if (c == null) {
            throw new Exception("CATNLL");
        }
//        if (!assignedCategories.isEmpty()) {
//            throw new Exception("CAT_ALREADY_ASSIGNED: " + assignedCategories);
//        }
//        assignedCategories.add(c);
        if (this.category != null) {
            throw new Exception("CAT_ALREADY_ASSIGNED: " + this.category);
        } else {
            this.category = c;
        }

    }

    /**
     * zwraca kategorie wybrana ale rowniez wszystkich starszych z drzewa az do roota.
     * @param ac 
     */
    public Set<Category> getAssignedCategories() {
        Set<Category> retS = new HashSet<Category>();

        if (this.category != null) {
            this.category.collectParent(retS);
        }

        return retS;
    }

    public Category getCategory() {
        return this.category;
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

    @Override
    public String toString() {
        return String.format("[%s, id: %s, %s]", this.getClass().getName(), this.getId(), this.category.toString());
    }
}
