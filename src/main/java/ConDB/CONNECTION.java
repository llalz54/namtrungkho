package ConDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CONNECTION {

    public static Connection getConnection() {
        try {

          String URL = "jdbc:sqlserver://10.10.1.250:1433;DatabaseName=QLKHO;user=sa;password=thongAd@12345;"
                   + "encrypt=true; trustServerCertificate=true;sslProtocol=TLSv1.2";
         //  String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=QLKHO;user=sa;password=123;"
            //       + "encrypt=true; trustServerCertificate=true;sslProtocol=TLSv1.2";
            Connection con = DriverManager.getConnection(URL);
            return con;
        } catch (SQLException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
//    public static void main(String[] args) {
//        Connection conn = CONNECTION.getConnection();
//        if (conn != null) {
//            System.out.println("Kết nối thành công từ hàm main!");
//        } else {
//            System.out.println("Kết nối thất bại từ hàm main.");
//        }
//    }
}
