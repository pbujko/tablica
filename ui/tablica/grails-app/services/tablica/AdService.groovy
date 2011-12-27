package tablica

class AdService {
 def adDao
    def listRecent(from, limit) {
        adDao.listRecent(from, limit)
    }
}
