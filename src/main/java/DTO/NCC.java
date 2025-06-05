
package DTO;

/**
 *
 * @author Admin
 */
public class NCC {
    private int supplier_id;
    private String fullName;
    private String name;
    private String MST;
    private String diaChi;
    private String status;

    public NCC() {
    }

    
    public NCC(int supplier_id, String name,String fullName, String MST, String diaChi, String status) {
        this.supplier_id = supplier_id;
        this.name = name;
        this.fullName = fullName;
        this.MST = MST;
        this.diaChi = diaChi;
        this.status = status;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getMST() {
        return MST;
    }

    public void setMST(String MST) {
        this.MST = MST;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return  name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
      
}
