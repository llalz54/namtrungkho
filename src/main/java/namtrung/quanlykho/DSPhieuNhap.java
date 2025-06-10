package namtrung.quanlykho;

import ConDB.DBAccess;
import DAO.LOAISP_DATA;
import DAO.NCC_DATA;
import DAO.NHOMSP_DATA;
import DAO.OTHER_DATA;
import DAO.PHIEUNHAP_DATA;
import DAO.PHIEUXUAT_DATA;
import DAO.Session;
import DAO.UpperCase;
import DTO.LOAISP;
import DTO.PHIEUNHAP;
import DTO.PHIEUXUAT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class DSPhieuNhap extends JPanel {

    private ArrayList<LOAISP> loaisps;
    private NHOMSP_DATA nhomsp_data = new NHOMSP_DATA();
    private LOAISP_DATA loaisp_data = new LOAISP_DATA();
    private PHIEUXUAT_DATA px_data = new PHIEUXUAT_DATA();
    private PHIEUNHAP_DATA pn_data = new PHIEUNHAP_DATA();
    private NCC_DATA ncc_data = new NCC_DATA();

    public DSPhieuNhap() {

        initComponents();
        loadDataTableSP();
        customControls();
        OTHER_DATA.load_Cb_Supplier(cb_Supplier);
        OTHER_DATA.customTable(tbPN);
        OTHER_DATA.customTable(tbSerial);
    }

    private void capNhatBangSerialTheoSoLuong() {
        DefaultTableModel model = (DefaultTableModel) tbSerial.getModel();
        int soLuongHienTai = model.getRowCount();

        int soLuongMoi;
        try {
            soLuongMoi = Integer.parseInt(tf_soLuong.getText().trim());
            if (soLuongMoi < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên dương hợp lệ!");
            tf_soLuong.requestFocus();
            return;
        }

        // Nếu số lượng mới > số hàng hiện có → thêm dòng trống
        if (soLuongMoi > soLuongHienTai) {
            for (int i = soLuongHienTai; i < soLuongMoi; i++) {
                model.addRow(new Object[]{i + 1, ""}); // Thêm STT và Serial
            }
        } // Nếu số lượng mới < số hàng hiện có → xóa dòng dư
        else if (soLuongMoi < soLuongHienTai) {
            for (int i = soLuongHienTai - 1; i >= soLuongMoi; i--) {
                model.removeRow(i);
            }
        }

        // Cập nhật lại STT cho các dòng còn lại
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // Cập nhật cột STT (cột 0)
        }
    }

//    public void loadCBGroup() {
//        nhomsp_data.getListnhomSP().forEach(nhomSP -> {
//            cb_GrProduct1.addItem(nhomSP.getName());
//        });
//    }
//
//    public void loadCBTenSP() {
//        cb_Brand.removeAllItems();
//        if (cb_GrProduct1.getSelectedItem() != null) {
//            String groupName = String.valueOf(cb_GrProduct1.getSelectedItem());
//            this.loaisps = loaisp_data.getLoaiSP(groupName);
//            this.loaisps.forEach(loaisp -> {
//                cb_Brand.addItem(loaisp.getName());
//            });
//        }
//    }
    private void loadChiTietPhieuNhap(int idpn) {
        try {
            System.out.println("idpx: " + idpn);
            DBAccess acc = new DBAccess();

            // Lấy thông tin phiếu xuất, nhóm SP, loại SP
            String sqlPhieu = "SELECT pn.*,ncc.name AS supplier_name, n.name AS group_name, l.name AS category_name "
                    + "FROM PhieuNhap pn "
                    + "JOIN LoaiSP l ON pn.category_id = l.category_id "
                    + "JOIN NhomSP n ON l.group_id = n.group_id "
                    + "JOIN NCC ncc ON pn.supplier_id = ncc.supplier_id "
                    + "WHERE pn.idpn = ?";
            PreparedStatement ps = acc.getConnection().prepareStatement(sqlPhieu);
            ps.setInt(1, idpn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cb_Supplier.setSelectedItem(rs.getString("supplier_name"));
                tf_NYCau.setText(rs.getString("NYCau"));
                tf_ghiChu.setText(rs.getString("ghiChu"));
                tf_soLuong.setText(rs.getString("quantity"));
                tf_giaNhap.setText(rs.getString("price"));
                tf_ngayNhap.setText(rs.getString("ngayNhap"));
                cb_DiaChiKho.setSelectedItem(rs.getString("diaChiKho"));
                tf_soHD.setText(rs.getString("soHoaDon"));
            }
            ps.close();

            // Load danh sách serial theo dòng (2 cột: STT và Serial)
            String sqlCTPX = "SELECT serial FROM CTPN WHERE idpn = ? ORDER BY serial";
            ps = acc.getConnection().prepareStatement(sqlCTPX);
            ps.setInt(1, idpn);
            rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new Object[]{"STT", "Serial"}, 0);
            int stt = 1;
            while (rs.next()) {
                model.addRow(new Object[]{stt++, rs.getString("serial")});
            }
            tbSerial.setModel(model);

            ps.close();
            acc.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải chi tiết phiếu xuất!");
            e.printStackTrace();
        }
    }

    private void customControls() {
        tfTim.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btnTim.doClick();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnTim.doClick();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btnTim.doClick();
            }
        });
        btnTim.setVisible(false);

    }

    private void loadTable_TheoTen(String tenSP) {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        DefaultTableModel dtm = (DefaultTableModel) tbPN.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUNHAP> dssp = pn_data.getSPtheoTen(tenSP);
        for (PHIEUNHAP sp : dssp) {
            Vector vec = new Vector();
            vec.add(sp.getIdpn());
            vec.add(sp.getTenLoai());
            vec.add(sp.getQuantity());
            vec.add(vnFormat.format(sp.getPrice()));// định dạng đơn giá
            vec.add(vnFormat.format(sp.getTongTien())); // định dạng tổng tiền
            vec.add(sp.getNgayNhap());
            vec.add(sp.getSupplier());
            dtm.addRow(vec);
        }
        tbPN.setModel(dtm);
        // Ẩn cột idpx (giả sử là cột đầu tiên - index 0)
        tbPN.getColumnModel().getColumn(0).setMinWidth(0);
        tbPN.getColumnModel().getColumn(0).setMaxWidth(0);
        tbPN.getColumnModel().getColumn(0).setWidth(0);
    }

    private void loadDataTableSP() {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        DefaultTableModel dtm = (DefaultTableModel) tbPN.getModel();
        dtm.setNumRows(0);
        ArrayList<PHIEUNHAP> dssp = pn_data.getListPN();
        if (dssp != null) {
            for (PHIEUNHAP sp : dssp) {
                Vector vec = new Vector();
                vec.add(sp.getIdpn());
                vec.add(sp.getTenLoai());
                vec.add(sp.getQuantity());
                vec.add(vnFormat.format(sp.getPrice()));// định dạng đơn giá
                vec.add(vnFormat.format(sp.getTongTien())); // định dạng tổng tiền
                vec.add(sp.getNgayNhap());
                vec.add(sp.getSupplier());
                dtm.addRow(vec);
            }
            tbPN.setModel(dtm);
            // Ẩn cột idpx (giả sử là cột đầu tiên - index 0)
            tbPN.getColumnModel().getColumn(0).setMinWidth(0);
            tbPN.getColumnModel().getColumn(0).setMaxWidth(0);
            tbPN.getColumnModel().getColumn(0).setWidth(0);
        }
    }
