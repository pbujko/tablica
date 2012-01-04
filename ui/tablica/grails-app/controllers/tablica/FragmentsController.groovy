package tablica

class FragmentsController {
    def categoryManager

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
}
