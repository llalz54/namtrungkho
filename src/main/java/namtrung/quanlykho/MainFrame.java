 package namtrung.quanlykho;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author Admin
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        applyDefaultUIStyle();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Lấy kích thước màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Tính toán kích thước tối đa (90% màn hình hoặc 1500x800, tùy cái nào nhỏ hơn)
        this.setMinimumSize(new Dimension(1200, 700)); // Giới hạn nhỏ nhất
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tự mở rộng ra hết màn hình
        setLocationRelativeTo(null);
        // Thiết lập icon
        try {
            java.awt.Image icon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png"));
            this.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Không thể tải icon: " + e.getMessage());
        }
        showPanel(new ChiTietNhapHang());
        this.setVisible(true);
    }

    Color defaultColor = new Color(89, 168, 105);
    Color clickColor = new Color(26, 188, 156);

    public void showPanel(JPanel panel) {
        panelShow.removeAll();              // Xóa panel cũ
        panelShow.setLayout(new BorderLayout());
        panelShow.add(panel); // Thêm panel mới
        panelShow.revalidate();             // Làm mới hiển thị
        panelShow.repaint();
    }

    public static void applyDefaultUIStyle() {
        try {
            // Giao diện hệ điều hành (Windows/macOS/Linux)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // ComboBox
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", Color.BLACK);
            UIManager.put("ComboBox.selectionBackground", Color.WHITE);
            UIManager.put("ComboBox.selectionForeground", Color.BLACK);
            UIManager.put("ComboBox.rendererUseListColors", Boolean.TRUE);

            // Button
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);

            // TextField (nếu cần)
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);

            // Nền trắng cho viewport (phần chứa JTable, JTextArea, ...)
            UIManager.put("Viewport.background", Color.WHITE);

            // Nền của chính JScrollPane
            UIManager.put("ScrollPane.background", Color.WHITE);;

            // Panel (nếu cần)
            UIManager.put("Panel.background", Color.WHITE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMenu = new javax.swing.JPanel();
        pn_OpenSP = new javax.swing.JPanel();
        lblSanPham = new javax.swing.JLabel();
        pn_OpenSupplier = new javax.swing.JPanel();
        lb_Supplier = new javax.swing.JLabel();
        pn_OpenNhap = new javax.swing.JPanel();
        lblNhapHang = new javax.swing.JLabel();
        pn_OpenPhieuNhap = new javax.swing.JPanel();
        lb_PhieuNhap = new javax.swing.JLabel();
        pn_OpenXuat = new javax.swing.JPanel();
        lblXuatHang = new javax.swing.JLabel();
        pn_OpenPhieuXuat = new javax.swing.JPanel();
        lb_PhieuXuat = new javax.swing.JLabel();
        pn_Logout = new javax.swing.JPanel();
        lb_Logout = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pn_OpenTaiKhoan = new javax.swing.JPanel();
        lb_TaiKhoan = new javax.swing.JLabel();
        pn_OpenGrSP = new javax.swing.JPanel();
        lblSanPham1 = new javax.swing.JLabel();
        panelShow = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setBackground(new java.awt.Color(89, 168, 105));
        panelMenu.setPreferredSize(new java.awt.Dimension(300, 700));

        pn_OpenSP.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenSP.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenSPMouseMoved(evt);
            }
        });
        pn_OpenSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenSPMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenSPMouseExited(evt);
            }
        });

        lblSanPham.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSanPham.setForeground(new java.awt.Color(255, 255, 255));
        lblSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/product.png"))); // NOI18N
        lblSanPham.setText("SẢN PHẨM");

        javax.swing.GroupLayout pn_OpenSPLayout = new javax.swing.GroupLayout(pn_OpenSP);
        pn_OpenSP.setLayout(pn_OpenSPLayout);
        pn_OpenSPLayout.setHorizontalGroup(
            pn_OpenSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_OpenSPLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(lblSanPham)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        pn_OpenSPLayout.setVerticalGroup(
            pn_OpenSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSanPham, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pn_OpenSupplier.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenSupplier.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenSupplierMouseMoved(evt);
            }
        });
        pn_OpenSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenSupplierMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenSupplierMouseExited(evt);
            }
        });

        lb_Supplier.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_Supplier.setForeground(new java.awt.Color(255, 255, 255));
        lb_Supplier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_Supplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/supplier.png"))); // NOI18N
        lb_Supplier.setText("NHÀ CUNG CẤP");

        javax.swing.GroupLayout pn_OpenSupplierLayout = new javax.swing.GroupLayout(pn_OpenSupplier);
        pn_OpenSupplier.setLayout(pn_OpenSupplierLayout);
        pn_OpenSupplierLayout.setHorizontalGroup(
            pn_OpenSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_OpenSupplierLayout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(lb_Supplier)
                .addGap(53, 53, 53))
        );
        pn_OpenSupplierLayout.setVerticalGroup(
            pn_OpenSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_Supplier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        pn_OpenNhap.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenNhap.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenNhapMouseMoved(evt);
            }
        });
        pn_OpenNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenNhapMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenNhapMouseExited(evt);
            }
        });

        lblNhapHang.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNhapHang.setForeground(new java.awt.Color(255, 255, 255));
        lblNhapHang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNhapHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/import.png"))); // NOI18N
        lblNhapHang.setText("NHẬP HÀNG");

        javax.swing.GroupLayout pn_OpenNhapLayout = new javax.swing.GroupLayout(pn_OpenNhap);
        pn_OpenNhap.setLayout(pn_OpenNhapLayout);
        pn_OpenNhapLayout.setHorizontalGroup(
            pn_OpenNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_OpenNhapLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(lblNhapHang, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        pn_OpenNhapLayout.setVerticalGroup(
            pn_OpenNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_OpenNhapLayout.createSequentialGroup()
                .addComponent(lblNhapHang, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pn_OpenPhieuNhap.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenPhieuNhap.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuNhapMouseMoved(evt);
            }
        });
        pn_OpenPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuNhapMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuNhapMouseExited(evt);
            }
        });

        lb_PhieuNhap.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_PhieuNhap.setForeground(new java.awt.Color(255, 255, 255));
        lb_PhieuNhap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_PhieuNhap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/requirement.png"))); // NOI18N
        lb_PhieuNhap.setText("PHIẾU NHẬP");

        javax.swing.GroupLayout pn_OpenPhieuNhapLayout = new javax.swing.GroupLayout(pn_OpenPhieuNhap);
        pn_OpenPhieuNhap.setLayout(pn_OpenPhieuNhapLayout);
        pn_OpenPhieuNhapLayout.setHorizontalGroup(
            pn_OpenPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_OpenPhieuNhapLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lb_PhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        pn_OpenPhieuNhapLayout.setVerticalGroup(
            pn_OpenPhieuNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_PhieuNhap, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        pn_OpenXuat.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenXuat.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenXuatMouseMoved(evt);
            }
        });
        pn_OpenXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenXuatMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenXuatMouseExited(evt);
            }
        });

        lblXuatHang.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblXuatHang.setForeground(new java.awt.Color(255, 255, 255));
        lblXuatHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblXuatHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/export.png"))); // NOI18N
        lblXuatHang.setText("XUẤT HÀNG");

        javax.swing.GroupLayout pn_OpenXuatLayout = new javax.swing.GroupLayout(pn_OpenXuat);
        pn_OpenXuat.setLayout(pn_OpenXuatLayout);
        pn_OpenXuatLayout.setHorizontalGroup(
            pn_OpenXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_OpenXuatLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(lblXuatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        pn_OpenXuatLayout.setVerticalGroup(
            pn_OpenXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_OpenXuatLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblXuatHang))
        );

        pn_OpenPhieuXuat.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenPhieuXuat.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuXuatMouseMoved(evt);
            }
        });
        pn_OpenPhieuXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuXuatMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenPhieuXuatMouseExited(evt);
            }
        });

        lb_PhieuXuat.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_PhieuXuat.setForeground(new java.awt.Color(255, 255, 255));
        lb_PhieuXuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_PhieuXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/checklist.png"))); // NOI18N
        lb_PhieuXuat.setText("PHIẾU XUẤT");

        javax.swing.GroupLayout pn_OpenPhieuXuatLayout = new javax.swing.GroupLayout(pn_OpenPhieuXuat);
        pn_OpenPhieuXuat.setLayout(pn_OpenPhieuXuatLayout);
        pn_OpenPhieuXuatLayout.setHorizontalGroup(
            pn_OpenPhieuXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_OpenPhieuXuatLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(lb_PhieuXuat)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        pn_OpenPhieuXuatLayout.setVerticalGroup(
            pn_OpenPhieuXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_PhieuXuat, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        pn_Logout.setBackground(new java.awt.Color(89, 168, 105));
        pn_Logout.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_LogoutMouseMoved(evt);
            }
        });
        pn_Logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_LogoutMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_LogoutMouseExited(evt);
            }
        });

        lb_Logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_Logout.setForeground(new java.awt.Color(255, 255, 255));
        lb_Logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_Logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logout.png"))); // NOI18N
        lb_Logout.setText("ĐĂNG XUẤT");

        javax.swing.GroupLayout pn_LogoutLayout = new javax.swing.GroupLayout(pn_Logout);
        pn_Logout.setLayout(pn_LogoutLayout);
        pn_LogoutLayout.setHorizontalGroup(
            pn_LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_LogoutLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(lb_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_LogoutLayout.setVerticalGroup(
            pn_LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_LogoutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 114, 157));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CÔNG TY CP ĐẦU TƯ KT TM");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("NAM TRUNG");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo_NT.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        pn_OpenTaiKhoan.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenTaiKhoan.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenTaiKhoanMouseMoved(evt);
            }
        });
        pn_OpenTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenTaiKhoanMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenTaiKhoanMouseExited(evt);
            }
        });

        lb_TaiKhoan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lb_TaiKhoan.setForeground(new java.awt.Color(255, 255, 255));
        lb_TaiKhoan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_TaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/account.png"))); // NOI18N
        lb_TaiKhoan.setText("   TÀI KHOẢN");

        javax.swing.GroupLayout pn_OpenTaiKhoanLayout = new javax.swing.GroupLayout(pn_OpenTaiKhoan);
        pn_OpenTaiKhoan.setLayout(pn_OpenTaiKhoanLayout);
        pn_OpenTaiKhoanLayout.setHorizontalGroup(
            pn_OpenTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_OpenTaiKhoanLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(lb_TaiKhoan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_OpenTaiKhoanLayout.setVerticalGroup(
            pn_OpenTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_TaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        pn_OpenGrSP.setBackground(new java.awt.Color(89, 168, 105));
        pn_OpenGrSP.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pn_OpenGrSPMouseMoved(evt);
            }
        });
        pn_OpenGrSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pn_OpenGrSPMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pn_OpenGrSPMouseExited(evt);
            }
        });

        lblSanPham1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSanPham1.setForeground(new java.awt.Color(255, 255, 255));
        lblSanPham1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSanPham1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/products.png"))); // NOI18N
        lblSanPham1.setText("NHÓM SẢN PHẨM");

        javax.swing.GroupLayout pn_OpenGrSPLayout = new javax.swing.GroupLayout(pn_OpenGrSP);
        pn_OpenGrSP.setLayout(pn_OpenGrSPLayout);
        pn_OpenGrSPLayout.setHorizontalGroup(
            pn_OpenGrSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_OpenGrSPLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(lblSanPham1)
                .addGap(34, 34, 34))
        );
        pn_OpenGrSPLayout.setVerticalGroup(
            pn_OpenGrSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSanPham1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pn_OpenTaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(pn_OpenPhieuXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pn_Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(pn_OpenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pn_OpenSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(pn_OpenXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(pn_OpenNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pn_OpenPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_OpenGrSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenGrSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pn_OpenSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenPhieuXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_OpenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        getContentPane().add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 800));

        panelShow.setBackground(new java.awt.Color(255, 255, 255));
        panelShow.setPreferredSize(new java.awt.Dimension(1000, 700));

        javax.swing.GroupLayout panelShowLayout = new javax.swing.GroupLayout(panelShow);
        panelShow.setLayout(panelShowLayout);
        panelShowLayout.setHorizontalGroup(
            panelShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        panelShowLayout.setVerticalGroup(
            panelShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );

        getContentPane().add(panelShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 0, 1200, 790));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void pn_OpenSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSPMouseClicked
        // TODO add your handling code here:
        showPanel(new QuanLyKho());
    }//GEN-LAST:event_pn_OpenSPMouseClicked

    private void pn_OpenSPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSPMouseExited
        // TODO add your handling code here:
        pn_OpenSP.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenSPMouseExited

    private void pn_OpenSPMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSPMouseMoved
        // TODO add your handling code here:
        pn_OpenSP.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenSPMouseMoved

    private void pn_OpenSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSupplierMouseClicked
        // TODO add your handling code here:
        showPanel(new QuanLyNCC());

    }//GEN-LAST:event_pn_OpenSupplierMouseClicked

    private void pn_OpenSupplierMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSupplierMouseExited
        // TODO add your handling code here:
        pn_OpenSupplier.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenSupplierMouseExited

    private void pn_OpenSupplierMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenSupplierMouseMoved
        // TODO add your handling code here:
        pn_OpenSupplier.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenSupplierMouseMoved

    private void pn_OpenNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenNhapMouseClicked
        // TODO add your handling code here:
        showPanel(new ChiTietNhapHang());
    }//GEN-LAST:event_pn_OpenNhapMouseClicked

    private void pn_OpenNhapMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenNhapMouseExited
        // TODO add your handling code here:
        pn_OpenNhap.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenNhapMouseExited

    private void pn_OpenNhapMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenNhapMouseMoved
        // TODO add your handling code here:
        pn_OpenNhap.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenNhapMouseMoved

    private void pn_OpenPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuNhapMouseClicked
        showPanel(new DSPhieuNhap());
    }//GEN-LAST:event_pn_OpenPhieuNhapMouseClicked

    private void pn_OpenPhieuNhapMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuNhapMouseExited
        // TODO add your handling code here:
        pn_OpenPhieuNhap.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenPhieuNhapMouseExited

    private void pn_OpenPhieuNhapMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuNhapMouseMoved
        // TODO add your handling code here:
        pn_OpenPhieuNhap.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenPhieuNhapMouseMoved

    private void pn_OpenXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenXuatMouseClicked
        // TODO add your handling code here:
        showPanel(new XuatHang());
    }//GEN-LAST:event_pn_OpenXuatMouseClicked

    private void pn_OpenXuatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenXuatMouseExited
        // TODO add your handling code here:
        pn_OpenXuat.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenXuatMouseExited

    private void pn_OpenXuatMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenXuatMouseMoved
        // TODO add your handling code here:
        pn_OpenXuat.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenXuatMouseMoved

    private void pn_OpenPhieuXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuXuatMouseClicked
        // TODO add your handling code here:
        showPanel(new QuanLyXuatHang());
    }//GEN-LAST:event_pn_OpenPhieuXuatMouseClicked

    private void pn_OpenPhieuXuatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuXuatMouseExited
        // TODO add your handling code here:
        pn_OpenPhieuXuat.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenPhieuXuatMouseExited

    private void pn_OpenPhieuXuatMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenPhieuXuatMouseMoved
        // TODO add your handling code here:
        pn_OpenPhieuXuat.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenPhieuXuatMouseMoved

    private void pn_LogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_LogoutMouseClicked
        Login main = new Login();
        main.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_pn_LogoutMouseClicked

    private void pn_LogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_LogoutMouseExited
        // TODO add your handling code here:
        pn_Logout.setBackground(defaultColor);
    }//GEN-LAST:event_pn_LogoutMouseExited

    private void pn_LogoutMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_LogoutMouseMoved
        // TODO add your handling code here:
        pn_Logout.setBackground(clickColor);
    }//GEN-LAST:event_pn_LogoutMouseMoved

    private void pn_OpenTaiKhoanMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenTaiKhoanMouseMoved
        // TODO add your handling code here:
        pn_OpenTaiKhoan.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenTaiKhoanMouseMoved

    private void pn_OpenTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenTaiKhoanMouseClicked
        showPanel(new TaiKhoan());
    }//GEN-LAST:event_pn_OpenTaiKhoanMouseClicked

    private void pn_OpenTaiKhoanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenTaiKhoanMouseExited
        // TODO add your handling code here:
        pn_OpenTaiKhoan.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenTaiKhoanMouseExited

    private void pn_OpenGrSPMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenGrSPMouseMoved
        // TODO add your handling code here:
        pn_OpenGrSP.setBackground(clickColor);
    }//GEN-LAST:event_pn_OpenGrSPMouseMoved

    private void pn_OpenGrSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenGrSPMouseClicked
        // TODO add your handling code here:
        showPanel(new NhomSP());
    }//GEN-LAST:event_pn_OpenGrSPMouseClicked

    private void pn_OpenGrSPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pn_OpenGrSPMouseExited
        // TODO add your handling code here:
        pn_OpenGrSP.setBackground(defaultColor);
    }//GEN-LAST:event_pn_OpenGrSPMouseExited

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_Logout;
    private javax.swing.JLabel lb_PhieuNhap;
    private javax.swing.JLabel lb_PhieuXuat;
    private javax.swing.JLabel lb_Supplier;
    private javax.swing.JLabel lb_TaiKhoan;
    private javax.swing.JLabel lblNhapHang;
    private javax.swing.JLabel lblSanPham;
    private javax.swing.JLabel lblSanPham1;
    private javax.swing.JLabel lblXuatHang;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelShow;
    private javax.swing.JPanel pn_Logout;
    private javax.swing.JPanel pn_OpenGrSP;
    private javax.swing.JPanel pn_OpenNhap;
    private javax.swing.JPanel pn_OpenPhieuNhap;
    private javax.swing.JPanel pn_OpenPhieuXuat;
    private javax.swing.JPanel pn_OpenSP;
    private javax.swing.JPanel pn_OpenSupplier;
    private javax.swing.JPanel pn_OpenTaiKhoan;
    private javax.swing.JPanel pn_OpenXuat;
    // End of variables declaration//GEN-END:variables
}
