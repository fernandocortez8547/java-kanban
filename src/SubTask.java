import java.util.ArrayList;

public class SubTask extends Task{
    private int supId;
    public SubTask(int id, String name, String description, String status, int supId) {
        super(id, name, description, status);
        this.supId = supId;
    }

    public int getSupId() {
        return supId;
    }

    public void setSupId(int supId) {
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
