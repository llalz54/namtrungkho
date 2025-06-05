package DTO;

public class PHIEUNHAP {
    private int idpn;
    private int userID;
    private int categoryID;
    private int quantity;
    private long price;
    private String ngayNhap;
    private int supplier_id;
    private String supplier;
    private int group_id;
    private long tongTien;
    private String tenLoai;
    public PHIEUNHAP() {
    }

    public PHIEUNHAP(int idpn, int userID, int categoryID,int group_id, int quantity, long price, String ngayNhap,int supplier_id, String supplier,String tenLoai) {
        this.idpn = idpn;
        this.userID = userID;
        this.categoryID = categoryID;
        this.supplier_id = supplier_id;
        this.quantity = quantity;
        this.price = price;
        this.ngayNhap = ngayNhap;
        this.supplier = supplier;
        this.group_id = group_id;
        this.tenLoai = tenLoai;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
    

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public long getTongTien() {
        return quantity * price;
    }

    public void setIdpn(int idpn) {
        this.idpn = idpn;
    }
    

    public void setTongTien(long tongTien) {
        this.tongTien = quantity*price;
    }

    
    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    
    public int getIdpn() {
        return idpn;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
    
    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    
}
