package DTO;

public class CTPN {
    private int idpn;
    private String serial;

    public CTPN() {
    }

    public CTPN(int idpn, String serial) {
        this.idpn = idpn;
        this.serial = serial;
    }

    public int getIdpn() {
        return idpn;
    }

    public void setIdpn(int idpn) {
        this.idpn = idpn;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
