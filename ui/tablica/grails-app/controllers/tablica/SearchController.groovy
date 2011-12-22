package tablica

class SearchController {
    def searchDao
    def categoryManager
    def index() {
        print "search ${params}"
        def searchCat = categoryManager.getCategoryByCode(params.code)
        def searchQ = searchDao.buildQuery("cat":searchCat.id)
                
        [searchCat:searchCat, res:searchDao.search(searchQ)]
    }
}
