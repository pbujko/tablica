package tablica

class Message {
    
    static constraints = {
        msgTo email: true, blank: false
        type blank: false
        sent nullable:true       
    }
    
    String msgTo
    String meta
    String type
    Date dateCreated
    Date sent
               
}

