package tablica

class FragmentsController {
    def categoryManager
    def adDao
    
    def index() { }
    
    def listCatsById(){
        if(params.cId)
        render(template: "subcatsDropDownTemplate", model: [cats: categoryManager.getCategoryById(params.cId).getChildCategories()])
        else
        render ""
    }
    
    def renderAttsForCat(){
        def atts =  categoryManager.getCategoryById(params.catId)?.getAttributes()
        render(template:'attsForCatDropDown', model:[atts:atts])
      //render atts
    }
    
    
    def showAdContact(){
        def ad = adDao.findByIdAndHashedId(params.id, params.hashedId)
        render ad?.phone
        
    }
    
    
    def showAdContactMsgForm(){
        
        render "tbd, showAdContactMsgForm"
    }
}
