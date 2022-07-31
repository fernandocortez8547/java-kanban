package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subIds;

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
        subIds = new ArrayList<>();
    }

    public void setSubIds(int id) {
        subIds.add(id);
    }

    public void deleteSubIds() {
        subIds.clear();
    }

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subTasks size ='" + subIds.size() + '\'' +
                '}';
    }
}

