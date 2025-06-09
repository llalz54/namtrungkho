package DTO;

import java.util.List;

public class ProductInfo {
    int idpx;
    String name;
    long price;
    int soLuong;
    List<String> serials;
    
    public ProductInfo(int idpx, String name, long price, int soLuong, List<String> serials) {
        this.idpx = idpx;
        this.name = name;
        this.price = price;
        this.soLuong = soLuong;
        this.serials = serials;
    }

    public int getIdpx() {
        return idpx;
    }

    public void setIdpx(int idpx) {
        this.idpx = idpx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }
    
    
}