//XOÁ XUẤT HÀNG

    public boolean xoaPhieuNhap(int idpn) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBAccess().getConnection();
            conn.setAutoCommit(false);

            // 1. Lấy danh sách serial cũ từ CTPN
            List<String> serialCu = new ArrayList<>();
            String sqlGetSerialCu = "SELECT serial FROM CTPN WHERE idpn = ?";
            ps = conn.prepareStatement(sqlGetSerialCu);
            ps.setInt(1, idpn);
            rs = ps.executeQuery();
            while (rs.next()) {
                serialCu.add(rs.getString("serial"));
            }
            ps.close();
            rs.close();

            // 2. Kiểm tra nếu có serial cũ đã được xuất thì không cho sửa
            for (String serial : serialCu) {
                String sqlCheckXuat = "SELECT * FROM CTPX WHERE serial = ?";
                ps = conn.prepareStatement(sqlCheckXuat);
                ps.setString(1, serial);
                rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Serial " + serial + " đã được xuất hàng. Không thể xoá!");
                    conn.rollback();
                    return false;
                }
                ps.close();
                rs.close();
            }
            // 2. Xóa chi tiết phiếu nhập
            for (String serial : serialCu) {
                // Xoá CTPN
                String sqlDeleteCTPN = "DELETE FROM CTPN WHERE idpn = ? AND serial = ?";
                ps = conn.prepareStatement(sqlDeleteCTPN);
                ps.setInt(1, idpn);
                ps.setString(2, serial);
                ps.executeUpdate();
                ps.close();

                // Xoá SanPham
                String sqlDeleteSP = "DELETE FROM SanPham WHERE serial = ?";
                ps = conn.prepareStatement(sqlDeleteSP);
                ps.setString(1, serial);
                ps.executeUpdate();
                ps.close();
            }

            // 3. Xóa phiếu nhập
            String deletePN = "DELETE FROM PhieuNhap WHERE idpn = ?";
            ps = conn.prepareStatement(deletePN);
            ps.setInt(1, idpn);
            int deletedRows = ps.executeUpdate();
            ps.close();

            conn.commit();

            if (deletedRows > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa phiếu nhập ");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu nhập để xóa!");
                return false;
            }
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa phiếu nhập: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
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

    //SỬA NHẬP HÀNG
    public boolean suaPhieuNhap(int idpn, int userId, int categoryId, int supplierId, int quantity, long price,
            String ngayNhap, String diaChiKho, String hoaDon, String NYC, String ghiChu, List<String> listSerial) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBAccess().getConnection();
            conn.setAutoCommit(false); // bắt đầu transaction

            // 1. Lấy danh sách serial cũ từ CTPN
            List<String> serialCu = new ArrayList<>();
            String sqlGetSerialCu = "SELECT serial FROM CTPN WHERE idpn = ?";
            ps = conn.prepareStatement(sqlGetSerialCu);
            ps.setInt(1, idpn);
            rs = ps.executeQuery();
            while (rs.next()) {
                serialCu.add(rs.getString("serial"));
            }
            ps.close();
            rs.close();

            // 2. Kiểm tra nếu có serial cũ đã được xuất thì không cho sửa
            for (String serial : serialCu) {
                String sqlCheckXuat = "SELECT * FROM CTPX WHERE serial = ?";
                ps = conn.prepareStatement(sqlCheckXuat);
                ps.setString(1, serial);
                rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Serial " + serial + " đã được xuất hàng. Không thể sửa!");
                    conn.rollback();
                    return false;
                }
                ps.close();
                rs.close();
            }

