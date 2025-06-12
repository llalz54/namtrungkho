package DAO;

import ConDB.DBAccess;
import DTO.SANPHAM;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SANPHAM_DATA {

    private ArrayList<SANPHAM> listSP = null;

    public SANPHAM_DATA() {
        docListSP();
    }

    public void docListSP() {
        listSP = getListSP();
    }

    public ArrayList<SANPHAM> getListSP() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT sp.*, c.name FROM SanPham sp  JOIN LoaiSP c ON sp.category_id = c.category_id");
            ArrayList<SANPHAM> dssp = new ArrayList<>();
            while (rs.next()) {
                SANPHAM sp = new SANPHAM();
                sp.setProductID(rs.getInt("product_id"));
                sp.setTenLoai(rs.getString("name"));
                sp.setSerial(rs.getString("serial").trim());

                sp.setStatus(rs.getString("status"));
                sp.setStartDate(rs.getString("start_date"));
                sp.setEndDate(rs.getString("end_date"));
                dssp.add(sp);

            }
            acc.close();
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm!!!");
            return null;
        }
    }

    public ArrayList<SANPHAM> getlistSP_TK() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT sp.*, c.name FROM SanPham sp  JOIN LoaiSP c ON sp.category_id = c.category_id where sp.status = 1");
            ArrayList<SANPHAM> dssp = new ArrayList<>();
            while (rs.next()) {
                SANPHAM sp = new SANPHAM();
                sp.setProductID(rs.getInt("product_id"));
                sp.setTenLoai(rs.getString("name"));
                sp.setSerial(rs.getString("serial").trim());

                sp.setStatus(rs.getString("status"));
                sp.setStartDate(rs.getString("start_date"));
                sp.setEndDate(rs.getString("end_date"));
                dssp.add(sp);

            }
            acc.close();
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm!!!");
            return null;
        }
    }

    public ArrayList<SANPHAM> getlistSP_DB() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT sp.*, c.name FROM SanPham sp  JOIN LoaiSP c ON sp.category_id = c.category_id where sp.status = 0");
            ArrayList<SANPHAM> dssp = new ArrayList<>();
            while (rs.next()) {
                SANPHAM sp = new SANPHAM();
                sp.setProductID(rs.getInt("product_id"));
                sp.setTenLoai(rs.getString("name"));
                sp.setSerial(rs.getString("serial").trim());

                sp.setStatus(rs.getString("status"));
                sp.setStartDate(rs.getString("start_date"));
                sp.setEndDate(rs.getString("end_date"));
                dssp.add(sp);

            }
            acc.close();
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm!!!");
            return null;
        }
    }
    

    public ArrayList<SANPHAM> getSP_Category(int categoryID) {
        ArrayList<SANPHAM> allSP = getListSP();
        ArrayList<SANPHAM> dssp = new ArrayList<>();
        for (SANPHAM sp : allSP) {
            int category = sp.getCategoryID();
            if (categoryID == category) {
                dssp.add(sp);
            }
        }
        return dssp;
    }

    public ArrayList<SANPHAM> getSPtheoSerial(String Serial) {
        ArrayList<SANPHAM> dssp = new ArrayList<>();
        for (SANPHAM sp : listSP) {
            String serial = sp.getSerial().toLowerCase();
            if (serial.contains(Serial.toLowerCase())) {
                dssp.add(sp);
            }
        }
        return dssp;
    }
    
    
    public ArrayList<SANPHAM> getSPTheoSerial_TK(String Serial) {
        ArrayList<SANPHAM> allSP = getlistSP_TK();
        ArrayList<SANPHAM> dssp = new ArrayList<>();
        for (SANPHAM sp : allSP) {
            String serial = sp.getSerial().toLowerCase();
            if (serial.contains(Serial.toLowerCase())) {
                dssp.add(sp);
            }
        }
        return dssp;
    }
    
    public ArrayList<SANPHAM> getSPTheoSerial_DB(String Serial) {
        ArrayList<SANPHAM> allSP = getlistSP_DB();
        ArrayList<SANPHAM> dssp = new ArrayList<>();
        for (SANPHAM sp : allSP) {
            String serial = sp.getSerial().toLowerCase();
            if (serial.contains(Serial.toLowerCase())) {
                dssp.add(sp);
            }
        }
        return dssp;
    }

}
