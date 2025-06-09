
package DTO;

import java.util.List;

/**
 *
 * @author Admin
 */
class ProductInfo {
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
}
