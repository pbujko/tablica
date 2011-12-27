package tablica

class AdService {
 def adDao
    def listRecent(from, limit) {
        adDao.listRecent(from, limit)
    }
    
    def getTotalNumberOfRecent(){
        adDao.listRecent(0, 1000).size()
    }
}
