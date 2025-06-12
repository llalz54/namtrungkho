package DAO;

import ConDB.DBAccess;
import DTO.NHANVIEN;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NHANVIEN_DATA {

    public NHANVIEN_DATA() {
    }

    public NHANVIEN findByUsername(String username) throws SQLException {
        DBAccess acc = new DBAccess();
        Connection conn = acc.getConnection();
        String sql = "SELECT * FROM NhanVien WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                NHANVIEN nv = new NHANVIEN();
                nv.setUserId(rs.getInt("user_id"));
                nv.setRole(rs.getString("role"));
                nv.setHoTen(rs.getString("hoten"));
                nv.setStatus(rs.getString("status"));
                nv.setUserName(rs.getString("username"));
                nv.setPassWord(rs.getString("password"));
                return nv;
            }
            return null;
        }

    }

    // Thêm nhân viên mới
    public boolean insert(NHANVIEN nv) throws SQLException {
        DBAccess acc = new DBAccess();
        Connection conn = null;
        PreparedStatement ps = null;
        conn = acc.getConnection();
        String sql = "INSERT INTO NhanVien (role, hoten,status, username, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(2, nv.getHoTen());
            stmt.setString(1, nv.getRole());
            stmt.setString(3, nv.getStatus());
            stmt.setString(4, nv.getUserName());
            stmt.setString(5, nv.getPassWord());
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        DBAccess acc = new DBAccess();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = acc.getConnection();
            // Sửa câu lệnh SQL - cập nhật password cho username tương ứng
            String sql = "UPDATE NhanVien SET password = ? WHERE username = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, newPassword); // Mật khẩu mới
            stmt.setString(2, username);    // Điều kiện WHERE

            int rowsAffected = stmt.executeUpdate();
            conn.commit(); // Commit transaction

            return rowsAffected > 0;
        } finally {
            // Đóng tài nguyên theo thứ tự ngược lại
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