//            // 3. Kiểm tra serial mới không trùng với bảng SanPham
//            for (String serial : listSerial) {
//                String sqlCheck = "SELECT * FROM SanPham WHERE serial = ?";
//                ps = conn.prepareStatement(sqlCheck);
//                ps.setString(1, serial);
//                rs = ps.executeQuery();
//                if (rs.next()) {
//                    JOptionPane.showMessageDialog(null, "Serial bị trùng trong hệ thống: " + serial);
//                    conn.rollback();
//                    return false;
//                }
//                ps.close();
//                rs.close();
//            }
            // 4. Cập nhật phiếu nhập
            String sqlUpdatePN = "UPDATE PhieuNhap SET user_id=?, category_id=?,supplier_id=?, quantity=?, price=?,  ngayNhap=?,diaChiKho=?,soHoaDon=?, NYCau=?, ghiChu=? WHERE idpn=?";
            ps = conn.prepareStatement(sqlUpdatePN);
            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setInt(3, supplierId);
            ps.setInt(4, quantity);
            ps.setLong(5, price);
            ps.setString(6, ngayNhap);
            ps.setString(7, diaChiKho);
            ps.setString(8, hoaDon);
            ps.setString(9, NYC);
            ps.setString(10, ghiChu);
            ps.setInt(11, idpn);
            ps.executeUpdate();
            ps.close();

            // 5. Xoá serial cũ trong CTPN và bảng SanPham
            for (String serial : serialCu) {
                // Xoá CTPN
                String sqlDeleteCTPN = "DELETE FROM CTPN WHERE idpn = ? AND serial = ?";
                ps = conn.prepareStatement(sqlDeleteCTPN);
                ps.setInt(1, idpn);
                ps.setString(2, serial);
                ps.executeUpdate();
                ps.close();

                // Xoá SanPham
                String sqlDeleteSP = "DELETE FROM SanPham WHERE serial = ?";
                ps = conn.prepareStatement(sqlDeleteSP);
                ps.setString(1, serial);
                ps.executeUpdate();
                ps.close();
            }

            // 6. Thêm lại serial mới vào CTPN và SanPham
            for (String serial : listSerial) {
                // CTPN
                String sqlInsertCTPN = "INSERT INTO CTPN(idpn, serial) VALUES (?, ?)";
                ps = conn.prepareStatement(sqlInsertCTPN);
                ps.setInt(1, idpn);
                ps.setString(2, serial);
                ps.executeUpdate();
                ps.close();

                // SanPham
                String sqlInsertSP = "INSERT INTO SanPham(serial, category_id, supplier_id, status) VALUES (?, ?, ?, 1)";
                ps = conn.prepareStatement(sqlInsertSP);
                ps.setString(1, serial);
                ps.setInt(2, categoryId);
                ps.setInt(3, supplierId);
                ps.executeUpdate();
                ps.close();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void suaNhapHang() {
        try {

            int userId = Session.getInstance().getUserId();
            int selectedRow = tbPN.getSelectedRow();
            String name = tbPN.getValueAt(selectedRow, 1).toString();
            int categoryId = loaisp_data.getCategoryIdByName(name);
            
            String ncc = cb_Supplier.getSelectedItem().toString();
            int ncc_id = ncc_data.getNCCId(ncc);
            Long price = Long.parseLong(tf_giaNhap.getText().trim());
            String NYC = tf_NYCau.getText().trim();
            String hd = tf_soHD.getText().trim();
            String diaChiKho = (String) cb_DiaChiKho.getSelectedItem();
            String ghiChu = tf_ghiChu.getText().trim();

            int soLuong = Integer.parseInt(tf_soLuong.getText().trim());
            // Lấy danh sách serial từ bảng
            List<String> listSerial = new ArrayList<>();
            //ngayNhap
            //ngayXuat
            LocalDate ngayNhap = LocalDate.now();
            String ngaynhapStr = ngayNhap.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            DefaultTableModel model = (DefaultTableModel) tbSerial.getModel();
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                Object value = model.getValueAt(i, 1); // Giờ Serial ở cột 1 (cột 2)
                if (value != null) {
                    String serial = value.toString().trim();
                    if (!serial.isEmpty()) {
                        listSerial.add(serial);
                    }
                }
            }

            // Gọi xử lý
            int idpn = Integer.parseInt(tbPN.getValueAt(selectedRow, 0).toString()); // Cột 0 là idpn
            boolean ok = suaPhieuNhap(idpn, userId, categoryId, ncc_id, soLuong, price, ngaynhapStr, hd, diaChiKho, NYC, ghiChu, listSerial);

            if (ok) {
                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                model.setRowCount(0); // clear table
            } else {
                JOptionPane.showMessageDialog(null, "Nhập hàng thất bại! Kiểm tra lại serial hoặc dữ liệu.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPN = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        tfTim = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSerial = new javax.swing.JTable();
        btn_Luu = new javax.swing.JButton();
        tf_soLuong = new javax.swing.JTextField();
        tf_ngayNhap = new javax.swing.JTextField();
        tf_giaNhap = new javax.swing.JTextField();
        btnXoa = new javax.swing.JButton();
        tf_ghiChu = new javax.swing.JTextField();
        tf_NYCau = new javax.swing.JTextField();
<<<<<<< HEAD
        tf_soHD = new javax.swing.JTextField();
        cb_DiaChiKho = new javax.swing.JComboBox<>();
        cb_Supplier = new javax.swing.JComboBox<>();
=======
        tf_ngayNhap1 = new javax.swing.JTextField();
        cb_supplier = new javax.swing.JComboBox<>();
        cb_kho = new javax.swing.JComboBox<>();
        txt_tenSP = new javax.swing.JTextField();
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DANH SÁCH PHIẾU NHẬP");

        tbPN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbPN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
<<<<<<< HEAD
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Hoá Đơn", "Tên", "Số Lượng", "Đơn Giá", "Thành Tiền", "Ngày Nhập", "Nhà Cung Cấp", "Địa Chỉ Kho"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, true
=======
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "Số Lượng", "Đơn Giá", "Thành Tiền", "Ngày Nhập", "Nhà Cung Cấp", "Số hoá đơn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbPN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPNMouseClicked(evt);
            }
        });
        tbPN.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tbPNComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(tbPN);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Tìm kiếm :");

        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        tbSerial.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbSerial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Serial"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbSerial);
        if (tbSerial.getColumnModel().getColumnCount() > 0) {
            tbSerial.getColumnModel().getColumn(0).setResizable(false);
            tbSerial.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbSerial.getColumnModel().getColumn(1).setResizable(false);
            tbSerial.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        btn_Luu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LuuActionPerformed(evt);
            }
        });

        tf_soLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_soLuong.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Số Lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        tf_soLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_soLuongActionPerformed(evt);
            }
        });

        tf_ngayNhap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ngayNhap.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Ngày Nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        tf_giaNhap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
