package sample.jherdman.fearlesstrialproject.model;

public class DataItem {
    private final int id;
    private String name;

    public DataItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
