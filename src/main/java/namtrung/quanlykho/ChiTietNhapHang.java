package namtrung.quanlykho;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DAO.CTPN_DATA;
import DAO.LOAISP_DATA;
import DAO.NCC_DATA;
import DAO.NHOMSP_DATA;
import DAO.NumberDocumentFilter;
import javax.swing.table.DefaultTableModel;
import DAO.OTHER_DATA;
import DAO.PHIEUNHAP_DATA;
import DAO.Session;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.text.AbstractDocument;
import utils.StringHelper;
import utils.UpperCaseEditor;

public class ChiTietNhapHang extends JPanel {

    private CTPN_DATA ctpn_Data = new CTPN_DATA();
    private PHIEUNHAP_DATA pn_Data = new PHIEUNHAP_DATA();
    private NHOMSP_DATA gr_Data = new NHOMSP_DATA();
    private LOAISP_DATA loaiSP_Data = new LOAISP_DATA();
    private NCC_DATA ncc_Data = new NCC_DATA();

    private String action = "";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ChiTietNhapHang() {
        initComponents();
        OTHER_DATA.loadCBDM(cb_GrProduct);
        OTHER_DATA.load_Cb_Brand(cb_Brand);
        OTHER_DATA.load_Cb_Supplier(cb_Supplier);

        txt_ngayNhap.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Đặt DocumentFilter cho txt_price
        ((AbstractDocument) txt_price.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        cb_Product.addActionListener(e -> {
            Object selectedItem = cb_Product.getSelectedItem();
            if (selectedItem != null) {
                cb_Product.setToolTipText(selectedItem.toString());
            }
        });
        cb_Product.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    label.setText(value.toString());
                    label.setToolTipText(value.toString()); // Tooltip từng option
                }
                return label;
            }
        });
    }

    private void completeSave() {
        txt_GhiChu.setText("");
        txt_NgYeuCau.setText("");
        txt_Quantity.setText("");
        txt_SoHoaDon.setText("");
        txt_ngayNhap.setText("");
        txt_price.setText("");

        // Xoá dữ liệu trên bảng Serial
        DefaultTableModel dtm = (DefaultTableModel) tb_CTPN.getModel();
        dtm.setRowCount(0);
        action = "";
    }

    private void create_TB_CTPN(int quantity) {

        int column = 10;
        int row = (int) Math.ceil((double) quantity / 10);

        String[] columnNames = new String[column];
        for (int i = 0; i < column; i++) {
            columnNames[i] = "" + (i + 1);
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                int cellIndex = rowIndex * column + columnIndex;
                return cellIndex < quantity; // chỉ các ô trong số lượng cho phép mới được chỉnh
            }
        };

        for (int i = 0; i < row; i++) {
            model.addRow(new Object[column]);
        }

        tb_CTPN.setModel(model);
        tb_CTPN.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (int i = 0; i < tb_CTPN.getColumnCount(); i++) {
            tb_CTPN.getColumnModel().getColumn(i).setCellEditor(new UpperCaseEditor());
        }

        String notification = "Hãy nhập Serial cho các thiết bị vừa nhập";
        JOptionPane.showMessageDialog(null, notification, "", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isTableSerialFilled(JTable table, int quantity) {
        int columnCount = table.getColumnCount();
        int rowCount = table.getRowCount();

        int filledCount = 0;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                int cellIndex = row * columnCount + col;
                if (cellIndex >= quantity) {
                    return filledCount == quantity;
                }
                Object value = table.getValueAt(row, col);
                if (value != null && !value.toString().trim().isEmpty()) {
                    filledCount++;
                }
            }
        }

        return filledCount == quantity;
    }

    private void loadCB_ListSP(String gr, String brand) {
        cb_Product.removeAllItems();
        int group_ID = gr_Data.name_to_ID(gr);
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT name FROM LoaiSP WHERE group_id = '" + group_ID + "' AND BRAND ='" + brand + "' AND status = 1");
            while (rs.next()) {
                cb_Product.addItem(rs.getString("name").trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCB_ListSP!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNhapHang = new javax.swing.JPanel();
        cb_Brand = new javax.swing.JComboBox<>();
        cb_Supplier = new javax.swing.JComboBox<>();
        txt_Quantity = new javax.swing.JTextField();
        txt_ngayNhap = new javax.swing.JTextField();
        txt_price = new javax.swing.JTextField();
        btn_Confirm = new javax.swing.JButton();
        btn_GhiPhieu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_CTPN = new javax.swing.JTable();
        cb_Product = new javax.swing.JComboBox<>();
        cb_GrProduct = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cb_DiaChiKho = new javax.swing.JComboBox<>();
        txt_GhiChu = new javax.swing.JTextArea();
        txt_NgYeuCau = new javax.swing.JTextField();
        txt_SoHoaDon = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNhapHang.setBackground(new java.awt.Color(255, 255, 255));
        panelNhapHang.setPreferredSize(new java.awt.Dimension(1000, 700));

        cb_Brand.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cb_Brand.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hãng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        cb_Brand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_BrandActionPerformed(evt);
            }
        });

        cb_Supplier.setEditable(true);
        cb_Supplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cb_Supplier.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhà cung cấp", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        cb_Supplier.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_Quantity.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_Quantity.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Số lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txt_Quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_QuantityKeyTyped(evt);
            }
        });

        txt_ngayNhap.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_ngayNhap.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ngày nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        txt_price.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_price.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giá nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txt_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_priceKeyTyped(evt);
            }
        });

        btn_Confirm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Confirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/check.png"))); // NOI18N
        btn_Confirm.setText("Xác Nhận");
        btn_Confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ConfirmActionPerformed(evt);
            }
        });

        btn_GhiPhieu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_GhiPhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        btn_GhiPhieu.setText("Ghi Phiếu");
        btn_GhiPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GhiPhieuActionPerformed(evt);
            }
        });

        tb_CTPN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tb_CTPN.setOpaque(false);
        tb_CTPN.setPreferredSize(new java.awt.Dimension(790, 400));
        tb_CTPN.setRowHeight(40);
        tb_CTPN.setRowSelectionAllowed(false);
        tb_CTPN.setShowGrid(true);
        jScrollPane1.setViewportView(tb_CTPN);
        if (tb_CTPN.getColumnModel().getColumnCount() > 0) {
            tb_CTPN.getColumnModel().getColumn(0).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(1).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(2).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(3).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(4).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(5).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(6).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(7).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(8).setResizable(false);
            tb_CTPN.getColumnModel().getColumn(9).setResizable(false);
        }

        cb_Product.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cb_Product.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        cb_GrProduct.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cb_GrProduct.setMaximumRowCount(10);
        cb_GrProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhóm sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        cb_GrProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_GrProductActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("QUẢN LÍ NHẬP HÀNG");

        cb_DiaChiKho.setEditable(true);
        cb_DiaChiKho.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cb_DiaChiKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Miền Nam", "Miền Trung", "Miền Bắc" }));
        cb_DiaChiKho.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kho", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        cb_DiaChiKho.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txt_GhiChu.setColumns(20);
        txt_GhiChu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_GhiChu.setRows(5);
        txt_GhiChu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ghi chú", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        txt_NgYeuCau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_NgYeuCau.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Người yêu cầu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txt_NgYeuCau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_NgYeuCauKeyTyped(evt);
            }
        });

        txt_SoHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txt_SoHoaDon.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Số hoá đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        javax.swing.GroupLayout panelNhapHangLayout = new javax.swing.GroupLayout(panelNhapHang);
        panelNhapHang.setLayout(panelNhapHangLayout);
        panelNhapHangLayout.setHorizontalGroup(
            panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhapHangLayout.createSequentialGroup()
                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhapHangLayout.createSequentialGroup()
                        .addGap(403, 403, 403)
                        .addComponent(jLabel1))
                    .addGroup(panelNhapHangLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cb_Brand, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_GrProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_Product, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NgYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Confirm, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(48, 48, 48)
                        .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelNhapHangLayout.createSequentialGroup()
                                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cb_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_DiaChiKho, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_SoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_ngayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(59, 59, 59)
                                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_GhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btn_GhiPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelNhapHangLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelNhapHangLayout.setVerticalGroup(
            panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhapHangLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelNhapHangLayout.createSequentialGroup()
                        .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_GrProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_Supplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_Brand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_SoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelNhapHangLayout.createSequentialGroup()
                                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cb_Product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cb_DiaChiKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_NgYeuCau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_ngayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txt_GhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68))
                    .addGroup(panelNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_GhiPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_Confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        add(panelNhapHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 1180, 1005));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ConfirmActionPerformed
        // TODO add your handling code here:

        String giaNhap = txt_price.getText().replace(".", "");
        String nyc = StringHelper.safeTrim(txt_NgYeuCau.getText());
