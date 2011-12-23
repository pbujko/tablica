/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.Collection;
import java.util.HashSet;
import net.bujko.tablica.be.categs.binding.atts.Choice;

/**
 * Wrapper - mapuje bindingAttribute do peniejszego obiektu AttributeEntity.
 * Zastosowanie wrappera uniezaleznia od XMLa w przyszlosci.
 * @author pbujko
 */
public class AttributeEntity {

    private Collection<AttributeChoiceEntity> choices = new HashSet<AttributeChoiceEntity>();
    private String id, label, code;
    Category parentCat;

    public AttributeEntity(String id) {
        this.id = id;
    }

    public AttributeEntity(net.bujko.tablica.be.categs.binding.atts.Attribute bindingAtt) throws Exception {
        this.label = bindingAtt.getLabel();
        this.id = bindingAtt.getId();
        this.code = bindingAtt.getCode();

        for (Choice tmpChoice : bindingAtt.getChoice()) {
            AttributeChoiceEntity ace = new AttributeChoiceEntity(tmpChoice);
            if (this.choices.contains(ace)) {
                throw new Exception(String.format("DUPLICATED_ATTCHOICE: %s, att: %s", ace, this));
            }
            this.choices.add(ace);
        }
    }

    public Collection<AttributeChoiceEntity> getChoices() {
        return choices;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Category getParentCat() {
        return parentCat;
    }

    public void setParentCat(Category parentCat) {
        this.parentCat = parentCat;
    }

    public String getCode() {
        return code;
    }
        
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AttributeEntity other = (AttributeEntity) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Attribute{" + "id=" + id + ", label=" + label + '}';
    }
}
