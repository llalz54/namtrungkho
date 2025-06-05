package DAO;

import ConDB.DBAccess;
import DTO.CTPN;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CTPN_DATA {
    private ArrayList<CTPN> listCTPN = null;
    
    public CTPN_DATA(){
        docListCTPN();
    }
    
    public void docListCTPN(){
        listCTPN = getListCTPN();
    }
    
    public ArrayList<CTPN> getListCTPN(){
        try {
            DBAccess acc = new DBAccess();
            ResultSet rs = acc.Query("SELECT * FROM CTPN");
            ArrayList<CTPN> ds_ctpn = new ArrayList<>();
            while (rs.next()) {
                CTPN ctpn = new CTPN();
                
                ctpn.setIdpn(rs.getInt(1));
                ctpn.setSerial(rs.getString(2));

                ds_ctpn.add(ctpn);
            }
            return ds_ctpn;
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách CTPN");
        }
        return null;
    }
    
}
