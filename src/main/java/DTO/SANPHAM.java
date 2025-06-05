package DTO;

public class SANPHAM {
    private int productID;
    private int categoryID;
    private String serial;
    private String status;
    private String startDate;
    private String endDate;
    private String tenLoai;
    private int supplier_id;
    private String supplier_name;

    public SANPHAM() {
    }

    public SANPHAM(int productID, int categoryID, String serial, String status, String startDate, String endDate, String tenLoai, int supplier_id, String supplier_name) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.serial = serial;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tenLoai = tenLoai;
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    
    
    public void setProductID(int productID) {
        this.productID = productID;
    }
    
    

    public int getProductID() {
        return productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    
}
