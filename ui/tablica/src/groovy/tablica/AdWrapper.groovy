/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tablica

import grails.validation.Validateable
import net.bujko.tablica.be.model.Ad

@Validateable
class AdWrapper {
    String title
    String description
    String categoryId
    String email, city
    Integer price
    static constraints = {
        title blank: false
        email email: true, blank: false
        description blank:false
        categoryId blank:false
        price blank:false
        city blank:false
    }
        
    public AdWrapper(params){
    
        if(params.title)
        title=params.title
        
        if(params.email)
           email = params.email
           
        if(params.description)
        description=params.description
   
        if(params.category)
        categoryId=params.category
        
        if(params.price)
        price = new Integer(params.price)
        
        if(params.city)
        city = params.city
    } 
    
    
    
}
