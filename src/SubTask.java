import java.util.ArrayList;

public class SubTask extends Task{
    int supId;
    ArrayList<Integer> curId;
    public SubTask(int id, String name, String description, String status, int supId) {
        super(id, name, description, status);
        this.supId = supId;
    }
}
