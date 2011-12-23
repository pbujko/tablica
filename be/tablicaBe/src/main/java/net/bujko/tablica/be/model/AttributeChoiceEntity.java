/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import net.bujko.tablica.be.categs.binding.atts.Choice;

/**
 *
 * @author pbujko
 */
public class AttributeChoiceEntity {

    private String id, label, code;

    AttributeChoiceEntity(Choice choice) {
        this.id = choice.getId();
        this.label = choice.getLabel();
        this.code = choice.getCode();
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

    public String getCode() {
        if(code==null || code.isEmpty())
            return this.label.toLowerCase().replace(' ', '_');
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
        final AttributeChoiceEntity other = (AttributeChoiceEntity) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "AttributeChoiceEntity{" + "id=" + id + '}';
    }
}
