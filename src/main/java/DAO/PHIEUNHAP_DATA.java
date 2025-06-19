package DAO;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DTO.PHIEUNHAP;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PHIEUNHAP_DATA {
    private ArrayList<PHIEUNHAP> listPN = null;
    
    public PHIEUNHAP_DATA() {
        docListPN();
    }
    
    public void docListPN() {
        listPN = getListPN();
    }
    
    public ArrayList<PHIEUNHAP> getListPN() {
        try {
           DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT pn.*, c.name AS tenLoai, s.name AS tenNCC FROM PhieuNhap pn  JOIN LoaiSP c ON pn.category_id = c.category_id JOIN NCC s on pn.supplier_id = s.supplier_id ORDER BY pn.ngayNhap DESC");
            ArrayList<PHIEUNHAP> dssp = new ArrayList<>();
            while (rs.next()) {
                PHIEUNHAP px = new PHIEUNHAP();
                px.setIdpn(rs.getInt("idpn"));
                px.setHoaDon(rs.getString("soHoaDon"));
                px.setTenLoai(rs.getString("tenLoai").trim());
                px.setQuantity(rs.getInt("quantity"));
                px.setPrice(rs.getLong("price"));
                px.setNgayNhap(rs.getString("ngayNhap"));
                px.setSupplier(rs.getString("tenNCC").trim());
                px.setDiaChiKho(rs.getString("diaChiKho"));
                dssp.add(px);

            }
            acc.close();
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách phiếu nhập!!!");
        }
        return null;
    }

    public static boolean createPN(int userID, int categoryID, int quantity, int price, String ngayNhap, String supplier) {
        Connection conn = CONNECTION.getConnection();
        try {
            String sql = "INSERT INTO PHIEUNHAP values(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, userID);
            ps.setInt(2, categoryID);
            ps.setInt(3, quantity);
            ps.setInt(4, price);
            ps.setString(5, ngayNhap);
            ps.setString(6, supplier);
            ps.executeUpdate();
            
            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm PN!!!");
        }
        return false;
    }
    
    public static void updatePN(){
        
    }
    
    public static void create_TB_CTPN(int quantity){
        
    }
    public ArrayList<PHIEUNHAP> getSPtheoTen(String key) {
        ArrayList<PHIEUNHAP> allSP = getListPN();
        ArrayList<PHIEUNHAP> dssp = new ArrayList<>();
        for (PHIEUNHAP sp : allSP) {
            String ncc = sp.getSupplier().toLowerCase();
            String tenSP = sp.getTenLoai().toLowerCase();
           
            if (tenSP.contains(key.toLowerCase()) || ncc.contains(key.toLowerCase()) ) {
                dssp.add(sp);
            }
        }
        return dssp;
    }
}
