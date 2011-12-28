/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tablica

import grails.validation.Validateable

@Validateable
class AdWrapper {
    String title
    String description
    String categoryId
    String email
    static constraints = {
        title blank: false
        email email: true, blank: false
    }
        
    public AdWrapper(title, email){
        this.title=title
        this.email = email        
        
    }    
    
    public void setTitle(String t){
        this.title=title            
        
    }
        
    public void setEmail(String email){
        this.email=email
    }

}
