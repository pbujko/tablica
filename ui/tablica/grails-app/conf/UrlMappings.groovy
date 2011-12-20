class UrlMappings {

    static mappings = {
		"/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

		"/"(view:"/index")
		"500"(view:'/error')
        //
               name adDetails: "/ogloszenie/${niewazneCo}_$id?"{
                    controller = "ad"
                    action = "show"
                       
                }
                
        
    }
}
