package DTO;

public class NHOMSP {
    private int groupID;
    private String name;
    private String status;
    
    public NHOMSP(){        
    }

    public NHOMSP(int groupID, String name, String status) {
        this.groupID = groupID;
        this.name = name;
        this.status = status;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }      

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
