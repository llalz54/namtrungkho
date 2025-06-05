package namtrung.quanlykho;

import ConDB.DBAccess;
import DAO.LOAISP_DATA;
import DAO.NCC_DATA;
import DAO.NHOMSP_DATA;
import DAO.NumberDocumentFilter;
import DAO.OTHER_DATA;
import DAO.PHIEUXUAT_DATA;
import DAO.SANPHAM_DATA;
import DAO.Session;
import DTO.LOAISP;
import DTO.NCC;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author Admin
 */
public class XuatHang extends javax.swing.JPanel {

    private NHOMSP_DATA nhomsp_data = new NHOMSP_DATA();
    private LOAISP_DATA loaisp_data = new LOAISP_DATA();
    private ArrayList<LOAISP> loaisps;
    private ArrayList<NCC> nccs;
    private NCC_DATA sp_data = new NCC_DATA();

    public XuatHang() {
        initComponents();
        loadCBGroup();
        loadCBTenSP();
        loadCBNCC();
        OTHER_DATA.customTable(tb_SerialConfirm);
        OTHER_DATA.customTable(tb_SerialNCC);
        tf_ngayXuat.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tf_ngayXuatHD.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        // Đặt DocumentFilter cho tf_giaXuat
        ((AbstractDocument) tf_giaXuat.getDocument()).setDocumentFilter(new NumberDocumentFilter());

    }

    private void clearForm() {
        tf_soLuong.setText("");
        tf_giaXuat.setText("");
        tf_khachHang.setText("");
        tf_NYC.setText("");
        tf_diaChi.setText("");
        tf_HD.setText("");
        tf_ngayXuat.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        cb_GrProduct1.setSelectedIndex(0);
        cb_TenSP.setSelectedIndex(0);
        cb_Time.setSelectedIndex(0);
        cb_NCC1.setSelectedIndex(0);
        tf_DviXuat.setText("");
        tf_HD.setText("");
        tf_ngayXuatHD.setText("");
        // Xóa bảng serial
        DefaultTableModel model = (DefaultTableModel) tb_SerialNCC.getModel();
        model.setRowCount(0);
        // Xóa bảng serial tổng
        DefaultTableModel model1 = (DefaultTableModel) tb_SerialConfirm.getModel();
        model.setRowCount(0);
    }

    public void loadCBNCC() {
        cb_NCC1.removeAllItems();
        nccs = sp_data.getListNCC(); // lưu lại để dùng sau nếu muốn

        for (NCC ncc : nccs) {
            cb_NCC1.addItem(ncc); // add object NCC chứ không phải string
        }
    }

    public void loadCBGroup() {
        nhomsp_data.getListnhomSP().forEach(nhomSP -> {
            cb_GrProduct1.addItem(nhomSP.getName());
        });
    }

    public void loadCBTenSP() {
        cb_TenSP.removeAllItems();
        if (cb_GrProduct1.getSelectedItem() != null) {
            String groupName = String.valueOf(cb_GrProduct1.getSelectedItem());
            this.loaisps = loaisp_data.getLoaiSP(groupName);
            this.loaisps.forEach(loaisp -> {
                cb_TenSP.addItem(loaisp.getName());
            });
        }
    }
// Kiểm tra đã có serial trong bảng confirm chưa

    private boolean isSerialAlreadyConfirmed(String serial) {
        DefaultTableModel model = (DefaultTableModel) tb_SerialConfirm.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (serial.equals(model.getValueAt(i, 1))) {
                return true;
            }
        }
        return false;
    }

// Xoá serial khỏi bảng confirm
    private void removeSerialFromConfirm(String serial) {
        DefaultTableModel model = (DefaultTableModel) tb_SerialConfirm.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (serial.equals(model.getValueAt(i, 1))) {
                model.removeRow(i);
                return;
            }
        }
    }

// Lấy số lượng giới hạn từ textfield
    private int getSoLuongToiDa() {
        try {
            return Integer.parseInt(tf_soLuong.getText().trim());
        } catch (Exception e) {
            return Integer.MAX_VALUE; // không giới hạn nếu lỗi
        }
    }

