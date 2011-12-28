package tablica

class AdController {
    def adDao

    def show() {
    println grails.util.GrailsUtil.getEnvironment()
        def ad = adDao.findById(params.id)

        if(!ad)
            render(view: "noAd")
            
        [ad: ad]
    }
    
    
    
    def editView(){}
    
    def create(){}
    def save(){    
        println params
        AdWrapper adW = new AdWrapper(params.title, params.email)
        println adW
        if(adW.validate())
        render "ok"
        else 
       { render "boo "
          adW.errors.allErrors.each {
        println it
    }
        }
       
    }
}
