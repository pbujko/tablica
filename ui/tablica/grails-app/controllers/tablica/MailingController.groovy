package tablica

class MailingController {
    def adDao
    

    def pw() {
        [ad:adDao.findById("1"), from:'me@me.me', msgBody:'msg body']
    }
    
    def adActivation(){
        def ad=adDao.findById("1")
        [ad:ad, key:new AdEncoder().encodeAd(ad)]
    }
}
