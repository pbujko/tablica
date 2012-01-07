package tablica

class Message {
    
    static constraints = {
        msgTo email: true, blank: false
        type blank: false        
    }
    
    String msgTo
    String meta
    String type
    Date dateCreated
    Date lastUpdated
    
        
    
}