<<<<<<< HEAD
        tf_giaNhap.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giá Nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_giaNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_giaNhapActionPerformed(evt);
            }
        });
=======
        tf_giaNhap.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giá Nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        tf_ghiChu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ghiChu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ghi Chú", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        tf_ghiChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_ghiChuActionPerformed(evt);
            }
        });

        tf_NYCau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_NYCau.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Người Yêu Cầu Nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        tf_NYCau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_NYCauActionPerformed(evt);
            }
        });

<<<<<<< HEAD
        tf_soHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_soHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hoá Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_soHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_soHDActionPerformed(evt);
            }
        });

        cb_DiaChiKho.setEditable(true);
        cb_DiaChiKho.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_DiaChiKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Miền Bắc", "Miền Trung", "Miền Nam" }));
        cb_DiaChiKho.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Địa Chỉ Kho", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_DiaChiKho.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        cb_Supplier.setEditable(true);
        cb_Supplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Supplier.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhà cung cấp", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_Supplier.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
=======
        tf_ngayNhap1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ngayNhap1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Số hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        cb_supplier.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhà cung cấp", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        cb_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_supplierActionPerformed(evt);
            }
        });

        cb_kho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Miền Nam", "Miền Trung", "Miền Bắc" }));
        cb_kho.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Địa chỉ kho", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        cb_kho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_khoActionPerformed(evt);
            }
        });

        txt_tenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_tenSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        txt_tenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tenSPActionPerformed(evt);
            }
        });
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
<<<<<<< HEAD
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 44, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
=======
                        .addGap(14, 14, 14)
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTim, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)))
                        .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
