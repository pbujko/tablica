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
        println 'upload'
        // File uploaded = new File('/Users/pbujko/Documents/tmp/aaa.upl')
        InputStream inputStream = selectInputStream(request)
        // uploaded << inputStream         


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

    def test(){
        
        def list = AdImage.findAllByHashedId(params.id)
        list.each{
            
            println it.id
            
        }
        render "l: ${list}"
    }
    /**
    def getImageThumbnail = {
        def logFile = AdImage.get(params.id)
        assert logFile != null
        println "Retrieved image size ${logFile.image.length}"
        def imageTool = new org.grails.plugins.imagetools.ImageTool()
        imageTool.load(logFile.image)
        imageTool.thumbnail(800)
        def thumbImage = imageTool.getBytes("JPEG")
        println "Returning thumb size ${thumbImage.length}"
        response.contentType = "image/jpeg"
        response.contentLength = thumbImage.length
        response.outputStream.write(thumbImage)    	    
    }
    
    */
    private InputStream selectInputStream(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')
            return uploadedFile.inputStream
        }
        return request.inputStream
    }
}
