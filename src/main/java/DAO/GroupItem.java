package DAO;


public class GroupItem {
    private int id;
    private String name;

    public GroupItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name; // Đây là cái hiển thị trong JComboBox
    }
}

