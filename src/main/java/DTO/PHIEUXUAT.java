package DTO;

public class PHIEUXUAT {
    private int idpx;
    private int userID;
    private int categoryID;
    private int quantity;
    private long price;
    private String ngayXuat;
    private String customer;
    private String tenLoai;
    private String diachi;
    private long tongTien;
    private String hoaDon;
    private String donViXuat;
    private String ngayXuatHD;

    public PHIEUXUAT() {
    }

    public PHIEUXUAT(int idpx, int userID, int categoryID, int quantity, long price, String ngayXuat, String customer,String diaChi, String hoaDon, String donViXuat, String ngayXuatHD) {
        this.idpx = idpx;
        this.userID = userID;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.price = price;
        this.ngayXuat = ngayXuat;
        this.customer = customer;
        this.diachi = diaChi;
        this.hoaDon = hoaDon;
        this.donViXuat = donViXuat;
        this.ngayXuatHD = ngayXuatHD;
    }

    public String getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(String hoaDon) {
        this.hoaDon = hoaDon;
    }

    public String getDonViXuat() {
        return donViXuat;
    }

    public void setDonViXuat(String donViXuat) {
        this.donViXuat = donViXuat;
    }

    public String getNgayXuatHD() {
        return ngayXuatHD;
    }

    public void setNgayXuatHD(String ngayXuatHD) {
        this.ngayXuatHD = ngayXuatHD;
    }

    
    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    
    public double getTongTien() {
        return quantity * price;
    }

    
    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
    

    public int getIdpx() {
        return idpx;
    }

    public void setIdpx(int idpx) {
        this.idpx = idpx;
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

    public String getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(String ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    
}
