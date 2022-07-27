import java.util.ArrayList;

public class Epic extends Task{
    String description;
    ArrayList<Integer> subIds;

    public Epic(int id, String name, String description, String status ) {
        super(id, name, description, status);
    }
    void setSubIds(int id) {
        subIds.add(id);
    }
    ArrayList<Integer> getSubIds() {
       return subIds;
    }
    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
