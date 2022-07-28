import java.util.ArrayList;

public class SubTask extends Task{
    int supId;
    public SubTask(int id, String name, String description, String status, int supId) {
        super(id, name, description, status);
        this.supId = supId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "supId=" + supId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
