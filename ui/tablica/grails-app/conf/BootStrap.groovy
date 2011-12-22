
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import net.bujko.tablica.be.model.Ad
import net.bujko.tablica.be.model.Category


class BootStrap {

    def grailsApplication
    def adDao, categoryManager
    def init = { servletContext ->
    
        String sqlFilePath = 'grails-app/conf/setUpDB.sql'
        String sqlString = new File(sqlFilePath).text
        
        def sql = groovy.sql.Sql.newInstance(ConfigurationHolder.config.dataSource.url, ConfigurationHolder.config.dataSource.username, ConfigurationHolder.config.dataSource.password, ConfigurationHolder.config.dataSource.driverClassName)

        sqlString.split(';').each(){
            //print ">>>>>>${it}"
            sql.execute it
            
        }
                

        //ad1 
        Ad ad = new Ad()
       
        ad.setDescription("description ad cat 121")
        ad.setTitle("title ad cat121")
        ad.setHashedId("---1---1---")
        
        Category c = new Category("121")
        c.setLabel("categ121") 
        ad.addCategory(c)

        adDao.save(ad)        
        
        //ad 2, one categ
        ad = new Ad()
       
        ad.setDescription("description ad cat11")
        ad.setTitle("title ad cat11")
        ad.setHashedId("---2---")

        c = new Category("11")
        c.setLabel("categ11") 
        ad.addCategory(c)
 
        adDao.save(ad)

        
        //ad 3
        ad = new Ad()       
        ad.setDescription("description ad cat1")
        ad.setTitle("title ad cat1")
        ad.setHashedId("---2--12df-")

        c = new Category("1")
        c.setLabel("categ1") 
        ad.addCategory(c)         
        
        adDao.save(ad)
        

        //wolaj init jeszcze raz tylko dlatego, ze po automatycznym init baza jest kasowana przez powyzszy SQL.
        //w produkcji nie ma potrzeby manualnego wolania init()
        categoryManager.init()
    }
    def destroy = {
    }
}
