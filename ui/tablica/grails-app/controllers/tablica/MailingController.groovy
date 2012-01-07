package tablica

class MailingController {
    def adDao
    def pw() {
        [ad:adDao.findById("1"), from:'me@me.me', msgBody:'msg body']
    }
}
