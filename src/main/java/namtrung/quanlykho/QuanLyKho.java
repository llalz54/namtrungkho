package namtrung.quanlykho;

import ConDB.CONNECTION;
import ConDB.DBAccess;
import DAO.LOAISP_DATA;
import DAO.NHOMSP_DATA;
import DAO.OTHER_DATA;
import DAO.Session;
import DTO.LOAISP;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class QuanLyKho extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyKho
     */
    public QuanLyKho() {
        initComponents();
        check_Role();
        OTHER_DATA.loadCBDM(cb_GrProduct);
        OTHER_DATA.load_Cb_Brand(cb_Brand);
        OTHER_DATA.customTable(tb_DSSP);
        OTHER_DATA.customTable(tb_DSSP_Serial);
        //loadCB_Status();
        customControls();
    }

    private final LOAISP_DATA loaisp_data = new LOAISP_DATA();
    private final NHOMSP_DATA nhomsp_data = new NHOMSP_DATA();

    private String action_QLLSP = "";
    private String current_Name;
    private int current_cateID;

    private void check_Role() {
        btn_Update.setEnabled(false);
        btn_delete.setEnabled(false);
        btn_Save.setEnabled(false);
        String role = Session.getInstance().getRole();
        if (!"admin".equalsIgnoreCase(role)) {
            pn_QLSP.setVisible(false);
        }
    }

    private void customControls() {
        tfTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btn_Search.doClick();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btn_Search.doClick();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btn_Search.doClick();
            }
        });
        btn_Search.setVisible(false);
    }

    private void completeSave() {
        txt_Name.setText("");
        txt_Name.setEnabled(true);
        cb_Status.setEnabled(true);

        tb_DSSP.clearSelection();
        btn_Update.setEnabled(false);
        btn_delete.setEnabled(false);
        btn_Save.setEnabled(false);
    }

    private String convertStatus(String status) {
        String trangThai;
        switch (status) {
            case "0":
                trangThai = "Ngừng kinh doanh";
                break;
            case "1":
                trangThai = "Đang bán";
                break;
            default:
                trangThai = "Không xác định";
                break;
        }
        return trangThai;
    }

    private String convertTrangThai(String trangThai) {
        String status;
        switch (trangThai) {
            case "Ngừng kinh doanh":
                status = "0";
                break;
            case "Đang bán":
                status = "1";
                break;
            default:
                status = "-1";
                break;
        }
        return status;
    }

    private void loadCB_Status() {
        cb_Status.removeAllItems();
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT DISTINCT status FROM LoaiSP");
            while (rs.next()) {
                String status = rs.getString("status").trim();
                String trangThai = convertStatus(status);
                cb_Status.addItem(trangThai);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi loadCB_Status Sản phẩm!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataTable_DSLSP(String grName, String brand) {
        loaisp_data.docListLoaiSP();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSSP.getModel();
        dtm.setNumRows(0);
        ArrayList<LOAISP> dssp = loaisp_data.getDS_LoaiSP(grName, brand);
        int n = 1;
        for (LOAISP sp : dssp) {
            Vector vec = new Vector();
            vec.add(n);
            vec.add(sp.getName());
            vec.add(sp.getBrand());
            String status = convertStatus(sp.getStatus());
            vec.add(status);
            vec.add(sp.getSoLuong());
            dtm.addRow(vec);
            n++;
        }
        tb_DSSP.setModel(dtm);
    }

    private void loadDataTable_DSTonKho(String status, int categoryID) {
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        DefaultTableModel dtm = (DefaultTableModel) tb_DSSP_Serial.getModel();
        dtm.setNumRows(0);
        String query
                = "SELECT \n"
                + "    fp.ngayNhap,\n"
                + "    sp.serial,\n"
                + "    fp.name,\n"
                + "    fp.price,\n"
                + "    sp.start_date,\n"
                + "    sp.end_date\n"
                + "FROM SanPham sp\n"
                + "CROSS APPLY (\n"
                + "    SELECT TOP 1\n"
                + "        pn.ngayNhap,\n"
                + "        pn.price,\n"
                + "        s.name\n"
                + "    FROM CTPN ct\n"
                + "    INNER JOIN PhieuNhap pn ON ct.idpn = pn.idpn\n"
                + "    INNER JOIN NCC s ON pn.supplier_id = s.supplier_id\n"
                + "    WHERE ct.serial = sp.serial\n"
                + "    ORDER BY pn.ngayNhap ASC\n"
                + ") AS fp\n"
                + "WHERE sp.status = ?\n"
                + "  AND sp.category_id = ?\n"
                + "ORDER BY fp.ngayNhap ASC;";

        try (Connection conn = CONNECTION.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, categoryID);
            ResultSet rs = stmt.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                Object[] row = new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    vnFormat.format(rs.getLong(4)),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                };
                dtm.addRow(row);
            }
            if (!hasData) {
                JOptionPane.showMessageDialog(null, "Không có Serial nào phù hợp!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách sản phẩm - Serial!");
            e.printStackTrace();
        }
        tb_DSSP_Serial.setModel(dtm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_DSSP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_DSSP = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        pn_QLSP = new javax.swing.JPanel();
        cb_Status = new javax.swing.JComboBox<>();
        pn_ChucNangQLSP = new javax.swing.JPanel();
        btn_Create = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        btn_Update = new javax.swing.JButton();
        txt_Name = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_DSSP_Serial = new javax.swing.JTable();
        pn_TimKiem = new javax.swing.JPanel();
        cbLocSP = new javax.swing.JComboBox<>();
        tfTimKiem = new javax.swing.JTextField();
        btn_LayDS = new javax.swing.JButton();
        btn_Search = new javax.swing.JButton();
        lb_QLSP = new javax.swing.JLabel();
        cb_GrProduct = new javax.swing.JComboBox<>();
        cb_Brand = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pn_DSSP.setBackground(new java.awt.Color(255, 255, 255));

        tb_DSSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_DSSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Tên sản phẩm", "Thương hiệu", "Trạng thái", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_DSSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_DSSPMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_DSSP);
        if (tb_DSSP.getColumnModel().getColumnCount() > 0) {
            tb_DSSP.getColumnModel().getColumn(0).setPreferredWidth(10);
            tb_DSSP.getColumnModel().getColumn(1).setPreferredWidth(300);
            tb_DSSP.getColumnModel().getColumn(2).setPreferredWidth(140);
            tb_DSSP.getColumnModel().getColumn(3).setPreferredWidth(80);
            tb_DSSP.getColumnModel().getColumn(4).setPreferredWidth(70);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("DANH SÁCH DÒNG SẢN PHẨM");

        javax.swing.GroupLayout pn_DSSPLayout = new javax.swing.GroupLayout(pn_DSSP);
        pn_DSSP.setLayout(pn_DSSPLayout);
        pn_DSSPLayout.setHorizontalGroup(
            pn_DSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_DSSPLayout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(jLabel1)
                .addContainerGap(324, Short.MAX_VALUE))
            .addGroup(pn_DSSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pn_DSSPLayout.setVerticalGroup(
            pn_DSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_DSSPLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(pn_DSSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, -1));

        pn_QLSP.setBackground(new java.awt.Color(255, 255, 255));

        cb_Status.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang bán", "Bị xoá" }));
        cb_Status.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trạng thái", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        pn_ChucNangQLSP.setBackground(new java.awt.Color(255, 255, 255));
        pn_ChucNangQLSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        btn_Create.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plus.png"))); // NOI18N
        btn_Create.setText("Thêm");
        btn_Create.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CreateActionPerformed(evt);
            }
        });

        btn_delete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        btn_delete.setText("Xoá");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_Save.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        btn_Save.setText("Ghi");
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        btn_Update.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/updated.png"))); // NOI18N
        btn_Update.setText("Sửa");
        btn_Update.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_ChucNangQLSPLayout = new javax.swing.GroupLayout(pn_ChucNangQLSP);
        pn_ChucNangQLSP.setLayout(pn_ChucNangQLSPLayout);
        pn_ChucNangQLSPLayout.setHorizontalGroup(
            pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_ChucNangQLSPLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Create, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );
        pn_ChucNangQLSPLayout.setVerticalGroup(
            pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_ChucNangQLSPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Create)
                    .addComponent(btn_delete))
                .addGap(18, 18, 18)
                .addGroup(pn_ChucNangQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Save))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        txt_Name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_Name.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên"));

        javax.swing.GroupLayout pn_QLSPLayout = new javax.swing.GroupLayout(pn_QLSP);
        pn_QLSP.setLayout(pn_QLSPLayout);
        pn_QLSPLayout.setHorizontalGroup(
            pn_QLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_QLSPLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(pn_QLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pn_ChucNangQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        pn_QLSPLayout.setVerticalGroup(
            pn_QLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_QLSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pn_ChucNangQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        add(pn_QLSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(848, 200, 350, 310));

        tb_DSSP_Serial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Ngày nhập", "Serial", "Nhà cung cấp", "Giá nhập", "Ngày kích hoạt", "Ngày kết thúc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tb_DSSP_Serial);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 1160, 280));

        pn_TimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pn_TimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        pn_TimKiem.setPreferredSize(new java.awt.Dimension(822, 75));

        cbLocSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbLocSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tồn kho", "Đã bán" }));
        cbLocSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLocSPActionPerformed(evt);
            }
        });

        tfTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTimKiemActionPerformed(evt);
            }
        });

        btn_LayDS.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_LayDS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/refresh.png"))); // NOI18N
        btn_LayDS.setText("Lấy danh sách");
        btn_LayDS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LayDSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_TimKiemLayout = new javax.swing.GroupLayout(pn_TimKiem);
        pn_TimKiem.setLayout(pn_TimKiemLayout);
        pn_TimKiemLayout.setHorizontalGroup(
            pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_TimKiemLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(cbLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(btn_LayDS)
                .addGap(18, 18, 18)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pn_TimKiemLayout.setVerticalGroup(
            pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_TimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Search, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfTimKiem, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_TimKiemLayout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addGroup(pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_LayDS, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14))
        );

        add(pn_TimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 420, 830, 90));

        lb_QLSP.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_QLSP.setText("QUẢN LÍ SẢN PHẨM");
        add(lb_QLSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, -1, -1));

        cb_GrProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_GrProduct.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Máy in" }));
        cb_GrProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhóm sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        cb_GrProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_GrProductActionPerformed(evt);
            }
        });
        add(cb_GrProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 50, 300, -1));

        cb_Brand.setEditable(true);
        cb_Brand.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Brand.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hãng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        cb_Brand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_BrandActionPerformed(evt);
            }
        });
        add(cb_Brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 300, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void tb_DSSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_DSSPMouseClicked
        // TODO add your handling code here:
        int i = tb_DSSP.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_DSSP.getModel();

        current_Name = dtm.getValueAt(i, 1).toString();
        txt_Name.setText(current_Name);
        cb_Status.setSelectedItem(dtm.getValueAt(i, 3).toString());

        current_cateID = loaisp_data.name_to_ID(current_Name);

        btn_Update.setEnabled(true);
        btn_delete.setEnabled(true);
    }//GEN-LAST:event_tb_DSSPMouseClicked

    private void cb_GrProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_GrProductActionPerformed
        // TODO add your handling code here:
        String groupName = cb_GrProduct.getSelectedItem() != null ? cb_GrProduct.getSelectedItem().toString().trim() : "";
        String brand = cb_Brand.getSelectedItem() != null ? cb_Brand.getSelectedItem().toString().trim() : "";
        loadDataTable_DSLSP(groupName, brand);
    }//GEN-LAST:event_cb_GrProductActionPerformed

    private void cb_BrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_BrandActionPerformed
        // TODO add your handling code here:
        String groupName = cb_GrProduct.getSelectedItem() != null ? cb_GrProduct.getSelectedItem().toString().trim() : "";
        String brand = cb_Brand.getSelectedItem() != null ? cb_Brand.getSelectedItem().toString().trim() : "";
        loadDataTable_DSLSP(groupName, brand);
    }//GEN-LAST:event_cb_BrandActionPerformed

    private void cbLocSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLocSPActionPerformed

    }//GEN-LAST:event_cbLocSPActionPerformed

    private void tfTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTimKiemActionPerformed

    }//GEN-LAST:event_tfTimKiemActionPerformed

    private void btn_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CreateActionPerformed
        // TODO add your handling code here:
        action_QLLSP = "create";
        txt_Name.setText("");
        txt_Name.setEditable(true);
        cb_Status.setEnabled(false);
        cb_Status.setSelectedItem("Đang bán");
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_btn_CreateActionPerformed

    private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateActionPerformed
        // TODO add your handling code here:        
        int i = tb_DSSP.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để sửa", "Input warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        action_QLLSP = "update";
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_btn_UpdateActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
        int i = tb_DSSP.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xoá", "Input warning", JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            action_QLLSP = "delete";
            txt_Name.setEnabled(false);
            cb_Status.setEnabled(false);
            btn_Save.setEnabled(true);
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        // TODO add your handling code here:
        try {
            if (action_QLLSP == null || action_QLLSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa chọn hành động để ghi !!!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String grName = cb_GrProduct.getSelectedItem().toString();
            int grID = nhomsp_data.name_to_ID(grName);
            String brand = cb_Brand.getSelectedItem().toString();
            String name = txt_Name.getText().trim();
            String trangThai = cb_Status.getSelectedItem().toString();
            String status = convertTrangThai(trangThai);
            if (grID <= 0 || brand.isEmpty() || name.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy điền đầy đủ thông tin!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            switch (action_QLLSP) {
                case "create" -> {
                    loaisp_data.create_LSP(grID, name, "1", brand);
                    completeSave();
                    loadDataTable_DSLSP(grName, brand);
                }
                case "update" -> {
                    loaisp_data.update_GrProduct(current_cateID, grID, name, status, brand);
                    completeSave();
                    loadDataTable_DSLSP(grName, brand);
                }
                case "delete" -> {
                    loaisp_data.delete_GrProduct(current_cateID);
                    completeSave();
                    loadDataTable_DSLSP(grName, brand);
                }
                default ->
                    JOptionPane.showMessageDialog(this, "Hành động không hợp lệ !!!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi sản phẩm", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void btn_LayDSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LayDSActionPerformed
        // TODO add your handling code here:
        int i = tb_DSSP.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để lấy danh sách", "Input warning", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel dtm = (DefaultTableModel) tb_DSSP.getModel();
            String cateName = dtm.getValueAt(i, 1).toString();
            current_cateID = loaisp_data.name_to_ID(cateName);

            String status = cbLocSP.getSelectedItem().toString();
            switch (status) {
                case "Tồn kho":
                    loadDataTable_DSTonKho("1", current_cateID);
                    break;
                case "Đã bán":
                    loadDataTable_DSTonKho("0", current_cateID);
                    break;
            }
        }


    }//GEN-LAST:event_btn_LayDSActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Create;
    private javax.swing.JButton btn_LayDS;
    private javax.swing.JButton btn_Save;
    private javax.swing.JButton btn_Search;
    private javax.swing.JButton btn_Update;
    private javax.swing.JButton btn_delete;
    private javax.swing.JComboBox<String> cbLocSP;
    private javax.swing.JComboBox<String> cb_Brand;
    private javax.swing.JComboBox<String> cb_GrProduct;
    private javax.swing.JComboBox<String> cb_Status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_QLSP;
    private javax.swing.JPanel pn_ChucNangQLSP;
    private javax.swing.JPanel pn_DSSP;
    private javax.swing.JPanel pn_QLSP;
    private javax.swing.JPanel pn_TimKiem;
    private javax.swing.JTable tb_DSSP;
    private javax.swing.JTable tb_DSSP_Serial;
    private javax.swing.JTextField tfTimKiem;
    private javax.swing.JTextField txt_Name;
    // End of variables declaration//GEN-END:variables
}
