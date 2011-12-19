
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class BootStrap {

    def grailsApplication
    def init = { servletContext ->
    
        String sqlFilePath = 'grails-app/conf/test.sql'
        String sqlString = new File(sqlFilePath).text

        
        def sql = groovy.sql.Sql.newInstance(ConfigurationHolder.config.dataSource.url, ConfigurationHolder.config.dataSource.username, ConfigurationHolder.config.dataSource.password, ConfigurationHolder.config.dataSource.driverClassName)

        sqlString.split(';').each(){
            //print ">>>>>>${it}"
            sql.execute it
            
        }
        
    }
    def destroy = {
    }
}
