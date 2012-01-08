/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tablica

/**
 *
 * @author pbujko
 */
class AdEncoder {
    def encodeAd(net.bujko.tablica.be.model.Ad ad){

        if(ad)      
        return new String( (ad.id + ad.hashedId).encodeAsMD5().encodeAsBase64() ) 
        else
        return ""
    }
    
    def decodeAd(net.bujko.tablica.be.model.Ad ad, String encodedAd){
        //compare 
        return encodeAd(ad) == encodedAd       
    }
}

