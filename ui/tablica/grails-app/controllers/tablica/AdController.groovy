package tablica

class AdController {
    def adDao
    def adService      
    def categoryManager
    def searchDao
    
    def simpleCaptchaService
    def messengerService

    def show() {
        println grails.util.GrailsUtil.getEnvironment()
        def ad = adDao.findById(params.id)

        if(!ad)
        render(view: "noAd")
        else if(!ad.active)
        render(view: "adNotActive", model:[ad:ad])
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
        
        def ad = adDao.findById("1")
        def encoded = adService.encodeAd(ad)
        println "encoded: ${encoded}"
        
        println adService.decodeAd(ad, encoded)
        println adService.decodeAd(adDao.findById("3"), encoded)       
        println adService.decodeAd(adDao.findById("2000"), encoded)        
    }
    
    def postPv(){

        boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)   
        log.debug "captcha ${captchaValid}"
//        captchaValid=true
        if(captchaValid){
            
            def meta = [:]
            meta.msgBody = params.msgBody
            meta.sender = params.senderEmail
            meta.adId = params.id
            def ad = adDao.findById(params.id)
            if(ad){
                def m=new Message(msgTo:ad.email, meta:meta.encodeAsJSON(), type:"PW")
                if (m.save(flush:true)) {                    
                    messengerService.send(m)
                    render "ok"    
                }
                else
                log.error "POSTPV: ${m.errors}"                            
            }                                
        }                
    }
    
    def activate(){
        def verified
        def ad
        if(params.id.isInteger()){        
            ad = adDao.findById(params.id)
            verified = adService.decodeAd(ad, params.k)
            //verified=true
        }
 
        if(verified){
            //do activate
            render(view:"activated", model:[ad:ad])
        }else
        
        render(view:"activationFailed")

    }
}
