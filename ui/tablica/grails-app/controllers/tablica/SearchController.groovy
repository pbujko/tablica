package tablica

class SearchController {
    def searchDao
    def categoryManager
    
    def ATTS_SEPARATOR='.'
    def index() {
        print "search: ${params}"

        def choicesSelected = [:]
        if(params.atts){

            params.atts.tokenize(ATTS_SEPARATOR).each(){
                println "${it},"
                def attChoiceCode = it.tokenize('-')[1]
                def attCode = it.tokenize('-')[0]
                def attChoice = categoryManager.getChoiceByCode(attChoiceCode) 
                print "${attChoiceCode} -> ${attChoice} ,${attCode}"
                choicesSelected[attCode] = attChoice
            }

        }        
        params['choicesSelected'] = choicesSelected
        def searchCat = categoryManager.getCategoryByCode(params.code)
        def searchQ = searchDao.buildQuery("cat":searchCat.id)        
        
        [searchCat:searchCat, res:searchDao.search(searchQ)]
    }
    
    def redir() {    
        println "redir ${params}"
        
        def attsUrl = ''
        params.findAll(){            
            it.key.startsWith('att|') && it.value            
        }.each(){            
            attsUrl = attsUrl + ATTS_SEPARATOR+"${it.key.substring(4)}-${it.value}"
        }
        attsUrl=attsUrl.substring(1)
        log.info(">>${attsUrl}<<")
        
        redirect(mapping:'search', params:[topCat:params.topCat, code:params.catCode, atts:attsUrl])
    }    
}
