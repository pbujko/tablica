
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import net.bujko.tablica.be.model.Ad
import net.bujko.tablica.be.model.Category


class BootStrap {

    def grailsApplication
    def adDao, categoryManager, searchDao
    
    def init = { servletContext ->
        
        servletContext["searchStats"] = searchDao.summary
        
        
        
    
        String sqlFilePath = 'grails-app/conf/setUpDB.sql'
        String sqlString = new File(sqlFilePath).text
        
        def sql = groovy.sql.Sql.newInstance(ConfigurationHolder.config.dataSource.url, ConfigurationHolder.config.dataSource.username, ConfigurationHolder.config.dataSource.password, ConfigurationHolder.config.dataSource.driverClassName)

        sqlString.split(';').each(){
            //print ">>>>>>${it}"
            sql.execute it
            
        }
                
        Ad ad = new Ad()       
        ad.setDescription("description ad cat 1.1")
        ad.setTitle("Mercedesa, cat 1.1")
        ad.setHashedId("---1---1--asd-")      
        ad.addCategory(categoryManager.getCategoryById("1.1"))
        adDao.save(ad)        
        
        ad = new Ad()       
        ad.setDescription("opis syrenke")
        ad.setTitle("Syrenkeeeeeeeee")
        ad.setHashedId("-sfd-sdf-sdffgh-sdfgg-")
        ad.addCategory(categoryManager.getCategoryById("1.1"))
        adDao.save(ad)   
        
        ad = new Ad()       
        ad.setDescription("opis syrenke")
        ad.setTitle("Syrenke")
        ad.setHashedId("-sfd-sdf-sdf-sdfgg-")
        ad.addCategory(categoryManager.getCategoryById("1.1"))
        adDao.save(ad)        

        ad = new Ad()       
        ad.setDescription("owczarka niemieckiego")
        ad.setTitle("Owczarka niemieckiego")
        ad.setHashedId("-sfd-sssss-")
        ad.addCategory(categoryManager.getCategoryById("2.1"))
        adDao.save(ad)          

        ad = new Ad()       
        ad.setDescription("kocura perskiego")
        ad.setTitle("Kota mam")
        ad.setHashedId("-sfd-ssssssss-")
        ad.addCategory(categoryManager.getCategoryById("2.2"))
        adDao.save(ad)                  

        //wolaj init jeszcze raz tylko dlatego, ze po automatycznym init baza jest kasowana przez powyzszy SQL.
        //w produkcji nie ma potrzeby manualnego wolania init()
        categoryManager.init()
    }
    def destroy = {
    }
}