<<<<<<< HEAD
                        .addGap(119, 119, 119)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(tf_soLuong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                        .addComponent(tf_NYCau, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tf_ngayNhap))
                                    .addComponent(cb_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tf_soHD, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tf_ghiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cb_DiaChiKho, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tf_giaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
=======
                        .addGap(36, 36, 36)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tf_soLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                            .addComponent(tf_giaNhap)
                                            .addComponent(tf_ngayNhap))
                                        .addGap(32, 32, 32)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cb_supplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(tf_ngayNhap1)
                                            .addComponent(cb_kho, 0, 200, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_tenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(tf_NYCau, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tf_ghiChu))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
<<<<<<< HEAD
=======
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_tenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_NYCau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_giaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_ngayNhap1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_ngayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_kho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tf_ghiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfTim, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTim)
                            .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
<<<<<<< HEAD
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_DiaChiKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_NYCau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_ghiChu))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_giaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_ngayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_soHD, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
=======
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGap(16, 16, 16)
                .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        loadTable_TheoTen(tfTim.getText().trim());
    }//GEN-LAST:event_btnTimActionPerformed

    private void tbPNComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tbPNComponentShown
        loadDataTableSP();
    }//GEN-LAST:event_tbPNComponentShown

    private void tf_ghiChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_ghiChuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_ghiChuActionPerformed

    private void tf_NYCauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_NYCauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_NYCauActionPerformed

