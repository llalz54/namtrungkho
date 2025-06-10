package DAO;

import ConDB.DBAccess;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class OTHER_DATA {

    public static void loadCBDM(JComboBox cbox) {
        cbox.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT name FROM NhomSP");
            while (rs.next()) {
                cbox.addItem(rs.getString("name").trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCBDM!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void load_Cb_Brand(JComboBox cbox) {
        cbox.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT DISTINCT brand FROM LoaiSP");
            while (rs.next()) {
                cbox.addItem(rs.getString("brand").trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCB_Brand!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void load_Cb_Supplier(JComboBox cbox) {
        cbox.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT name FROM NCC");
            while (rs.next()) {
                cbox.addItem(rs.getString("name").trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCB_Supplier!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void customTable(JTable table) {
        // 1. Font và chiều cao header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Cỡ chữ tiêu đề
        header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Chiều cao header

        // 2. Chiều cao dòng
        table.setRowHeight(40); // Chiều cao mỗi dòng

        // 3. Font dòng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));        
        table.setShowVerticalLines(false);

        // 4. (Tùy chọn) Căn giữa hoặc trái cho dữ liệu trong ô
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
    }

}
