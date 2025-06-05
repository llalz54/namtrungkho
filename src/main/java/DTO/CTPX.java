package DTO;

public class CTPX {
    private int idpx;
    private String serial;

    public CTPX() {
    }

    public CTPX(int idpx, String serial) {
        this.idpx = idpx;
        this.serial = serial;
    }

    public int getIdpx() {
        return idpx;
    }

    public void setIdpx(int idpx) {
        this.idpx = idpx;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }    
    
}
