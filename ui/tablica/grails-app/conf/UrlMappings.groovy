class UrlMappings {

    static mappings = {
		"/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

		"/"(controller:"index")
		"500"(controller:"error")
        
               name subcategs: "/ogloszenia/$parentCode"{
                   controller= "index"
                   action="secLevel"
                   
               }

               name search: "/szukaj/$code/$atts?"{
                   controller= "search"
                   action="index"
                   
               }

               name searchByPhrase: "/q/$catCode?/$phrase"{
                   controller= "search"
                   action="byPhrase"
                   
               }

        
               name adDetails: "/ogloszenie/${niewazneCo}_$id?"{
                    controller = "ad"
                    action = "show"
                       
                }
                
        
                
        
    }
}