//        String hoadon = StringHelper.safeTrim(txt_SoHoaDon.getText());
        String soLuong = StringHelper.safeTrim(txt_Quantity.getText());

        if (StringHelper.isNullOrBlank(giaNhap)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá nhập.");
            return;
        }
        if (StringHelper.isNullOrBlank(nyc)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập người yêu cầu nhập hàng.");
            return;
        }
        if (StringHelper.isNullOrBlank(soLuong)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập só lượng.");
            return;
        }
        if (Integer.parseInt(soLuong) > 100) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập só lượng không lớn hơn 100, để dễ kiểm tra.");
            return;
        }
//        if (StringHelper.isNullOrBlank(hoadon)) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập só hoá đơn.");
//            return;
//        }
        long gia;
        try {
            gia = Long.parseLong(giaNhap);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá xuất không hợp lệ. Vui lòng nhập số nguyên.");
            return;
        }

        int quantity = Integer.parseInt(soLuong);
        create_TB_CTPN(quantity);
        action = "confirm";
    }//GEN-LAST:event_btn_ConfirmActionPerformed

    private void btn_GhiPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GhiPhieuActionPerformed

        // TODO add your handling code here:
        if (!"confirm".equals(action)) {
            JOptionPane.showMessageDialog(this, "Chưa xác nhận để nhập!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tb_CTPN.isEditing()) {
            tb_CTPN.getCellEditor().stopCellEditing();
        }

        int quantity = Integer.parseInt(txt_Quantity.getText());

        if (isTableSerialFilled(tb_CTPN, quantity)) {
            System.out.println("Đã nhập đủ Serial.");
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập đủ Serial!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String grName = cb_GrProduct.getSelectedItem().toString();
        String name = cb_Product.getSelectedItem().toString();
        String supplier = cb_Supplier.getSelectedItem().toString();
        int supplier_ID = ncc_Data.name_to_ID(supplier);
        String ngayNhap = txt_ngayNhap.getText().trim();

        long price;
        try {
            // Lấy text, xóa dấu chấm và chuyển thành số
            String priceText = txt_price.getText().replace(".", "");
            price = Long.parseLong(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá nhập không hợp lệ");
            return;
        }
        int currentUserId = Session.getInstance().getUserId();
        int categoryID = loaiSP_Data.name_to_ID(name);;
        String diaChiKho = cb_DiaChiKho.getSelectedItem().toString();
        String soHD = txt_SoHoaDon.getText().trim();
        String ngYeuCau = txt_NgYeuCau.getText().trim();
        String ghiChu = txt_GhiChu.getText();

        Connection conn = CONNECTION.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql_insertPN = "INSERT INTO PhieuNhap values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql_insertPN, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, currentUserId);
            ps.setInt(2, categoryID);
            ps.setInt(3, supplier_ID);
            ps.setInt(4, quantity);
            ps.setLong(5, price);
            ps.setString(6, ngayNhap);
            ps.setString(7, diaChiKho);
            ps.setString(8, soHD);
            ps.setString(9, ngYeuCau);
            ps.setString(10, ghiChu);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            int idpn = 0;
            while (rs.next()) {
                idpn = rs.getInt(1);
            }
            rs.close();
            ps.close();

            String sql = "EXEC dbo.SP_INSERT_CTPN @CT=?";
            SQLServerDataTable dt = new SQLServerDataTable();
            dt.addColumnMetadata("idpn", java.sql.Types.INTEGER);
            dt.addColumnMetadata("serial", java.sql.Types.NVARCHAR);

            DefaultTableModel model1 = (DefaultTableModel) tb_CTPN.getModel();
            for (int row = 0; row < model1.getRowCount(); row++) {
                for (int col = 0; col < model1.getColumnCount(); col++) {
                    Object value = model1.getValueAt(row, col);
                    if (value != null) {
                        String serial = value.toString().trim();
                        if (!serial.isEmpty()) {
                            dt.addRow(idpn, serial);
                        }
                    }
                }
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ((SQLServerPreparedStatement) pstmt).setStructured(1, "dbo.TYPE_CTPN", dt);
            pstmt.execute();
            pstmt.close();
            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            completeSave();
            JOptionPane.showMessageDialog(null, "Ghi phiếu nhập thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi GHI phiếu nhập!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_GhiPhieuActionPerformed

    private void cb_GrProductActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String group = cb_GrProduct.getSelectedItem() != null ? cb_GrProduct.getSelectedItem().toString().trim() : "";
        String brand = cb_Brand.getSelectedItem() != null ? cb_Brand.getSelectedItem().toString().trim() : "";
        loadCB_ListSP(group, brand);
    }

    private void cb_BrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_BrandActionPerformed
        // TODO add your handling code here:
        String group = cb_GrProduct.getSelectedItem() != null ? cb_GrProduct.getSelectedItem().toString().trim() : "";
        String brand = cb_Brand.getSelectedItem() != null ? cb_Brand.getSelectedItem().toString().trim() : "";
        loadCB_ListSP(group, brand);
    }//GEN-LAST:event_cb_BrandActionPerformed

    private void txt_QuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_QuantityKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        // Nếu không phải số (0-9) và không phải phím xóa (backspace), thì chặn
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume(); // chặn ký tự nhập
        }
    }//GEN-LAST:event_txt_QuantityKeyTyped

    private void txt_NgYeuCauKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NgYeuCauKeyTyped
        char c = evt.getKeyChar();
        // Nếu không phải số và không phải phím xóa (backspace), thì hủy ký tự nhập
        if (!Character.isLetter(c) && c != '\b' && c != ' ') {
            evt.consume(); // chặn không cho nhập            
        }
    }//GEN-LAST:event_txt_NgYeuCauKeyTyped

    private void txt_priceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_priceKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        // Nếu không phải số (0-9) và không phải phím xóa (backspace), thì chặn
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume(); // chặn ký tự nhập
        }
    }//GEN-LAST:event_txt_priceKeyTyped

//     */ @param args the command line arguments
//     */
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
            java.util.logging.Logger.getLogger(ChiTietNhapHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChiTietNhapHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChiTietNhapHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChiTietNhapHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChiTietNhapHang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Confirm;
    private javax.swing.JButton btn_GhiPhieu;
    private javax.swing.JComboBox<String> cb_Brand;
    private javax.swing.JComboBox<String> cb_DiaChiKho;
    private javax.swing.JComboBox<String> cb_GrProduct;
    private javax.swing.JComboBox<String> cb_Product;
    private javax.swing.JComboBox<String> cb_Supplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelNhapHang;
    private javax.swing.JTable tb_CTPN;
    private javax.swing.JTextArea txt_GhiChu;
    private javax.swing.JTextField txt_NgYeuCau;
    private javax.swing.JTextField txt_Quantity;
    private javax.swing.JTextField txt_SoHoaDon;
    private javax.swing.JTextField txt_ngayNhap;
    private javax.swing.JTextField txt_price;
    // End of variables declaration//GEN-END:variables
}
