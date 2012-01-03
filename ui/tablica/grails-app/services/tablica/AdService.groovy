package tablica

class AdService {
    def adDao
    def categoryManager
    def searchDao
    
    def listRecent(from, limit) {
        adDao.listRecent(from, limit)
    }
    
    def getTotalNumberOfRecent(){
        adDao.listRecent(0, 1000).size()
    }
    
    def save(params){
  
        def hashedId = UUID.randomUUID().toString()
        //create Ad object
        
        net.bujko.tablica.be.model.Ad ad = new net.bujko.tablica.be.model.Ad()
        ad.setTitle(params.title)
        ad.setDescription(params.description)
        ad.setHashedId(hashedId)
        ad.addCategory(categoryManager.getCategoryById(params.category))
        ad.setCity(categoryManager.getCityById(params.city))
        ad.setPrice(params.price)
        
        //save to AdDao
        log.debug "saving ad ${ad}"        
        adDao.save(ad)
        log.info "ad persisted, id: ${ad.id}"
        
        //save to searchDao
        searchDao.add(ad)
        log.info "ad added to lucene"
        
        return ad
    }
}
