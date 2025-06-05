package DAO;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DTO.NCC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class NCC_DATA {

    private ArrayList<NCC> listNCC = null;

    public NCC_DATA() {
        docListNCC();
    }

    public void docListNCC() {
        listNCC = getListNCC();
    }

    public ArrayList<NCC> getListNCC() {
        DBAccess acc = null;
        try {
            acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM NCC");
            ArrayList<NCC> dsncc = new ArrayList<>();
            while (rs.next()) {
                NCC gr = new NCC();
                gr.setSupplier_id(rs.getInt(1));
                gr.setName(rs.getString(2).trim());
                gr.setFullName(rs.getString(3).trim());
                gr.setMST(rs.getString(4).trim());
                gr.setDiaChi(rs.getString(5).trim());
                gr.setStatus(rs.getString(6).trim());
                dsncc.add(gr);
            }
            return dsncc;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhà cung cấp!!!");
            return null;
        } finally {
            if (acc != null) {
                acc.close();
            }
        }
    }

    public ArrayList<NCC> getListNCC_Status(String status) {
        ArrayList<NCC> list = new ArrayList<>();
        String sql = "SELECT * FROM NCC WHERE status = ?";

        try (Connection conn = CONNECTION.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NCC ncc = new NCC();
                ncc.setSupplier_id(rs.getInt(1));
                ncc.setName(rs.getString(2).trim());
                ncc.setFullName(rs.getString(3).trim());
                ncc.setMST(rs.getString(4).trim());
                ncc.setDiaChi(rs.getString(5).trim());
                ncc.setStatus(rs.getString(6).trim());
                list.add(ncc);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhà cung cấp!!!");
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public static void create_Supplier(String name, String fullName, String MST, String address, String status) {
        Connection conn = CONNECTION.getConnection();
        String sql = "insert into NCC values(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, fullName);
            ps.setString(3, MST);
            ps.setString(4, address);
            ps.setString(5, status);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm NCC!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void update_Supplier(int id, String name, String fullName, String MST, String address, String status) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = CONNECTION.getConnection();
            String sql = "UPDATE NCC SET name = ?, fullname = ?, MST = ?, address = ?, status = ? WHERE supplier_id= ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, fullName);
            ps.setString(3, MST);
            ps.setString(4, address);
            ps.setString(5, status);
            ps.setInt(6, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Sửa nhà cung cấp thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa nhà cung cấp!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void delete_Supplier(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = CONNECTION.getConnection();
            Boolean result = check_HD_NCC(id);
            if (result == null) {
                JOptionPane.showMessageDialog(null, "Lỗi kiểm tra dữ liệu nhà cung cấp!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!result) {
                String sql = "DELETE FROM NCC WHERE supplier_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Xoá nhà cung cấp thành công!");
            } else {
                String sql = "UPDATE NCC SET status = 0 WHERE supplier_id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Nhà cung cấp này đã được ghi phiếu nhập => Thay đổi trạng thái!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi thao tác với nhà cung cấp!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Boolean check_HD_NCC(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = CONNECTION.getConnection();
            String sql = "SELECT TOP 1 1 FROM PhieuNhap WHERE supplier_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            return rs.next(); // Trả về true nếu có ít nhất 1 dòng

        } catch (Exception e) {
            System.out.println("Lỗi hàm kiểm tra hoạt động Nhà cung cấp!");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int name_to_ID(String name) {
        for (NCC ncc : listNCC) {
            if (name.equals(ncc.getName())) {
                return ncc.getSupplier_id();
            }
        }
        return -1;
    }

    public boolean checkName_NCC(String name, String fullName) {
        String sql = "SELECT 1 FROM NCC WHERE name = ? OR fullname = ?";
        try (
                Connection conn = CONNECTION.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, fullName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Có bản ghi => đã tồn tại
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra trùng tên NCC: " + e.getMessage());
            return false;
        }
    }

    public int getNCCId(String name) {
        try (Connection conn = new DBAccess().getConnection()) {
            String sql = "SELECT supplier_id FROM NCC WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("supplier_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<NCC> getNCC_Name(String text) {
        ArrayList<NCC> list = new ArrayList<>();
        for (NCC ncc : listNCC) {
            String name = ncc.getName().toLowerCase();
            if (name.contains(text.toLowerCase())) {
                list.add(ncc);
            }
        }
        return list;
    }

}
