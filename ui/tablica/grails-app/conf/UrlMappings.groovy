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

        name searchPhrase: "/szukaj"{
            controller= "search"
            action="byPhrase"
                   
        }
               
        name search: "/szukaj/$code/$atts?"{
            controller= "search"
            action="index"
                   
        }

        name searchByPhrase: "/q/$catCode?/$phrase"{
            controller= "search"
            action="byPhrase"
                   
        }


        name searchRecent: "/najnowsze"{
            controller= "search"
            action="recent"
                   
        }        

        name newAd: "/nowe-ogloszenie"{
            controller="ad"
            action="create"
        }
        
        name adDetails: "/ogloszenie/${niewazneCo}_$id?"{
            controller = "ad"
            action = "show"
                       
        }
        
        name image:"/image/$id/img.jpg"{
            controller = "adImage"
            action = "image"
        }
                
        
                
        
    }
}
