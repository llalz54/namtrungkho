package DAO;

import ConDB.DBAccess;
import DTO.NHOMSP;
import DTO.SANPHAM;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 *
 * @author Admin
 */
public class NHOMSP_DATA {

    private ArrayList<NHOMSP> listnhomSP = null;

    public NHOMSP_DATA() {
        docListnhomSP();
    }

    public void docListnhomSP() {
        listnhomSP = getListnhomSP();
    }

    public ArrayList<NHOMSP> getListnhomSP() {
        DBAccess acc = null;
        try {
            acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM NhomSP");
            ArrayList<NHOMSP> dssp = new ArrayList<>();
            while (rs.next()) {
                NHOMSP gr = new NHOMSP();
                gr.setGroupID(rs.getInt(1));
                gr.setName(rs.getString(2).trim());
                gr.setStatus(rs.getString(3));
                dssp.add(gr);
            }
            return dssp;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhóm sản phẩm!!!");
            return null;
        } finally {
            if(acc != null) {
                acc.close();
            }
        }
    }

    public int name_to_ID(String name) {
        for (NHOMSP gr : listnhomSP) {
            if (name.equals(gr.getName())) {                
                return gr.getGroupID();
            }
        }
        return -1;
    }

    public String iD_to_name(int id) {
        for (NHOMSP gr : listnhomSP) {
            if (id==(gr.getGroupID())) {
                return gr.getName();
            }
        }
        return null;
    }
}
