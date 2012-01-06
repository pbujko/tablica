package tablica

import grails.converters.JSON
import static org.codehaus.groovy.grails.commons.ConfigurationHolder.config as Config
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

class AdImageController {

    def upload() { 
        println "upload: ${params.qqfile}"
        def InputStream inputStream
        try{
            inputStream = selectInputStream(request, params)
            // uploaded << inputStream         

        }
        catch(e){
            log.warn(e)
            return render(text: [success:false] as JSON, contentType:'text/json')                        
        }
        
        
        def imageTool = new org.grails.plugins.imagetools.ImageTool()
        imageTool.load(inputStream.bytes)
        imageTool.thumbnailSpecial(120, 120, 3, 2)
        def thumbImage = imageTool.getBytes("JPEG")
        println "Returning thumb size ${thumbImage.length}"
        
        AdImage adImage= new AdImage(hashedId:params.aa)
        adImage.thumbnail = thumbImage
        
        imageTool.thumbnailSpecial(800, 800, 2, 1)
        def bigImage = imageTool.getBytes("JPEG")
        println "Returning bigImage size ${bigImage.length}"
        adImage.image=bigImage 
        //     adImage.image = inputStream.bytes
        adImage.save()
        println "done processing image, id: ${adImage.id}, hashedId: ${adImage.hashedId}"
        
        return render(text: [success:true, imgId:adImage.id] as JSON, contentType:'text/json')

    }

    def image() {
        def something = AdImage.get( params.id )
        byte[] image = something.image

        response.contentType="image/jpeg";        
        response.outputStream << image
    }
    

    def thumbnail() {
        def something = AdImage.get( params.id )
        byte[] image = something.thumbnail
        response.contentType="image/jpeg";
        response.outputStream << image
    }    
    
    def delete={
        def ad = AdImage.get(params.id)
        if(ad && ad.hashedId==params.hashedId){
            ad.delete()
            println "deleted adImage ${params.id}"
        }
        render "deleted"
    }
    
    private InputStream selectInputStream(HttpServletRequest request, params) {
        if (request instanceof MultipartHttpServletRequest) {
            println "instance of multipart..."
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')   
            
            def fExtension = uploadedFile.originalFilename.lastIndexOf('.') >= 0 ? uploadedFile.originalFilename.substring( uploadedFile.originalFilename.lastIndexOf('.')+1 ) : uploadedFile.originalFilename
            print fExtension
            if(fExtension != 'png' && fExtension != 'PNG'&& fExtension != 'jpg' && fExtension != 'JPG'){
                log.debug "bad multipart file type ${fExtension}"
                throw new Exception("BAD FILE " +fExtension)
            }            
                        
            return uploadedFile.inputStream
        }
        else{
            println "NOT instance of multipart..."        
            def fExtension = params.qqfile.lastIndexOf('.') >= 0 ? params.qqfile.substring( params.qqfile.lastIndexOf('.')+1 ) : params.qqfile        
            if(fExtension != 'png' && fExtension != 'PNG'&& fExtension != 'jpg' && fExtension != 'JPG'){
                throw new Exception("bad file type ${fExtension}, filename: ${params.qqfile}")
            }            
                        
            return request.inputStream            
        }


    }
}
