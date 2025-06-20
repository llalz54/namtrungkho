package namtrung.quanlykho;

import ConDB.DBAccess;
import DAO.NHOMSP_DATA;
import DAO.OTHER_DATA;
import DAO.Session;
import DTO.NHOMSP;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import utils.StringHelper;

public class NhomSP extends javax.swing.JPanel {

    public NhomSP() {
        initComponents();
        check_Role();
        OTHER_DATA.customTable(tb_GrProduct);
        loadDataTable_NhomSP();
        customControls();
    }

    private NHOMSP_DATA nhomsp_Data = new NHOMSP_DATA();
    private String action_QLNSP = "";
    private String current_grName;
    private int current_grID;

    private void completeAction() {
        txt_Name.setText("");

        unableTXT();
        tb_GrProduct.clearSelection();

        action_QLNSP = "";

        btn_Update.setEnabled(false);
        btn_Delete.setEnabled(false);
        btn_Save.setEnabled(false);
        nhomsp_Data.docListnhomSP();
        loadDataTable_NhomSP();        
    }

    private void enableTXT() {
        txt_Name.setEnabled(true);
        cb_Status.setEnabled(true);
    }

    private void unableTXT() {
        txt_Name.setEnabled(false);
        cb_Status.setEnabled(false);
    }