// Cập nhật lại STT cho bảng confirm
    private void updateSTT(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // STT ở cột 0
        }
    }

// Lấy model bảng Confirm
    private DefaultTableModel getConfirmTableModel() {
        return (DefaultTableModel) tb_SerialConfirm.getModel();
    }

    private void loadSerialTheoNCCVaLoaiSP(int categoryId) {
        NCC selectedNCC = (NCC) cb_NCC1.getSelectedItem();
        int nccId = selectedNCC.getSupplier_id();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"STT", "Serial", "NCC", "Chọn"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        try (Connection conn = new DBAccess().getConnection()) {
            String sql = "SELECT serial FROM SanPham WHERE category_id = ? AND supplier_id = ? AND status = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ps.setInt(2, nccId);
            ResultSet rs = ps.executeQuery();
            int stt = 1;
            // ✅ Lấy danh sách serial đã được chọn
            Set<String> selectedSerials = new HashSet<>();
            DefaultTableModel confirmModel = (DefaultTableModel) tb_SerialConfirm.getModel();
            for (int i = 0; i < confirmModel.getRowCount(); i++) {
                Object val = confirmModel.getValueAt(i, 1);
                if (val != null) {
                    selectedSerials.add(val.toString());
                }
            }

            // ✅ Thêm vào bảng với trạng thái checkbox đúng
            while (rs.next()) {
                String serial = rs.getString("serial");
                boolean isSelected = selectedSerials.contains(serial);
                model.addRow(new Object[]{stt++, serial, selectedNCC.getName(), isSelected});
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        tb_SerialNCC.setModel(model);
        tb_SerialNCC.getColumnModel().getColumn(0).setPreferredWidth(50);
        tb_SerialNCC.getColumnModel().getColumn(1).setPreferredWidth(200);
        tb_SerialNCC.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Thêm checkbox vào header
        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setOpaque(false);
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        JPanel headerPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(headerCheckBox);
        tb_SerialNCC.getColumnModel().getColumn(3).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerPanel);

        // Bảng confirm
        DefaultTableModel confirmModel = (DefaultTableModel) tb_SerialConfirm.getModel();
        //tb_SerialConfirm.setModel(confirmModel);

        // Bắt sự kiện click checkbox từng dòng
        tb_SerialNCC.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col != 3 || row < 0 || row >= model.getRowCount()) {
                return;
            }

            Object checkVal = model.getValueAt(row, 3);
            Object serialVal = model.getValueAt(row, 1);
            Object nccVal = model.getValueAt(row, 2);

            if (!(checkVal instanceof Boolean) || serialVal == null || nccVal == null) {
                return;
            }

            boolean isChecked = (Boolean) checkVal;
            String serial = serialVal.toString();
            String ncc = nccVal.toString();

            if (isChecked) {
                // Kiểm tra giới hạn số lượng
                if (getConfirmTableModel().getRowCount() >= getSoLuongToiDa()) {
                    JOptionPane.showMessageDialog(this, "Đã đạt số lượng tối đa cho phép.");
                    model.setValueAt(false, row, 3); // bỏ lại checkbox
                    return;
                }
                // Thêm vào bảng xác nhận nếu chưa có
                boolean exists = false;
                for (int i = 0; i < confirmModel.getRowCount(); i++) {
                    if (serial.equals(confirmModel.getValueAt(i, 1))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    int stt = confirmModel.getRowCount() + 1;
                    confirmModel.addRow(new Object[]{stt, serial, ncc});
                }
            } else {
                // Bỏ khỏi bảng xác nhận nếu bỏ tick
                for (int i = 0; i < confirmModel.getRowCount(); i++) {
                    if (serial.equals(confirmModel.getValueAt(i, 1))) {
                        confirmModel.removeRow(i);
// Cập nhật lại STT
                        for (int j = 0; j < confirmModel.getRowCount(); j++) {
                            confirmModel.setValueAt(j + 1, j, 0); // cột 0 là STT
                        }
                        break;
                    }
                }
            }

        });

        // Bắt sự kiện click vào header để chọn tất cả
        tb_SerialNCC.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tb_SerialNCC.columnAtPoint(e.getPoint());
                if (col == 3) {
                    boolean newState = !headerCheckBox.isSelected();
                    headerCheckBox.setSelected(newState);

                    String selectedNCC = cb_NCC1.getSelectedItem().toString(); // NCC đang chọn

                    int max = 0;
                    try {
                        max = Integer.parseInt(tf_soLuong.getText().trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng hợp lệ.");
                        return;
                    }

                    int count = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String nccRow = model.getValueAt(i, 2).toString(); // Cột NCC

                        if (nccRow.equals(selectedNCC)) {
                            if (count < max && newState) {
                                model.setValueAt(true, i, 3);
                                count++;
                            } else {
                                model.setValueAt(false, i, 3);
                            }
                        }
                    }

                    tb_SerialNCC.getTableHeader().repaint(); // cập nhật lại header
                }
            }
        });
    }
