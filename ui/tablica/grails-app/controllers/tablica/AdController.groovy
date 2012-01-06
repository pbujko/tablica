package tablica

class AdController {
    def adDao
    def adService      
    def categoryManager
    def searchDao
    

    def show() {
        println grails.util.GrailsUtil.getEnvironment()
        def ad = adDao.findById(params.id)

        if(!ad)
        render(view: "noAd")
            
        [ad: ad]
    }
    
    
    
    def editView(){}
    
    def create(){        
        [imguuid:UUID.randomUUID().toString(), topLevelCats:categoryManager.getTopLevelCategories(), allCities:categoryManager.getAllCities()]
    }
    
    def save(){    
        println "ad save ${params}"
        AdWrapper adW = new AdWrapper(params)        
        try{
            if(adW.validate()){
                def retAd = adService.save(params)    
                servletContext["searchStats"] = searchDao.summary
                render "${retAd?.id}"           
            }            
            else {
                throw new Exception("VALIDATE")
            }                        
        }
        catch(e){
            log.error "save: ${e}"
            
            response.status=500
            render "${e.message}"            
        }                
       
    }
    
        def test(){
        
        print params
        render(view:'test', model:[params:params])
    }
}
