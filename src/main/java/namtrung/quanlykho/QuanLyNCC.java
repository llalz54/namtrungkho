package namtrung.quanlykho;

import ConDB.DBAccess;
import DAO.NCC_DATA;
import DAO.OTHER_DATA;
import DAO.Session;
import DTO.NCC;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class QuanLyNCC extends javax.swing.JPanel {

    public QuanLyNCC() {
        initComponents();
        check_Role();
        loadDataTable_DSNCC();
        OTHER_DATA.customTable(tb_Supplier);
        customControls();
    }

    private NCC_DATA ncc_data = new NCC_DATA();

    private String action_QLNCC = "";
    private int current_supplierID;

    private void clearTXT() {
        txt_Name.setText("");
        txt_tenGoiNho.setText("");
        txt_MST.setText("");
        txt_diaChi.setText("");
    }

    private void enableTXT() {
        txt_Name.setEnabled(true);
        txt_tenGoiNho.setEnabled(true);
        txt_MST.setEnabled(true);
        txt_diaChi.setEnabled(true);
        cb_Status.setEnabled(true);
    }
    
    private void check_Role(){
        String role = Session.getInstance().getRole();
        if(!"admin".equalsIgnoreCase(role)){
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
                trangThai = "Đối tác";
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
            case "Đối tác":
                status = "1";
                break;
            default:
                status = "-1";
                break;
        }
        return status;
    }

    private void loadDataTable_DSNCC() {
        DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
        dtm.setNumRows(0);
        ArrayList<NCC> dsncc = ncc_data.getListNCC();
        for (NCC ncc : dsncc) {
            Vector vec = new Vector();
            vec.add(ncc.getName());
            vec.add(ncc.getFullName());
            vec.add(ncc.getMST());
            vec.add(ncc.getDiaChi());
            String status = convertStatus(ncc.getStatus());
            vec.add(status);
            dtm.addRow(vec);
        }
        tb_Supplier.setModel(dtm);
    }
    
    private void loadDataTable_NCC_Search(String name) {
        DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
        dtm.setNumRows(0);
        ArrayList<NCC> dsncc = ncc_data.getNCC_Name(name);
        for (NCC ncc : dsncc) {
            Vector vec = new Vector();
            vec.add(ncc.getName());
            vec.add(ncc.getFullName());
            vec.add(ncc.getMST());
            vec.add(ncc.getDiaChi());
            String status = convertStatus(ncc.getStatus());
            vec.add(status);
            dtm.addRow(vec);
        }
        tb_Supplier.setModel(dtm);
    }

    private void loadDataTable_NCC_Status() {
        String selected = cbLocSP.getSelectedItem().toString();
        ArrayList<NCC> dsncc = null;

        switch (selected) {
            case "Tất cả":
                dsncc = ncc_data.getListNCC();
                break;
            case "Đối tác":
                dsncc = ncc_data.getListNCC_Status("1");
                break;
            case "Bị xoá":
                dsncc = ncc_data.getListNCC_Status("0");
                break;
        }

        DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
        dtm.setRowCount(0); // Xóa dữ liệu cũ

        if (dsncc != null) {
            for (NCC ncc : dsncc) {
                Vector<Object> vec = new Vector<>();
                // vec.add(sp.getProductID());
                vec.add(ncc.getName());
                vec.add(ncc.getFullName());
                vec.add(ncc.getMST());
                vec.add(ncc.getDiaChi());
                vec.add(convertStatus(ncc.getStatus()));

                dtm.addRow(vec);
            }
        }
        tb_Supplier.setModel(dtm);
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
        txt_diaChi = new javax.swing.JTextArea();
        txt_tenGoiNho = new javax.swing.JTextField();
        txt_MST = new javax.swing.JTextField();
        pn_button = new javax.swing.JPanel();
        btn_Create = new javax.swing.JButton();
        btn_Delete = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        btn_Update = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_Supplier = new javax.swing.JTable();
        pn_TimKiem = new javax.swing.JPanel();
        cbLocSP = new javax.swing.JComboBox<>();
        tfTimKiem = new javax.swing.JTextField();
        btn_Search = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pn_funtion.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Quản lý nhà cung cấp");

        cb_Status.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cb_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đối tác", "Bị xoá" }));
        cb_Status.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trạng thái", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        txt_Name.setColumns(20);
        txt_Name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_Name.setLineWrap(true);
        txt_Name.setRows(3);
        txt_Name.setWrapStyleWord(true);
        txt_Name.setBorder(javax.swing.BorderFactory.createTitledBorder("Tên"));

        txt_diaChi.setColumns(20);
        txt_diaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_diaChi.setLineWrap(true);
        txt_diaChi.setRows(5);
        txt_diaChi.setWrapStyleWord(true);
        txt_diaChi.setBorder(javax.swing.BorderFactory.createTitledBorder("Địa chỉ"));

        txt_tenGoiNho.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_tenGoiNho.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên gợi nhớ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        txt_MST.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_MST.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mã số thuế", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        txt_MST.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_MSTKeyTyped(evt);
            }
        });

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
                .addContainerGap(19, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_funtionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(75, 75, 75))
            .addGroup(pn_funtionLayout.createSequentialGroup()
                .addGroup(pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pn_funtionLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pn_funtionLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(pn_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_MST, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_tenGoiNho, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 23, Short.MAX_VALUE))
        );
        pn_funtionLayout.setVerticalGroup(
            pn_funtionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_funtionLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_tenGoiNho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_MST, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cb_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pn_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        add(pn_funtion, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 0, 340, 580));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tb_Supplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_Supplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên", "Tên gợi nhớ", "Mã số thuế", "Địa chỉ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tb_Supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_SupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_Supplier);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 820, 550));

        pn_TimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pn_TimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N
        pn_TimKiem.setPreferredSize(new java.awt.Dimension(822, 75));

        cbLocSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbLocSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đối tác", "Bị xoá" }));
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
                .addGap(18, 18, 18)
                .addComponent(tfTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                .addGap(51, 51, 51)
                .addComponent(btn_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        pn_TimKiemLayout.setVerticalGroup(
            pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_TimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_TimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cbLocSP, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(tfTimKiem)
                    .addComponent(btn_Search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        add(pn_TimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 90));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("DANH SÁCH NHÀ CUNG CẤP");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 150, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btn_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CreateActionPerformed
        // TODO add your handling code here:
        action_QLNCC = "create";
        txt_Name.setText("");
        txt_Name.setEditable(true);
        txt_tenGoiNho.setText("");
        txt_tenGoiNho.setEditable(true);
        txt_diaChi.setText("");
        txt_diaChi.setEditable(true);
        txt_MST.setText("");
        txt_MST.setEditable(true);
        cb_Status.setSelectedItem("Đối tác");
        cb_Status.setEnabled(false);
    }//GEN-LAST:event_btn_CreateActionPerformed

    private void btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteActionPerformed
        // TODO add your handling code here:
        action_QLNCC = "delete";
        int i = tb_Supplier.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn nhà cung cấp để xoá", "Input warning", JOptionPane.WARNING_MESSAGE);
        } else {
            txt_Name.setEnabled(false);
            txt_tenGoiNho.setEnabled(false);
            txt_MST.setEnabled(false);
            txt_diaChi.setEnabled(false);
            cb_Status.setEnabled(false);
            DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
            String supplierName = dtm.getValueAt(i, 0).toString();
            current_supplierID = ncc_data.name_to_ID(supplierName);
        }
    }//GEN-LAST:event_btn_DeleteActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        // TODO add your handling code here:
        try {
            String name = txt_Name.getText().trim();
            String suggestName = txt_tenGoiNho.getText().trim();
            String MST = txt_MST.getText().trim();
            String address = txt_diaChi.getText().trim();
            String trangThai = cb_Status.getSelectedItem().toString();
            String status = convertTrangThai(trangThai);
            if (name.isEmpty() || suggestName.isEmpty() || MST.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy điền đầy đủ thông tin!", "Input warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (action_QLNCC.equals("create")) {
                if (ncc_data.checkName_NCC(name, suggestName) == true) {
                    JOptionPane.showMessageDialog(this, "Tên hoặc tên gới nhớ nhà cung cấp bị trùng!", "Input warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ncc_data.create_Supplier(name, suggestName, MST, address, status);
                loadDataTable_DSNCC();
                clearTXT();

            } else if (action_QLNCC.equals("update")) {
                boolean check = true;
                if (check == true) {
                    ncc_data.update_Supplier(current_supplierID, name, suggestName, MST, address, status);
                    loadDataTable_DSNCC();
                    clearTXT();
                    tb_Supplier.clearSelection();
                }

            } else if (action_QLNCC.equals("delete")) {
                ncc_data.delete_Supplier(current_supplierID);
                loadDataTable_DSNCC();
                clearTXT();
                enableTXT();
                tb_Supplier.clearSelection();

            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn hành động để ghi !!!", "Input warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ghi nhà cung cấp", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void txt_MSTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_MSTKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        // Nếu ký tự nhập không phải là số và không phải phím backspace
        if (!Character.isDigit(c) && c != '\b') {
            evt.consume(); // Hủy bỏ ký tự đó, không cho nhập
        }
    }//GEN-LAST:event_txt_MSTKeyTyped

    private void tb_SupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_SupplierMouseClicked
        // TODO add your handling code here:
        int i = tb_Supplier.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
        txt_Name.setText(dtm.getValueAt(i, 0).toString());
        txt_tenGoiNho.setText(dtm.getValueAt(i, 1).toString());
        txt_MST.setText(dtm.getValueAt(i, 2).toString());
        txt_diaChi.setText(dtm.getValueAt(i, 3).toString());
        cb_Status.setSelectedItem(dtm.getValueAt(i, 4).toString());
    }//GEN-LAST:event_tb_SupplierMouseClicked

    private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateActionPerformed
        // TODO add your handling code here:
        action_QLNCC = "update";
        int i = tb_Supplier.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Chọn nhà cung cấp để sửa", "Input warning", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel dtm = (DefaultTableModel) tb_Supplier.getModel();
            String supplierName = dtm.getValueAt(i, 0).toString();
            current_supplierID = ncc_data.name_to_ID(supplierName);
        }
    }//GEN-LAST:event_btn_UpdateActionPerformed

    private void cbLocSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLocSPActionPerformed
        // TODO add your handling code here:
        loadDataTable_NCC_Status();
    }//GEN-LAST:event_cbLocSPActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        // TODO add your handling code here:
        loadDataTable_NCC_Search(tfTimKiem.getText().trim());
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
    private javax.swing.JTable tb_Supplier;
    private javax.swing.JTextField tfTimKiem;
    private javax.swing.JTextField txt_MST;
    private javax.swing.JTextArea txt_Name;
    private javax.swing.JTextArea txt_diaChi;
    private javax.swing.JTextField txt_tenGoiNho;
    // End of variables declaration//GEN-END:variables
}
