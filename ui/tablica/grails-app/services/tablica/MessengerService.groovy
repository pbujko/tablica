/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tablica
import grails.converters.*
import org.codehaus.groovy.grails.web.json.*; // package containing JSONObject, JSONArray,...

/**
 *
 * @author pbujko
 */

class MessengerService {
    def mailService
    def adDao
    def executorService
            
    def send(message){
        
        runAsync {

            log.info "sending ${message}"
            
            if(message.type == 'PW'){         
                mailService.sendMail {

                    def o = JSON.parse(message.meta)

                    def from = o.sender
                    def msgBody = o.msgBody
                    def ad = adDao.findById(o.adId)
                                        
                    to message.msgTo      
                    subject "Zapytanie dot ogłoszenia: '${ad.title}'"
                    body( view:"/mailing/pw", 
                        model:[from:from, msgBody:msgBody, ad:ad])
                    
                    message = Message.get(message.id)
                    message.sent = new Date()
                    
                    message.save()                    
                }
            }
            else if(message.type == 'ACTIVATE'){         
                mailService.sendMail {

                    def o = JSON.parse(message.meta)
                    def ad = adDao.findById(o.adId)
                                        
                    to message.msgTo      
                    subject "Aktywacja ogłoszenia: '${ad.title}'"
                    body( view:"/mailing/adActivation", 
                        model:[ad:ad, key:new AdEncoder().encodeAd(ad)])
                    
                    message = Message.get(message.id)
                    message.sent = new Date()
                    
                    message.save()                    
                }
            }
            
            else
            log.warn "UNKNMSGTYPE: ${message.type}, ${message.id}"
            
            log.info "sent"
        }
                     

    }
}

