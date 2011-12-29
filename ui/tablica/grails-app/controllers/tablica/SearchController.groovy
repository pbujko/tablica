package tablica

class SearchController {
    def searchDao
    def categoryManager
    def adService        

    
    def ATTS_SEPARATOR='.'
    def ATTS_KEYVAL_SEPARATOR='-'
    def ATTS_CITY='city'
    
    def index() {
        print "search: ${params}"

        def searchQParams=[:]
        
        def choicesSelected = [:]
        def selectedCityCode
        if(params.atts){

            params.atts.tokenize(ATTS_SEPARATOR).each(){
                                                
                def attChoiceCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[1]
                def attCode = it.tokenize(ATTS_KEYVAL_SEPARATOR)[0]
                if(attCode == ATTS_CITY){                    
                    searchQParams['city']=categoryManager.getCityByCode(attChoiceCode).id
                    selectedCityCode=attChoiceCode
                }                
                else{
                
                    def attChoice = categoryManager.getChoiceByCode(attChoiceCode)
                    def tmpAtt = categoryManager.getAttributeByCode(attCode)
                
                    choicesSelected[attCode] = attChoice
                    searchQParams['attChoice'+attCode]= tmpAtt.id+"|"+attChoice.id                       
                }           
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
       
        [searchCat:searchCat, res:searchDao.search(searchQ), hideSearch:"aa", allCities:categoryManager.getAllCities(), citySelected:selectedCityCode]
    }

    def byPhrase(){
        def searchQParams=[:]

        if(params.q){
            searchQParams.phrase=params.q
        }
        
        def searchQ = searchDao.buildQuery(searchQParams)        
        println searchQ
        def res=searchDao.search(searchQ);
        [res:res, cats:searchDao.extractCategories(res)]
        
    }
    
    def recent(){
        def offset =  params.offset? params.offset as int:0
        
        [res:adService.listRecent(offset,5), total:adService.getTotalNumberOfRecent()]
    }
    
    def redir() {    
        println "redir ${params}"
        
        def attsUrl = ''
        params.findAll(){            
            it.key.startsWith('att|') && it.value            
        }.each(){            
            attsUrl = attsUrl + ATTS_SEPARATOR+"${it.key.substring(4)}${ATTS_KEYVAL_SEPARATOR}${it.value}"
        }
        
        if(params.city != "0")
        attsUrl = attsUrl + ATTS_SEPARATOR + ATTS_CITY +ATTS_KEYVAL_SEPARATOR + categoryManager.getCityById(params.city).code
        
        if(attsUrl)
        attsUrl=attsUrl.substring(1)
        log.info(">>${attsUrl}<<")
        
        if(params.q)        
        redirect(mapping:'search', params:[code:params.catCode, atts:attsUrl, q:params.q])
        else
        redirect(mapping:'search', params:[code:params.catCode, atts:attsUrl])
    }    
}
