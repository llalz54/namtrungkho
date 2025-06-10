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
import utils.StringHelper;

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
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên sản phẩm không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(brand)) {
            JOptionPane.showMessageDialog(null, "Tên hãng không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = CONNECTION.getConnection()) {

            // Kiểm tra trùng tên (không phân biệt hoa thường)
            String checkSql = "SELECT 1 FROM loaiSP WHERE LOWER(name) = LOWER(?)";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name.trim());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null,
                            "Tên sản phẩm đã tồn tại.",
                            "Trùng tên", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Xác nhận thêm mới
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn thêm sản phẩm này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Thực hiện thêm mới
            String insertSql = "INSERT INTO loaiSP (group_id, name, status, brand) VALUES (?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, grID);
                ps.setString(2, StringHelper.safeTrim(name));
                ps.setString(3, status);
                ps.setString(4, brand);
                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(null,
                        rows > 0 ? "Thêm sản phẩm thành công!" : "Không thêm được nhóm sản phẩm.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi thêm sản phẩm: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void update_GrProduct(int cateID, int grID, String name, String status, String brand) {
        if (StringHelper.isNullOrBlank(name)) {
            JOptionPane.showMessageDialog(null, "Tên nhóm sản phẩm không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (StringHelper.isNullOrBlank(brand)) {
            JOptionPane.showMessageDialog(null, "Tên hãng không được để trống.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Nếu tên mới khác tên cũ thì kiểm tra trùng lặp
        String checkSql = "SELECT 1 FROM loaiSP WHERE name = ? AND category_id != ?";
        try (
                Connection conn = CONNECTION.getConnection(); PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, name);
            checkPs.setInt(2, cateID);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Tên sản phẩm đã tồn tại. Vui lòng chọn tên khác.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi kiểm tra tên sản phẩm: " + e.getMessage(),
                    "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        // Hỏi xác nhận trước khi sửa
        int confirm = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc chắn muốn sửa sản phẩm này?",
                "Xác nhận sửa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Người dùng không đồng ý, thoát hàm
        }
        String sql = "UPDATE LoaiSP set group_id=?, name=? , status=?, brand=? WHERE category_id= ?";
        try (
                Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grID);
            ps.setString(2, name);
            ps.setString(3, status);
            ps.setString(4, brand);
            ps.setInt(5, cateID);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Sửa sản phẩm thành công!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Không tìm thấy sản phẩm để cập nhật.",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa sản phẩm: " + e.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public static void delete_GrProduct(int cate_ID) {
        try {
            // 1. Hỏi xác nhận người dùng
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc chắn muốn xóa sản phẩm này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // Người dùng chọn "Không" => thoát hàm, không xóa
            }

            // 2. Kiểm tra xem cate_id có đang được sử dụng không
            boolean isUsed = isCategoryIdInUse(cate_ID);

            // 3. Chọn câu SQL và thông điệp phù hợp
            String sql = isUsed
                    ? "UPDATE LoaiSP SET status = 0 WHERE category_id = ?"
                    : "DELETE FROM LoaiSP WHERE category_id = ?";

            String message = isUsed
                    ? "Sản phẩm đang được sử dụng, đã chuyển sang trạng thái 'BỊ XOÁ'."
                    : "Xóa sản phẩm thành công!";

            // 4. Thực hiện câu lệnh
            try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, cate_ID);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Không tìm thấy sản phẩm để xử lý.",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi xử lý xóa sản phẩm:\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean isCategoryIdInUse(int cate_ID) {
        String sql = "SELECT TOP 1 1 FROM PhieuNhap WHERE category_id = ?";
        try (Connection conn = CONNECTION.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cate_ID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true nếu đang được sử dụng
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Đã xảy ra lỗi khi kiểm tra cateInUse:\n" + e.getMessage(),
                    "Lỗi kiểm tra dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
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
