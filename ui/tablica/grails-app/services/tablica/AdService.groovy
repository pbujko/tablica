package tablica


class AdService {
    def adDao
    def categoryManager
    def searchDao
    def messengerService
    
    def listRecent(from, limit) {
        adDao.listRecent(from, limit)
    }
    
    def getTotalNumberOfRecent(){
        adDao.listRecent(0, 1000).size()
    }
    
    def save(params){
  
        def hashedId = UUID.randomUUID().toString()
        //create Ad object
        log.debug "save - params: ${params}"
        net.bujko.tablica.be.model.Ad ad = new net.bujko.tablica.be.model.Ad()
        ad.setTitle(params.title)
        ad.setDescription(params.description)
        ad.setHashedId(hashedId)
        ad.addCategory(categoryManager.getCategoryById(params.category))
        ad.setCity(categoryManager.getCityById(params.city))
        ad.setPrice(params.price)
        if(params.phone)
        ad.setPhone(params.phone)
        ad.setEmail(params.email)    
        
        //add images
        AdImage.findAllByHashedId(params.imguuid).each{
            ad.addImageId(it.id.toString())
        }
        
        //attributes (choices)
        params.findAll(){            
            it.key.startsWith('att|') && it.value
        }.each(){            
            log.debug "att: ${it.key.substring(4)}  -> ${it.value}"
            ad.addChoice(it.key.substring(4), it.value)
        }
        
        
        
        //save to AdDao
        log.debug "saving ad ${ad}"        
        adDao.save(ad)
        log.info "ad persisted, id: ${ad.id}"
        
        //save to searchDao
        searchDao.add(ad)
        log.info "ad added to lucene"
        
        def meta = [:]
        meta.adId = ad.id        
        def m=new Message(msgTo:ad.email, meta:meta.encodeAsJSON(), type:"ACTIVATE")
        if (m.save()) {                    
            messengerService.send(m)
        }
        
        log.debug "activation email scheduled for delivery"
        
        return ad
    }
    
//    
//    def encodeAd(ad){
//        if(ad)      
//        return new String( (ad.id + ad.hashedId).encodeAsMD5().encodeAsBase64() ) 
//        else
//        return ""
//    }
//    
//    def decodeAd(ad, encodedAd){
//        return encodeAd(ad) == encodedAd       
//    }
}
