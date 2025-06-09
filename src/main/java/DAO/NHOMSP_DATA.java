package DAO;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DTO.NHOMSP;
import DTO.SANPHAM;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import utils.IntHelper;
import utils.StringHelper;

/**
 *
 * @author Admin
 */
public class NHOMSP_DATA {

    private ArrayList<NHOMSP> listnhomSP = null;

    public NHOMSP_DATA() {
        docListnhomSP();
    }

    public void docListnhomSP() {
        listnhomSP = getListnhomSP();
    }

    public ArrayList<NHOMSP> getListnhomSP() {
        String sql = "SELECT * FROM NhomSP";
        try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            ArrayList<NHOMSP> dssp = new ArrayList<>();
            while (rs.next()) {
                NHOMSP gr = new NHOMSP();
                gr.setGroupID(rs.getInt(1));
                gr.setName(StringHelper.safeTrim(rs.getString(2)));
                gr.setStatus(StringHelper.safeTrim(rs.getString(3)));
                dssp.add(gr);
            }
            return dssp;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi lấy danh sách nhóm sản phẩm: " + e.getMessage(),
                        "ERROR!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public ArrayList<NHOMSP> getListnhomSP_Status(String status) {
        String sql = "SELECT * FROM NhomSP WHERE status = ?";
        try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            ArrayList<NHOMSP> dssp = new ArrayList<>();
            while (rs.next()) {
                NHOMSP gr = new NHOMSP();
                gr.setGroupID(rs.getInt(1));
                gr.setName(StringHelper.safeTrim(rs.getString(2)));
                gr.setStatus(StringHelper.safeTrim(rs.getString(3)));
                dssp.add(gr);
            }
            return dssp;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi lấy danh sách nhóm sản phẩm theo trạng thái: " + e.getMessage(),
                        "ERROR!", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public ArrayList<NHOMSP> getGrProduct_Name(String text) {
        ArrayList<NHOMSP> list = new ArrayList<>();
        for (NHOMSP gr : listnhomSP) {
            String name = gr.getName().toLowerCase();
            if (name.contains(text.toLowerCase())) {
                list.add(gr);
            }
        }
        return list;
    }

    public static void create_GrProduct(String name, String status) {
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên nhóm sản phẩm không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = CONNECTION.getConnection()) {

            // Kiểm tra trùng tên (không phân biệt hoa thường)
            String checkSql = "SELECT 1 FROM NhomSP WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name.trim());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "Tên nhóm sản phẩm đã tồn tại.",
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
            String insertSql = "INSERT INTO NhomSP (name, status) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, StringHelper.safeTrim(name));
                ps.setString(2, status);
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(null,
                        rows > 0 ? "Thêm nhóm sản phẩm thành công!" : "Không thêm được nhóm sản phẩm.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi thêm nhóm sản phẩm: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void update_GrProduct(int gr_ID, String oldName, String name, String status) {
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên nhóm sản phẩm không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Nếu tên mới khác tên cũ thì kiểm tra trùng lặp
        if (!name.equalsIgnoreCase(oldName)) {
            String checkSql = "SELECT COUNT(*) FROM NhomSP WHERE name = ?";
            try (
                    Connection conn = CONNECTION.getConnection(); PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Tên nhóm sản phẩm đã tồn tại. Vui lòng chọn tên khác.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi kiểm tra tên nhóm sản phẩm: " + e.getMessage(),
                        "ERROR!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
        // Hỏi xác nhận trước khi sửa
        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc chắn muốn sửa nhóm sản phẩm này?",
                "Xác nhận sửa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Người dùng không đồng ý, thoát hàm
        }
        String sql = "UPDATE NhomSP set name=? , status= ? WHERE group_id= ?";
        try (
                Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, status);
            ps.setInt(3, gr_ID);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Sửa nhóm sản phẩm thành công!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Không tìm thấy nhóm sản phẩm để cập nhật.",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sữa Nhóm sản phẩm: " + e.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public static void delete_GrProduct(int gr_ID) {
        try {
            // 1. Hỏi xác nhận người dùng
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn xóa nhóm sản phẩm này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng chọn "Không" => thoát hàm, không xóa
            }

            // 2. Kiểm tra xem group_id có đang được sử dụng không
            boolean isUsed = isGroupIdInUse(gr_ID);

            // 3. Chọn câu SQL và thông điệp phù hợp
            String sql = isUsed
                    ? "UPDATE NhomSP SET status = 0 WHERE group_id = ?"
                    : "DELETE FROM NhomSP WHERE group_id = ?";

            String message = isUsed
                    ? "Nhóm sản phẩm đang được sử dụng, đã chuyển sang trạng thái 'BỊ XOÁ'."
                    : "Xóa nhóm sản phẩm thành công!";

            // 4. Thực hiện câu lệnh
            try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, gr_ID);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Không tìm thấy nhóm sản phẩm để xử lý.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi xử lý xóa nhóm sản phẩm:\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean isGroupIdInUse(int gr_ID) {
        String sql = "SELECT TOP 1 1 FROM LoaiSP WHERE group_id = ?";
        try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gr_ID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true nếu đang được sử dụng
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi kiểm tra group_id:\n" + e.getMessage(),
                    "Lỗi kiểm tra dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    public int name_to_ID(String name) {
        for (NHOMSP gr : listnhomSP) {
            if (name.equals(gr.getName())) {
                return gr.getGroupID();
            }
        }
        return -1;
    }

    public String iD_to_name(int id) {
        for (NHOMSP gr : listnhomSP) {
            if (id == (gr.getGroupID())) {
                return gr.getName();
            }
        }
        return null;
    }
}
