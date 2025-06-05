package DTO;

import java.sql.ResultSet;

public class LOAISP {
    private int categoryID;
    private int groupID;
    private String grName;
    private String name;
    private String brand;
    private String status;
    private int soLuong;

    public LOAISP() {
    }
    
    public LOAISP(ResultSet rs) throws Exception{
        this.categoryID = rs.getInt("category_id");
        this.name = rs.getString("name");
    }

    public LOAISP(int categoryID, int groupID, String name, String brand, String status) {
        this.categoryID = categoryID;
        this.groupID = groupID;
        this.name = name;
        this.brand = brand;
        this.status = status;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrName() {
        return grName;
    }

    public void setGrName(String grName) {
        this.grName = grName;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
      
}
