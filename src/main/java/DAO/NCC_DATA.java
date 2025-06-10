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
import utils.StringHelper;

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
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên gợi nhớ nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(fullName)) {
            JOptionPane.showMessageDialog(null, "Tên nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(MST)) {
            JOptionPane.showMessageDialog(null, "Mã số thuế nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = CONNECTION.getConnection()) {

            // Kiểm tra trùng tên (không phân biệt hoa thường)
            String checkSql = "SELECT 1 FROM NCC WHERE LOWER(name) = LOWER(?) OR LOWER(fullname) = LOWER(?)";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name);
                checkPs.setString(2, fullName);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "Tên hoặc tên gợi nhớ nhà cung cấp đã tồn tại.",
                            "Trùng tên", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Xác nhận thêm mới
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn thêm nhóm sản phẩm này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Thực hiện thêm mới
            String insertSql = "INSERT INTO NCC (name, fullname, MST, address, status) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, StringHelper.safeTrim(name));
                ps.setString(2, StringHelper.safeTrim(fullName));
                ps.setString(3, StringHelper.safeTrim(MST));
                ps.setString(4, StringHelper.safeTrim(address));
                ps.setString(5, StringHelper.safeTrim(status));
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(null,
                        rows > 0 ? "Thêm nhà cung cấp thành công!" : "Không thêm được nhà cung cấp.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi thêm nhà cung cấp: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void update_Supplier(int id, String name, String oldName, String fullName, String oldFullName, String MST, String address, String status) {
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên gợi nhớ nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(fullName)) {
            JOptionPane.showMessageDialog(null, "Tên nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(MST)) {
            JOptionPane.showMessageDialog(null, "Mã số thuế nhà cung cấp không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Nếu tên mới khác tên cũ thì kiểm tra trùng lặp
        if (!name.equalsIgnoreCase(oldName) || !fullName.equalsIgnoreCase(oldFullName)) {
            String checkSql = "SELECT 1 FROM NCC WHERE (name = ? OR fullname = ?) AND supplier_id != ?";
            try (
                    Connection conn = CONNECTION.getConnection(); PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name);
                checkPs.setString(2, fullName);
                checkPs.setInt(3, id);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "Tên hoặc tên nhà cung cấp đã tồn tại. Vui lòng chọn tên khác.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi kiểm tra tên nhà cung cấp: " + e.getMessage(),
                        "ERROR!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
        // Hỏi xác nhận trước khi sửa
        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc chắn muốn sửa nhà cung cấp này?",
                "Xác nhận sửa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Người dùng không đồng ý, thoát hàm
        }
        String sql = "UPDATE NCC SET name = ?, fullname = ?, MST = ?, address = ?, status = ? WHERE supplier_id= ?";
        try (
                Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, fullName);
            ps.setString(3, MST);
            ps.setString(4, address);
            ps.setString(5, status);
            ps.setInt(6, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Sửa nhà cung cấp thành công!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Không tìm thấy nhà cung cấp để cập nhật.",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sữa nhà cung cấp: " + e.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
    
    public static void delete_GrProduct(int id) {
        try {
            // 1. Hỏi xác nhận người dùng
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn xóa nhà cung cấp này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng chọn "Không" => thoát hàm, không xóa
            }

            // 2. Kiểm tra xem supplier_id có đang được sử dụng không
            boolean isUsed = isSupplierIdInUse(id);

            // 3. Chọn câu SQL và thông điệp phù hợp
            String sql = isUsed
                    ? "UPDATE NCC SET status = 0 WHERE supplier_id = ?"
                    : "DELETE FROM NCC WHERE supplier_id = ?";

            String message = isUsed
                    ? "Nhà cung cấp đang được sử dụng, đã chuyển sang trạng thái 'BỊ XOÁ'."
                    : "Xóa nhà cung cấp thành công!";

            // 4. Thực hiện câu lệnh
            try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Không tìm thấy nhà cung cấp để xử lý.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi xử lý xóa nhà cung cấp:\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean isSupplierIdInUse(int id) {
        String sql = "SELECT TOP 1 1 FROM PhieuNhap WHERE supplier_id = ?";
        try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true nếu đang được sử dụng
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi kiểm tra supplier_In_Use:\n" + e.getMessage(),
                    "Lỗi kiểm tra dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return true;
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