<<<<<<< HEAD
    private void tf_soHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_soHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_soHDActionPerformed

    private void tf_giaNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_giaNhapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_giaNhapActionPerformed
=======
    private void tf_soLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_soLuongActionPerformed
        // TODO add your handling code here:
        capNhatBangSerialTheoSoLuong();
    }//GEN-LAST:event_tf_soLuongActionPerformed

    private void cb_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_supplierActionPerformed

    private void cb_khoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_khoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_khoActionPerformed

    private void txt_tenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tenSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tenSPActionPerformed
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3

    private void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbPN.getSelectedRow();
        if (selectedRow >= 0) {
            int idpn = Integer.parseInt(tbPN.getValueAt(selectedRow, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn sửa phiếu nhập này?",
                    "Xác nhận sửa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                suaNhapHang();
                loadDataTableSP(); // Tải lại danh sách phiếu nhập
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập cần sửa!");
        }
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tbPN.getSelectedRow();
        if (selectedRow >= 0) {
            int idpn = Integer.parseInt(tbPN.getValueAt(selectedRow, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa phiếu nhập này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = xoaPhieuNhap(idpn);
                if (success) {
                    loadDataTableSP(); // Tải lại danh sách phiếu nhập
                    DefaultTableModel model = (DefaultTableModel) tbSerial.getModel();
                    model.setRowCount(0); // Xóa dữ liệu bảng serial
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu xuất cần xóa!");
        }
    }


    private void tbPNMouseClicked(java.awt.event.MouseEvent evt) {

        int selectedRow = tbPN.getSelectedRow();
        if (selectedRow >= 0) {
            int idpn = Integer.parseInt(tbPN.getValueAt(selectedRow, 0).toString()); // Cột 0 là idpx
            System.out.println("CLICKED idpn = " + idpn); // ✅ để test có click không

            // Gọi form sửa hoặc load dữ liệu
            loadChiTietPhieuNhap(idpn); // hoặc gọi form mới: new SuaXuatHang(idpx)
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DSPhieuNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DSPhieuNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DSPhieuNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DSPhieuNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DSPhieuNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btn_Luu;
<<<<<<< HEAD
    private javax.swing.JComboBox<String> cb_DiaChiKho;
    private javax.swing.JComboBox<String> cb_Supplier;
=======
    private javax.swing.JComboBox<String> cb_kho;
    private javax.swing.JComboBox<String> cb_supplier;
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbPN;
    private javax.swing.JTable tbSerial;
    private javax.swing.JTextField tfTim;
    private javax.swing.JTextField tf_NYCau;
    private javax.swing.JTextField tf_ghiChu;
    private javax.swing.JTextField tf_giaNhap;
    private javax.swing.JTextField tf_ngayNhap;
<<<<<<< HEAD
    private javax.swing.JTextField tf_soHD;
=======
    private javax.swing.JTextField tf_ngayNhap1;
>>>>>>> ea0c4cd7a0ac5fb058dbaa211cc8ec988d832ef3
    private javax.swing.JTextField tf_soLuong;
    private javax.swing.JTextField txt_tenSP;
    // End of variables declaration//GEN-END:variables
}
