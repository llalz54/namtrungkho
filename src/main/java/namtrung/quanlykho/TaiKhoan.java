package namtrung.quanlykho;

import DAO.NHANVIEN_DATA;
import DAO.Session;
import DTO.NHANVIEN;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class TaiKhoan extends javax.swing.JPanel {

    private  NHANVIEN_DATA nhanVienDAO = new NHANVIEN_DATA();
    public String currentUser = Session.getInstance().getUsername(); // Người dùng hiện tại đang đăng nhập
    public int user_id = Session.getInstance().getUserId();

    public TaiKhoan() {
        initComponents();
        // Thêm sự kiện cho các nút
        btn_XacNhan.addActionListener(e -> doiMatKhau());
        btn_Tao.addActionListener(e -> taoTaiKhoan());
        check_Role();
    }


    private void check_Role() {
        String role = Session.getInstance().getRole();
        if (!"admin".equalsIgnoreCase(role)) {
            lb_Tao.setVisible(false);
            lb_username.setVisible(false);
            lb_pass.setVisible(false);
            lb_name.setVisible(false);
            tf_name.setVisible(false);
            tf_pass.setVisible(false);
            tf_username.setVisible(false);
            btn_Tao.setVisible(false);
        }
    }

    // Hàm đổi mật khẩu
    private void doiMatKhau() {

        String oldPass = tf_passold.getText().trim();
        String newPass = tf_passnew.getText().trim();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            NHANVIEN nv = nhanVienDAO.findByUsername(currentUser);
            if (nv == null) {
                JOptionPane.showMessageDialog(this,
                        "Người dùng không tồn tại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra mật khẩu cũ
            
            if (!oldPass.equals(nv.getPassWord())) {
                System.out.println("mk " + nv.getPassWord());
                JOptionPane.showMessageDialog(this,
                        "Mật khẩu cũ không đúng",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo mật khẩu mới
           
            // Lưu cả salt và password đã hash (có thể cần điều chỉnh tùy cách lưu trữ)
            if (nhanVienDAO.updatePassword(currentUser, newPass)) {
                JOptionPane.showMessageDialog(this,
                        "Đổi mật khẩu thành công",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                tf_passold.setText("");
                tf_passnew.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Đổi mật khẩu thất bại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.out.println("tên tài khoản : "+ currentUser);
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi đổi mật khẩu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            
        }
    }

    // Hàm tạo tài khoản mới
    private void taoTaiKhoan() {
        String username = tf_username.getText().trim();
        String password = tf_pass.getText().trim();
        String name = tf_name.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Kiểm tra username đã tồn tại chưa
            if (nhanVienDAO.findByUsername(username) != null) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản đã tồn tại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo salt và hash password
      

            // Tạo nhân viên mới với role mặc định và status active
            NHANVIEN newNhanVien = new NHANVIEN(
                    user_id,
                    "employee", // Role mặc định
                    name,
                    "1",// Status mặc định
                    username,
                    password// Lưu cả salt và password          
            );

            if (nhanVienDAO.insert(newNhanVien)) {
                JOptionPane.showMessageDialog(this,
                        "Tạo tài khoản thành công",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                tf_username.setText("");
                tf_pass.setText("");
                tf_name.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tạo tài khoản thất bại",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tạo tài khoản: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTaiKhoan = new javax.swing.JPanel();
        lb_Doi = new javax.swing.JLabel();
        lb_passold = new javax.swing.JLabel();
        lb_passnew = new javax.swing.JLabel();
        btn_XacNhan = new javax.swing.JButton();
        lb_Tao = new javax.swing.JLabel();
        lb_username = new javax.swing.JLabel();
        lb_pass = new javax.swing.JLabel();
        lb_name = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        tf_name = new javax.swing.JTextField();
        btn_Tao = new javax.swing.JButton();
        tf_passold = new javax.swing.JPasswordField();
        tf_passnew = new javax.swing.JPasswordField();
        tf_pass = new javax.swing.JPasswordField();

        lb_Doi.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_Doi.setText("ĐỔI MẬT KHẨU");

        lb_passold.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_passold.setText("Mật khẩu cũ    :");

        lb_passnew.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_passnew.setText("Mật khẩu mới :");

        btn_XacNhan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_XacNhan.setText("Xác Nhận");

        lb_Tao.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_Tao.setText("TẠO TÀI KHOẢN");

        lb_username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_username.setText("Tài khoản :");

        lb_pass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_pass.setText("Mật khẩu :");

        lb_name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lb_name.setText("Họ tên     :");

        tf_username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tf_name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_Tao.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_Tao.setText("Tạo");

        tf_passold.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tf_passnew.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tf_pass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout panelTaiKhoanLayout = new javax.swing.GroupLayout(panelTaiKhoan);
        panelTaiKhoan.setLayout(panelTaiKhoanLayout);
        panelTaiKhoanLayout.setHorizontalGroup(
            panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                        .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lb_passold, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_passnew, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(32, 32, 32)
                        .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tf_passold)
                                    .addComponent(tf_passnew, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                                .addGap(191, 191, 191)
                                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lb_username)
                                    .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lb_name)
                                        .addComponent(lb_pass)))
                                .addGap(26, 26, 26)
                                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tf_username)
                                    .addComponent(tf_name, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tf_pass, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                                .addComponent(lb_Doi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 409, Short.MAX_VALUE)
                                .addComponent(lb_Tao)
                                .addGap(364, 364, 364))))
                    .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(btn_XacNhan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Tao)
                        .addGap(372, 372, 372))))
        );
        panelTaiKhoanLayout.setVerticalGroup(
            panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_Doi)
                    .addComponent(lb_Tao))
                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_passold)
                            .addComponent(lb_username)
                            .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTaiKhoanLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_passold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_passnew)
                            .addComponent(lb_pass)
                            .addComponent(tf_passnew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lb_name))
                    .addGroup(panelTaiKhoanLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(tf_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(panelTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Tao)
                            .addComponent(btn_XacNhan))))
                .addContainerGap(405, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TaiKhoan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Tao;
    private javax.swing.JButton btn_XacNhan;
    private javax.swing.JLabel lb_Doi;
    private javax.swing.JLabel lb_Tao;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_pass;
    private javax.swing.JLabel lb_passnew;
    private javax.swing.JLabel lb_passold;
    private javax.swing.JLabel lb_username;
    private javax.swing.JPanel panelTaiKhoan;
    private javax.swing.JTextField tf_name;
    private javax.swing.JPasswordField tf_pass;
    private javax.swing.JPasswordField tf_passnew;
    private javax.swing.JPasswordField tf_passold;
    private javax.swing.JTextField tf_username;
    // End of variables declaration//GEN-END:variables
}
