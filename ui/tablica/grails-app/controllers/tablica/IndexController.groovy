package tablica

class IndexController {
    def categoryManager
    def adService
    def index = {

        [categoryManager:categoryManager, recentAds:adService.listRecent(0,2)]
    }
    
    def secLevel={
        println "${params}"
        def parentCat = categoryManager.getCategoryByCode(params.parentCode)
        [
            parentCat:parentCat, 
            cats:categoryManager.getChildCategories(parentCat.id)
        ]            
    }
}

