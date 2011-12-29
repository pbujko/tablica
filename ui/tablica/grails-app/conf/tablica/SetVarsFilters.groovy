package tablica

class SetVarsFilters {
    
    def categoryManager

    def filters = {
        all(controller:'index|ad|search', action:'*') {
            before = {

            }
            after = { Map model ->
                if(model){
                    model.allCities = categoryManager.getAllCities()
                    model.citySelected = params.city}
            }
            afterView = { Exception e ->

            }
        }
    }
}
