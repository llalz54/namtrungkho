package DAO;

import ConDB.DBAccess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Connection;
import ConDB.CONNECTION;
import DTO.PHIEUXUAT;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class PHIEUXUAT_DATA {

    private ArrayList<PHIEUXUAT> listPX = null;

    public PHIEUXUAT_DATA() {
        docListPX();
    }

    public void docListPX() {
        listPX = getListPX();
    }

    public ArrayList<PHIEUXUAT> getListPX() {
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT px.*, c.name FROM PhieuXuat px  JOIN LoaiSP c ON px.category_id = c.category_id ORDER BY px.ngayXuat DESC");
            ArrayList<PHIEUXUAT> dssp = new ArrayList<>();
            while (rs.next()) {
                PHIEUXUAT px = new PHIEUXUAT();
                px.setIdpx(rs.getInt("idpx"));
                px.setHoaDon(rs.getString("HoaDon"));
                px.setTenLoai(rs.getString("name").trim());
                px.setQuantity(rs.getInt("quantity"));
                px.setPrice(rs.getLong("price"));
                px.setNgayXuat(rs.getString("ngayXuat"));
                px.setCustomer(rs.getString("customer").trim());
                px.setDonViXuat(rs.getString("DonViXuat"));
                dssp.add(px);

            }
            acc.close();
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm!!!");
            return null;
        }
    }

    public List<String> getSerialByPhieuXuatID(int idpx) throws SQLException {
        List<String> serials = new ArrayList<>();
        String sql = "SELECT serial FROM CTPX WHERE idpx = ?";
        try (Connection conn = new DBAccess().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idpx);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                serials.add(rs.getString("serial"));
            }
        }
        return serials;
    }

    public ArrayList<PHIEUXUAT> getSPtheoTen(String key) {
        ArrayList<PHIEUXUAT> allSP = getListPX();
        ArrayList<PHIEUXUAT> dssp = new ArrayList<>();
        for (PHIEUXUAT sp : allSP) {
            String tenSP = sp.getTenLoai().toLowerCase();
            String kH = sp.getCustomer().toLowerCase();
            String hd = sp.getHoaDon();
            if (hd != null || !hd.isEmpty()) {
                if (tenSP.contains(key.toLowerCase()) || hd.contains(key) || kH.contains(key.toLowerCase())) {
                    dssp.add(sp);
                } else {
                    if (tenSP.contains(key.toLowerCase()) || kH.contains(key.toLowerCase())) {
                        dssp.add(sp);
                    }
                }
            }

        }
        return dssp;
    }

    public boolean xuatHang(int userId, int categoryId, int quantity, long price,
            String customer, String address, String NYC, String ghiChu, String ngayXuat, String hoaDon, String donViXuat, String ngayXuatHD, String startDate, String endDate,
            List<String> listSerial) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DBAccess dBAccess = null;

        try {
            dBAccess = new DBAccess();
            conn = dBAccess.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Kiểm tra serial hợp lệ và thuộc đúng category
            String sqlCheck = "SELECT supplier_id, category_id, status FROM SanPham WHERE serial = ?";
            ps = conn.prepareStatement(sqlCheck);

            for (String serial : listSerial) {
                ps.setString(1, serial);
                rs = ps.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Serial không tồn tại: " + serial);
                    conn.rollback();
                    return false;
                }

                int serialCategoryId = rs.getInt("category_id");
                int serialNCC = rs.getInt("supplier_id");
                int status = rs.getInt("status");

                // Kiểm tra category_id
                if (serialCategoryId != categoryId) {
                    JOptionPane.showMessageDialog(null,
                            "Serial " + serial + " không thuộc loại sản phẩm này!\n"
                            + "Vui lòng nhập serial đúng cho danh mục đã chọn.");
                    conn.rollback();
                    return false;
                }

                // Kiểm tra trạng thái
                if (status != 1) {
                    JOptionPane.showMessageDialog(null,
                            "Serial " + serial + " đã được xuất hoặc không khả dụng!");
                    conn.rollback();
                    return false;
                }

                rs.close();
            }
            ps.close();

            // 2. Insert vào bảng PhieuXuat
            String sqlInsertPX = "INSERT INTO PhieuXuat(user_id, category_id, quantity, "
                    + "price, ngayXuat, customer, address, NYCau, ghiChu, HoaDon, DonViXuat, ngayXuatHD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlInsertPX, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setInt(3, quantity);
            ps.setLong(4, price);
            ps.setString(5, ngayXuat);
            ps.setString(6, customer);
            ps.setString(7, address);
            ps.setString(8, NYC);
            ps.setString(9, ghiChu);
            ps.setString(10, hoaDon);
            ps.setString(11, donViXuat);
            if (ngayXuatHD != null && !ngayXuatHD.trim().isEmpty()) {
                ps.setDate(12, java.sql.Date.valueOf(ngayXuatHD));
            } else {
                ps.setNull(12, java.sql.Types.DATE);
            }
            ps.executeUpdate();

            // Lấy ID vừa insert
            rs = ps.getGeneratedKeys();
            int idpx = -1;
            if (rs.next()) {
                idpx = rs.getInt(1);
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(null, "Lỗi khi tạo phiếu xuất!");
                return false;
            }
            ps.close();
            rs.close();

            // 3. Thêm chi tiết serial và cập nhật trạng thái sản phẩm
            String sqlInsertCTPX = "INSERT INTO CTPX(idpx, serial) VALUES (?, ?)";
            String sqlUpdateStatus = "UPDATE SanPham SET status = 0, "
                    + "start_date = ?, end_date = ? WHERE serial = ?";

            for (String serial : listSerial) {
                // Insert vào CTPX
                ps = conn.prepareStatement(sqlInsertCTPX);
                ps.setInt(1, idpx);
                ps.setString(2, serial);
                ps.executeUpdate();
                ps.close();

                // Update trạng thái sản phẩm
                ps = conn.prepareStatement(sqlUpdateStatus);
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                ps.setString(3, serial);
                ps.executeUpdate();
                ps.close();
            }

            conn.commit(); // Commit transaction
            JOptionPane.showMessageDialog(null, "Xuất hàng thành công! Số phiếu: " + idpx);
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
