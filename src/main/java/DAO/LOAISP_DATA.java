package DAO;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DTO.LOAISP;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class LOAISP_DATA {

    private ArrayList<LOAISP> listLoaiSP = null;

    public LOAISP_DATA() {

    }

    public void docListLoaiSP() {
        listLoaiSP = getListLoaiSP();
    }

    public ArrayList<LOAISP> getListLoaiSP() {
        try {
            String query = "SELECT lsp.category_id, lsp.group_id, n.name AS gr_name,"
                    + "lsp.name, lsp.status, lsp.brand, COUNT(sp.product_id) AS soluong\n"
                    + "FROM LoaiSP lsp\n"
                    + "JOIN NhomSP n ON lsp.group_id = n.group_id\n"
                    + "LEFT JOIN SanPham sp ON lsp.category_id = sp.category_id\n"
                    + "GROUP BY lsp.category_id,lsp.group_id,n.name,lsp.name,lsp.status,lsp.brand;";
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query(query);
            ArrayList<LOAISP> list = new ArrayList<>();
            while (rs.next()) {
                LOAISP loaiSP = new LOAISP();
                loaiSP.setCategoryID(rs.getInt("category_id"));
                loaiSP.setGroupID(rs.getInt("group_id"));
                loaiSP.setGrName(rs.getString("gr_name"));
                loaiSP.setName(rs.getString("name"));
                loaiSP.setStatus(rs.getString("status"));
                loaiSP.setBrand(rs.getString("brand"));
                loaiSP.setSoLuong(rs.getInt("soluong"));

                list.add(loaiSP);
            }
            acc.close();
            return list;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách loại sản phẩm!!!");
            return null;
        }
    }

    public ArrayList<LOAISP> getLoaiSP(String nhomSP) {
        DBAccess dBAccess = null;
        String sqString = "SELECT l.category_id, l.name FROM LoaiSP l JOIN NhomSP n ON l.group_id = n.group_id WHERE n.name = ?";
        try {
            dBAccess = new DBAccess();

            PreparedStatement preparedStatement = dBAccess.getConnection().prepareStatement(sqString);
            preparedStatement.setString(1, nhomSP);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<LOAISP> result = new ArrayList<>();
            while (rs.next()) {
                LOAISP category = new LOAISP(rs);
                result.add(category);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Loi load loaiSP");
            e.printStackTrace();
            return null;
        } finally {
            if (dBAccess != null) {
                dBAccess.close();
            }
        }
    }

    public ArrayList<LOAISP> getDS_LoaiSP(String groupName, String brand) {
        //ArrayList<LOAISP> fullList = getListLoaiSP();
        ArrayList<LOAISP> dssp = new ArrayList<>();
        for (LOAISP sp : listLoaiSP) {
            if (groupName.contains(sp.getGrName()) && brand.contains(sp.getBrand())) {
                dssp.add(sp);
            }
        }
        return dssp;
    }

    public static void create_LSP(int grID, String name, String status, String brand) {
        Connection conn = CONNECTION.getConnection();
        String sql = "insert into LoaiSP values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, grID);
            ps.setString(2, name);
            ps.setString(3, status);
            ps.setString(4, brand);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Thêm loại sản phẩm thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm loại sản phẩm!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void update_LSP(int cateID, int grID, String name, String status, String brand) {
        Connection conn = CONNECTION.getConnection();
        String sql = "UPDATE LoaiSP SET group_id='" + grID + "', name='" + name + "', status='" + status + "', brand='" + brand + "'  where category_id='" + cateID + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Sửa sản phẩm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa sản phẩm!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void delete_LSP(int cateID) {
        Connection conn = CONNECTION.getConnection();
        if (check_HDLSP(cateID) == false) {
            String sql = "DELETE FROM LoaiSP WHERE category_id='" + cateID + "'";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Xoá loại sản phẩm thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xoá loại sản phẩm!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String sql1 = "UPDATE LoaiSP SET status='0' WHERE category_id='" + cateID + "'";
            try {
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.executeUpdate();
                ps1.close();
                conn.close();
                JOptionPane.showMessageDialog(null, "Dòng sản phẩm này đã được ghi phiếu nhập => Thay đổi trạng thái!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật trạng thái sản phẩm!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean check_HDLSP(int cateID) {
        Connection conn = CONNECTION.getConnection();
        String sql = "SELECT category_id FROM PhieuNhap WHERE category_id ='" + cateID + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Lỗi hàm kiểm tra hoạt động loại sản phẩm!");
        }
        return false;
    }

    public boolean checkName_LSP(String name) {
        boolean check = false;
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT name FROM LoaiSP WHERE name ='" + name + "'");
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra trùng name");
        }
        return check;
    }

    public int name_to_ID(String name) {
        for (LOAISP sp : listLoaiSP) {
            if (name.equals(sp.getName())) {
                return sp.getCategoryID();
            }
        }
        return -1;
    }

    public int getCategoryIdByName(String tenLoai) {
        int categoryId = -1;
        try (Connection conn = new DBAccess().getConnection()) {
            String sql = "SELECT category_id FROM LOAISP WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenLoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoryId = rs.getInt("category_id");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryId;
    }

}
