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
    
    def redir() {    
        println params
        
        def attsUrl = ''
        params.findAll(){            
            it.key.startsWith('att|') && it.value            
        }.each(){            
            attsUrl = attsUrl + "${it.key.substring(4)}-${it.value}|"
        }
        println ">>${attsUrl}<<"
        
        redirect(mapping:'search', params:[topCat:params.topCat, code:params.catCode, otherParams:attsUrl])
    }    
}
