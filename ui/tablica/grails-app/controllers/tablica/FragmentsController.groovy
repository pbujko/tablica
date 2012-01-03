package tablica

class FragmentsController {
    def categoryManager

    def index() { }
    
    def listCatsById(){
        if(params.cId)
        render(template: "subcatsDropDownTemplate", model: [cats: categoryManager.getCategoryById(params.cId).getChildCategories() ])
        else
        render ""
    }
}
