/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.bujko.tablica.be.city.binding.City;

/**
 *
 * @author pbujko
 */
public class CityEntity {
    
    String id, code, label;
    Collection<CityEntity> childCities = new HashSet<CityEntity>();
    CityEntity parentCityEntity;
    
    public CityEntity(String id) {
        this.id = id;
    }
    
    public CityEntity(City c) {
        this.code = c.getCode();
        this.id = c.getId();
        this.label = c.getLabel();
        
    }
    
    public String getCode() {
        if (code == null || code.isEmpty()) {
            return label.toLowerCase().replace(' ', '_');
        } else {
            return code;
        }
    }
    
    public void setCode(String code) {
        this.code = code;
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
    
    public CityEntity getParentCityEntity() {
        return parentCityEntity;
    }
    
    public Collection<CityEntity> getAncestors() {
        Set<CityEntity> memo = new HashSet<CityEntity>();
        collectParentCity(memo);
        return memo;
    }
    
    private void collectParentCity(Collection memo) {
        if (this.parentCityEntity != null) {
            memo.add(parentCityEntity);
            parentCityEntity.collectParentCity(memo);
        }
    }
    
    public void setParentCityEntity(CityEntity parentCityEntity) {
        this.parentCityEntity = parentCityEntity;
    }
    
    public void addChildCity(CityEntity ce) {
        this.childCities.add(ce);
    }
    
    public Collection getChildCities() {
        return this.childCities;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CityEntity other = (CityEntity) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "CityEntity{" + "id=" + id + ", code=" + code + ", label=" + label + '}';
    }
}
