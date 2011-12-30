package tablica

class AdImage {

    static constraints = {
        image(maxSize:10241024)
        thumbnail(maxSize:201024)
    }
    
    String hashedId
    byte[] image
    byte[] thumbnail
}
