package tablica

class SearchController {
    def searchDao
    def categoryManager
    
    def ATTS_SEPARATOR='-'
    def ATTS_KEYVAL_SEPARATOR='.'
    def index() {
        print "search: ${params}"

        def choicesSelected = [:]
        if(params.atts){

            params.atts.tokenize(ATTS_SEPARATOR).each(){
                println "${it},"
                def attChoiceCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[1]
                def attCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[0]
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
            attsUrl = attsUrl + ATTS_SEPARATOR+"${it.key.substring(4)}${ATTS_KEYVAL_SEPARATOR}${it.value}"
        }
        if(attsUrl)
        attsUrl=attsUrl.substring(1)
        log.info(">>${attsUrl}<<")
        
        redirect(mapping:'search', params:[code:params.catCode, atts:attsUrl])
    }    
}
