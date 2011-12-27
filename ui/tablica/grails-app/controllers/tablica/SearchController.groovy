package tablica

class SearchController {
    def searchDao
    def categoryManager
    
    def ATTS_SEPARATOR='-'
    def ATTS_KEYVAL_SEPARATOR='.'
    def index() {
        print "search: ${params}"

        def searchQParams=[:]
        
        def choicesSelected = [:]
        if(params.atts){

            params.atts.tokenize(ATTS_SEPARATOR).each(){
                                
                def attChoiceCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[1]
                def attCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[0]
                def attChoice = categoryManager.getChoiceByCode(attChoiceCode)
                def tmpAtt = categoryManager.getAttributeByCode(attCode)
                
                choicesSelected[attCode] = attChoice
                searchQParams['attChoice'+attCode]=  tmpAtt.id+"|"+attChoice.id                       
                                
            }

        }        
        params['choicesSelected'] = choicesSelected
        
        def searchCat = categoryManager.getCategoryByCode(params.code)        
        searchQParams.cat = searchCat.id

        if(params.q){
            searchQParams.phrase=params.q
        }
        
        def searchQ = searchDao.buildQuery(searchQParams)        
        println searchQ
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
        
        if(params.q)        
        redirect(mapping:'search', params:[code:params.catCode, atts:attsUrl, q:params.q])
        else
        redirect(mapping:'search', params:[code:params.catCode, atts:attsUrl])
    }    
}
