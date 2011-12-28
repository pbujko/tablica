/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.Collection;
import java.util.HashMap;
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
    private CityEntity city;
//    private Collection<AttributeEntity> atts = new HashSet<AttributeEntity>();
    /**
     * jakie opcje zostaly wybrane z atrybutow
     * moze byc puste kiedy nie wybrano zadnego atrybutu
     * format mapy:
     * entityAttributeId -> choiceId
     * 
     * entityAttributeId musi byc unikalny poniewaz dozwolony tylko jeden choice z danego atrybutu
     * 
     */
    private Map<String, String> choices = new HashMap<String, String>();

    public Ad() {
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

    public void addChoices(Map<String, String> aChoices) throws Exception{
        if(aChoices == null)
            return;
        
        for(String k: aChoices.keySet()){
            addChoice(k, aChoices.get(k));
        }
    }
    
    public void addChoice(String attId, String choiceId) throws Exception {
        if (this.choices.containsKey(attId)) {
            throw new Exception("ATTR_ALREADY_ASSIGND: " + attId);
        } else {
            this.choices.put(attId, choiceId);
        }
    }

    public Map<String, String> getChoices() {
        return this.choices;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
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
        return String.format("[%s, id: %s, %s]", this.getClass().getName(), this.getId() != null ? this.getId() : "<noid>", this.category != null ? this.category : "<nocat>");
    }
}
