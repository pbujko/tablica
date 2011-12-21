package tablica

class AdController {
    def adDao

    def show() {
    println grails.util.GrailsUtil.getEnvironment()
        def ad = adDao.findById(params.id)

        if(!ad)
            render(view: "noAd")
            
    [ad: ad]
    }
    
    
    def editView(){}
    def editSave(){}
}
