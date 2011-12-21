package tablica

class ErrorController {

    def error() {
        println 'eee'
        if(grails.util.GrailsUtil.getEnvironment() == 'development'){
            render(view:"error_dev")
        }
        else
        render(view:"error")
    
    }
}