    private void check_Role() {
        unableTXT();
        btn_Update.setEnabled(false);
        btn_Delete.setEnabled(false);
        btn_Save.setEnabled(false);
        String role = Session.getInstance().getRole();
        if (!"admin".equalsIgnoreCase(role)) {
            pn_funtion.setVisible(false);
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

    private String convertStatus(String status) {
        String trangThai;
        switch (status) {
            case "0":
                trangThai = "Bị xoá";
                break;
            case "1":
                trangThai = "Đang dùng";
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
            case "Bị xoá":
                status = "0";
                break;
            case "Đang dùng":
                status = "1";
                break;
            default:
                status = "-1";
                break;
        }
        return status;
    }

    private void loadDataTable_NhomSP() {
        DefaultTableModel dtm = (DefaultTableModel) tb_GrProduct.getModel();
        dtm.setNumRows(0);
        ArrayList<NHOMSP> dsnsp = nhomsp_Data.getListnhomSP();
        int n = 1;
        for (NHOMSP gr : dsnsp) {
            Vector vec = new Vector();
            vec.add(n);
            vec.add(gr.getName());
            String status = convertStatus(gr.getStatus());
            vec.add(status);
            dtm.addRow(vec);
            n++;
        }
        tb_GrProduct.setModel(dtm);
    }

    private void loadDataTable_NCC_Search(String name) {
        DefaultTableModel dtm = (DefaultTableModel) tb_GrProduct.getModel();
        dtm.setNumRows(0);
        ArrayList<NHOMSP> dsnsp = nhomsp_Data.getGrProduct_Name(name);
        int n = 1;
        for (NHOMSP gr : dsnsp) {
            Vector vec = new Vector();
            vec.add(n);
            vec.add(gr.getName());
            String status = convertStatus(gr.getStatus());
            vec.add(status);
            dtm.addRow(vec);
            n++;
        }
        tb_GrProduct.setModel(dtm);
    }

    private void loadDataTable_GrProduct_Status() {
        String selected = cbLocSP.getSelectedItem().toString();
        ArrayList<NHOMSP> dsnsp = null;

        switch (selected) {
            case "Tất cả":
                dsnsp = nhomsp_Data.getListnhomSP();
                break;
            case "Đang dùng":
                dsnsp = nhomsp_Data.getListnhomSP_Status("1");
                break;
            case "Bị xoá":
                dsnsp = nhomsp_Data.getListnhomSP_Status("0");
                break;
        }

        DefaultTableModel dtm = (DefaultTableModel) tb_GrProduct.getModel();
        dtm.setRowCount(0); // Xóa dữ liệu cũ
        int n = 1;

        if (dsnsp != null) {
            for (NHOMSP gr : dsnsp) {
                Vector<Object> vec = new Vector<>();
                vec.add(n);
                vec.add(gr.getName());
                vec.add(convertStatus(gr.getStatus()));
                dtm.addRow(vec);
                n++;
            }
        }
        tb_GrProduct.setModel(dtm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_funtion = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cb_Status = new javax.swing.JComboBox<>();
        txt_Name = new javax.swing.JTextArea();
        pn_button = new javax.swing.JPanel();
        btn_Create = new javax.swing.JButton();
        btn_Delete = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        btn_Update = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_GrProduct = new javax.swing.JTable();
        pn_TimKiem = new javax.swing.JPanel();
        cbLocSP = new javax.swing.JComboBox<>();
        tfTimKiem = new javax.swing.JTextField();
        btn_Search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pn_funtion.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Quản lý nhóm sản phẩm");

        cb_Status.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang dùng", "Bị xoá" }));
        cb_Status.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trạng thái", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        txt_Name.setColumns(20);
        txt_Name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_Name.setLineWrap(true);
        txt_Name.setRows(3);
        txt_Name.setWrapStyleWord(true);
        txt_Name.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên"));

        pn_button.setBackground(new java.awt.Color(255, 255, 255));
        pn_button.setBorder(javax.swing.BorderFactory.createTitledBorder("Chức năng"));

        btn_Create.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Create.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plus.png"))); // NOI18N
        btn_Create.setText("Thêm");
        btn_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CreateActionPerformed(evt);
            }
        });

        btn_Delete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        btn_Delete.setText("Xoá");
        btn_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteActionPerformed(evt);
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
        btn_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_buttonLayout = new javax.swing.GroupLayout(pn_button);
        pn_button.setLayout(pn_buttonLayout);
        pn_buttonLayout.setHorizontalGroup(
            pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_buttonLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Create)
                    .addComponent(btn_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        pn_buttonLayout.setVerticalGroup(
            pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_buttonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Create)
                    .addComponent(btn_Update))
                .addGap(18, 18, 18)
                .addGroup(pn_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Delete)
                    .addComponent(btn_Save))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_funtionLayout = new javax.swing.GroupLayout(pn_funtion);
        pn_funtion.setLayout(pn_funtionLayout);
        pn_funtionLayout.setHorizontalGroup(
            pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_funtionLayout.createSequentialGroup()
                .addGroup(pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_funtionLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(pn_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pn_funtionLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_funtionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(67, 67, 67))
        );
        pn_funtionLayout.setVerticalGroup(
            pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_funtionLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(pn_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        add(pn_funtion, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 0, 350, 580));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tb_GrProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_GrProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "STT", "Tên", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_GrProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_GrProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_GrProduct);
        if (tb_GrProduct.getColumnModel().getColumnCount() > 0) {
            tb_GrProduct.getColumnModel().getColumn(0).setPreferredWidth(10);
            tb_GrProduct.getColumnModel().getColumn(1).setPreferredWidth(500);
            tb_GrProduct.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 820, 550));

        pn_TimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pn_TimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        pn_TimKiem.setPreferredSize(new java.awt.Dimension(822, 75));

        cbLocSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbLocSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang dùng", "Bị xoá" }));
        cbLocSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLocSPActionPerformed(evt);
            }
        });

        tfTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_Search.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_TimKiemLayout = new javax.swing.GroupLayout(pn_TimKiem);
        pn_TimKiem.setLayout(pn_TimKiemLayout);
        pn_TimKiemLayout.setHorizontalGroup(
            pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_TimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(tfTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pn_TimKiemLayout.setVerticalGroup(
            pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_TimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfTimKiem, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        add(pn_TimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 90));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("DANH SÁCH NHÓM SẢN PHẨM");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 150, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CreateActionPerformed
        // TODO add your handling code here:
        action_QLNSP = "create";
        txt_Name.setEnabled(true);
        txt_Name.setText("");
        txt_Name.setEditable(true);
        cb_Status.setSelectedItem("Đang dùng");
        cb_Status.setEnabled(false);

        tb_GrProduct.clearSelection();
        btn_Update.setEnabled(false);
        btn_Delete.setEnabled(false);

        btn_Save.setEnabled(true);
    }//GEN-LAST:event_btn_CreateActionPerformed

    private void btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteActionPerformed
        // TODO add your handling code here:
        int i = tb_GrProduct.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn nhà cung cấp để xoá", "Input warning", JOptionPane.WARNING_MESSAGE);
        } else {
            action_QLNSP = "delete";
            unableTXT();
            btn_Save.setEnabled(true);
        }
    }//GEN-LAST:event_btn_DeleteActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        // TODO add your handling code here:
        try {
            if (action_QLNSP == null || action_QLNSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa chọn hành động để ghi !!!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String name = StringHelper.safeTrim(txt_Name.getText());
            String trangThai = cb_Status.getSelectedItem().toString();
            String status = convertTrangThai(trangThai);
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy điền đầy đủ thông tin!", "Input warning", JOptionPane.WARNING_MESSAGE);
                txt_Name.requestFocusInWindow();
                return;
            }
            switch (action_QLNSP) {
                case "create" -> {
                    nhomsp_Data.create_GrProduct(name, status);
                    completeAction();
                }
                case "update" -> {
                    nhomsp_Data.update_GrProduct(current_grID, name, status);
                    completeAction();
                }
                case "delete" -> {
                    nhomsp_Data.delete_GrProduct(current_grID);
                    completeAction();
                }
                default ->
                    JOptionPane.showMessageDialog(this, "Hành động không hợp lệ!!!", "Input warning", JOptionPane.WARNING_MESSAGE);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi nhóm sản phẩm", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void tb_GrProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_GrProductMouseClicked
        // TODO add your handling code here:
        btn_Update.setEnabled(true);
        btn_Delete.setEnabled(true);
        int i = tb_GrProduct.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_GrProduct.getModel();
        txt_Name.setText(dtm.getValueAt(i, 1).toString());
        cb_Status.setSelectedItem(dtm.getValueAt(i, 2).toString());

        current_grName = dtm.getValueAt(i, 1).toString();
        current_grID = nhomsp_Data.name_to_ID(current_grName);
    }//GEN-LAST:event_tb_GrProductMouseClicked

    private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateActionPerformed
        // TODO add your handling code here:       
        int i = tb_GrProduct.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn nhóm sản phẩm để sửa", "Input warning", JOptionPane.WARNING_MESSAGE);
        }
        action_QLNSP = "update";
        enableTXT();
        btn_Save.setEnabled(true);
    }//GEN-LAST:event_btn_UpdateActionPerformed

    private void cbLocSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLocSPActionPerformed
        // TODO add your handling code here:
        loadDataTable_GrProduct_Status();
    }//GEN-LAST:event_cbLocSPActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        // TODO add your handling code here:
        String name = StringHelper.safeTrim(tfTimKiem.getText());
        loadDataTable_NCC_Search(name);
    }//GEN-LAST:event_btn_SearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Create;
    private javax.swing.JButton btn_Delete;
    private javax.swing.JButton btn_Save;
    private javax.swing.JButton btn_Search;
    private javax.swing.JButton btn_Update;
    private javax.swing.JComboBox<String> cbLocSP;
    private javax.swing.JComboBox<String> cb_Status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pn_TimKiem;
    private javax.swing.JPanel pn_button;
    private javax.swing.JPanel pn_funtion;
    private javax.swing.JTable tb_GrProduct;
    private javax.swing.JTextField tfTimKiem;
    private javax.swing.JTextArea txt_Name;
    // End of variables declaration//GEN-END:variables
}
