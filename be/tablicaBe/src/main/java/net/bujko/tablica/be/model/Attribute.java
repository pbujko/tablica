/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.model;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author pbujko
 */
public class Attribute {
    private Collection<AttributeChoice> choices = new HashSet<AttributeChoice>();
    private String id, label;
    Category parentCat;
}