//XUẤT HÀNG

    public void thucHienXuatHang() {
        try {

            int userId = Session.getInstance().getUserId();
            System.out.println("id user = " + userId);
            int categoryId = 0;
            for (LOAISP loaisp : loaisps) {
                if (loaisp.getName().equals(cb_TenSP.getSelectedItem())) {
                    categoryId = loaisp.getCategoryID();
                    break;
                }
            }

            long price;
            try {
                // Lấy text, xóa dấu chấm và chuyển thành số
                String priceText = tf_giaXuat.getText().replace(".", "");
                price = Long.parseLong(priceText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Giá xuất không hợp lệ");
                return;
            }
            String customer = tf_khachHang.getText().trim();
            String address = tf_diaChi.getText().trim();
            String NYC = tf_NYC.getText().trim();
            String ghiChu = tf_ghiChu.getText().trim();
            int soLuong = Integer.parseInt(tf_soLuong.getText().trim());
            // Lấy danh sách serial từ bảng
            List<String> listSerial = new ArrayList<>();
            //ngayXuat
            LocalDate ngayXuat = LocalDate.now();
            String ngayXuatStr = ngayXuat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            //HoaDon

            String hoaDon = tf_HD.getText().trim();
            //DonViXuat
            String dviXuat = tf_DviXuat.getText().trim();
            //ngayXuatHD
            String ngayXuatHD_Str = tf_ngayXuatHD.getText().trim();
            String ngayXuatHD = null;
            if (!ngayXuatHD_Str.isEmpty()) {
                ngayXuatHD = LocalDate.parse(ngayXuatHD_Str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
            }
            // Ngày bắt đầu bảo hành (start_date)
            LocalDate startDate = LocalDate.parse(tf_ngayXuat.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Lấy thời gian bảo hành từ comboBox
            String selectedWarranty = (String) cb_Time.getSelectedItem(); // "12 tháng"
            int months = Integer.parseInt(selectedWarranty.split(" ")[0]);

            // Tính end_date
            LocalDate endDate = startDate.plusMonths(months);
            String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Lặp serial
            DefaultTableModel model = (DefaultTableModel) tb_SerialConfirm.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {

                String serial = model.getValueAt(i, 1).toString(); // cột Serial
                listSerial.add(serial);

            }

            if (listSerial.size() != soLuong) {
                JOptionPane.showMessageDialog(null, "Số lượng serial được chọn không khớp với số lượng cần xuất.");
                return;
            }

            // Gọi xử lý
            PHIEUXUAT_DATA data = new PHIEUXUAT_DATA();
            boolean ok = data.xuatHang(userId, categoryId, soLuong, price, customer, address, NYC, ghiChu, ngayXuatStr, hoaDon, dviXuat, ngayXuatHD, startDate.toString(), endDateStr, listSerial);
            if (ok) {
                JOptionPane.showMessageDialog(null, "Xuất hàng thành công!");
                clearForm(); // clear table
            } else {
                JOptionPane.showMessageDialog(null, "Xuất hàng thất bại! Kiểm tra lại serial hoặc dữ liệu.");
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
        cb_GrProduct1 = new javax.swing.JComboBox<>();
        cb_TenSP = new javax.swing.JComboBox<>();
        cb_Time = new javax.swing.JComboBox<>();
        cb_NCC1 = new javax.swing.JComboBox<>();
        tf_giaXuat = new javax.swing.JTextField();
        tf_soLuong = new javax.swing.JTextField();
        tf_NYC = new javax.swing.JTextField();
        tf_HD = new javax.swing.JTextField();
        tf_ngayXuat = new javax.swing.JTextField();
        tf_khachHang = new javax.swing.JTextField();
        tf_diaChi = new javax.swing.JTextField();
        btn_XacNhan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_SerialNCC = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_SerialConfirm = new javax.swing.JTable();
        btn_Luu = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tf_ghiChu = new javax.swing.JTextField();
        tf_DviXuat = new javax.swing.JTextField();
        tf_ngayXuatHD = new javax.swing.JTextField();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        cb_GrProduct1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_GrProduct1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhóm sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_GrProduct1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_GrProduct1ItemStateChanged(evt);
            }
        });

        cb_TenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_TenSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên SP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_TenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_TenSPActionPerformed(evt);
            }
        });

        cb_Time.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Time.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12 tháng", "24 tháng", "36 tháng" }));
        cb_Time.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bảo Hành", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_Time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_TimeActionPerformed(evt);
            }
        });

        cb_NCC1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_NCC1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhà Cung Cấp", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        cb_NCC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_NCC1ActionPerformed(evt);
            }
        });

        tf_giaXuat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_giaXuat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giá Xuất", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_giaXuat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_giaXuatKeyTyped(evt);
            }
        });

        tf_soLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_soLuong.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Số Lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_soLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_soLuongKeyTyped(evt);
            }
        });

        tf_NYC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_NYC.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Người Xuất", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_NYC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_NYCKeyTyped(evt);
            }
        });

        tf_HD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_HD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hoá Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tf_ngayXuat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ngayXuat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ngày Xuất", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_ngayXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_ngayXuatActionPerformed(evt);
            }
        });

        tf_khachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_khachHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khách Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_khachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_khachHangActionPerformed(evt);
            }
        });

        tf_diaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_diaChi.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Địa Chỉ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N
        tf_diaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_diaChiActionPerformed(evt);
            }
        });

        btn_XacNhan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_XacNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/check.png"))); // NOI18N
        btn_XacNhan.setText("Xác Nhận");
        btn_XacNhan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_XacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XacNhanActionPerformed(evt);
            }
        });

        tb_SerialNCC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_SerialNCC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tb_SerialNCC);

        tb_SerialConfirm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_SerialConfirm.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Serial", "NCC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tb_SerialConfirm);

        btn_Luu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_Luu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LuuActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("BẢNG SERIAL THEO NHÀ CUNG CẤP");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("BẢNG SERIAL ĐỂ XUẤT HÀNG");

        tf_ghiChu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ghiChu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ghi Chú", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tf_DviXuat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_DviXuat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn Vị Xuất", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tf_ngayXuatHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_ngayXuatHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ngày Xuất HD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(129, 129, 129))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_XacNhan, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Luu)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(tf_soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cb_GrProduct1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tf_ngayXuat)))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cb_TenSP, 0, 212, Short.MAX_VALUE)
                            .addComponent(tf_giaXuat)
                            .addComponent(tf_HD))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tf_NYC, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_khachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_NCC1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tf_ghiChu, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb_Time, javax.swing.GroupLayout.Alignment.LEADING, 0, 214, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tf_DviXuat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tf_ngayXuatHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(tf_diaChi))))
                .addGap(66, 66, 66))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_GrProduct1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_NCC1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_Time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_DviXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_soLuong)
                    .addComponent(tf_giaXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_NYC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_ghiChu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_ngayXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_ngayXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_khachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_HD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_XacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(372, 372, 372))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 700, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cb_GrProduct1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_GrProduct1ItemStateChanged
        loadCBTenSP();
    }//GEN-LAST:event_cb_GrProduct1ItemStateChanged

    private void cb_TenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_TenSPActionPerformed

    }//GEN-LAST:event_cb_TenSPActionPerformed

    private void cb_TimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_TimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_TimeActionPerformed

    private void cb_NCC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_NCC1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_NCC1ActionPerformed

    private void tf_ngayXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_ngayXuatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_ngayXuatActionPerformed

    private void tf_khachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_khachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_khachHangActionPerformed

    private void tf_diaChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_diaChiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_diaChiActionPerformed

    private void btn_XacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XacNhanActionPerformed
        String soLuongStr = tf_soLuong.getText().trim();
        String giaXuat = tf_giaXuat.getText().replace(".", "");
        String nyc = tf_NYC.getText().trim();

        if (soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng cần xuất.");
            return;
        }
        if (giaXuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá xuất.");
            return;
        }
        if (nyc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập người yêu cầu xuất hàng.");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ. Vui lòng nhập số nguyên.");
            return;
        }
     
        long gia;
        try {
            gia = Long.parseLong(giaXuat);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá xuất không hợp lệ. Vui lòng nhập số nguyên.");
            return;
        }

        String tenLoaiSP = (String) cb_TenSP.getSelectedItem();

        for (LOAISP loaisp : loaisps) {
            if (loaisp.getName().equals(tenLoaiSP)) {
                loadSerialTheoNCCVaLoaiSP(loaisp.getCategoryID());
                break;
            }
        }

    }//GEN-LAST:event_btn_XacNhanActionPerformed

    private void tf_soLuongKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_soLuongKeyTyped
        char c = evt.getKeyChar();
        // Nếu không phải số và không phải phím xóa (backspace), thì hủy ký tự nhập
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume(); // chặn không cho nhập
            java.awt.Toolkit.getDefaultToolkit().beep(); // kêu beep để báo
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên!");
        }
    }//GEN-LAST:event_tf_soLuongKeyTyped

    private void tf_giaXuatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_giaXuatKeyTyped
        char c = evt.getKeyChar();
        // Nếu không phải số và không phải phím xóa (backspace), thì hủy ký tự nhập
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume(); // chặn không cho nhập
            java.awt.Toolkit.getDefaultToolkit().beep(); // kêu beep để báo
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên!");

        }
    }//GEN-LAST:event_tf_giaXuatKeyTyped

    private void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LuuActionPerformed
        if (tb_SerialConfirm.isEditing()) {
            tb_SerialConfirm.getCellEditor().stopCellEditing();
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn chắc chắn muốn lưu?",
                "Xác nhận lưu",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            thucHienXuatHang();
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy lưu!");
        }
    }//GEN-LAST:event_btn_LuuActionPerformed

    private void tf_NYCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_NYCKeyTyped
        char c = evt.getKeyChar();
        String input = tf_NYC.getText().trim();
        // Nếu không phải số và không phải phím xóa (backspace), thì hủy ký tự nhập
        if (c != '\b' && !Character.isLetter(c)) {
            evt.consume(); // chặn không cho nhập
            java.awt.Toolkit.getDefaultToolkit().beep(); // kêu beep để báo
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chữ cái!");
        }

    }//GEN-LAST:event_tf_NYCKeyTyped

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
            java.util.logging.Logger.getLogger(XuatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XuatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XuatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XuatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new XuatHang().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_XacNhan;
    private javax.swing.JComboBox<String> cb_GrProduct1;
    private javax.swing.JComboBox<NCC> cb_NCC1;
    private javax.swing.JComboBox<String> cb_TenSP;
    private javax.swing.JComboBox<String> cb_Time;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tb_SerialConfirm;
    private javax.swing.JTable tb_SerialNCC;
    private javax.swing.JTextField tf_DviXuat;
    private javax.swing.JTextField tf_HD;
    private javax.swing.JTextField tf_NYC;
    private javax.swing.JTextField tf_diaChi;
    private javax.swing.JTextField tf_ghiChu;
    private javax.swing.JTextField tf_giaXuat;
    private javax.swing.JTextField tf_khachHang;
    private javax.swing.JTextField tf_ngayXuat;
    private javax.swing.JTextField tf_ngayXuatHD;
    private javax.swing.JTextField tf_soLuong;
    // End of variables declaration//GEN-END:variables
}
