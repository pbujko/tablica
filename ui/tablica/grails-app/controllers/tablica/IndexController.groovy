package tablica

class IndexController {
    def categoryManager
    
    def index = {

        [categoryManager:categoryManager]
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

