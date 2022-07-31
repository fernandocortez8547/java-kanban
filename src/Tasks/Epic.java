package Tasks;

import java.util.ArrayList;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subIds, epic.subIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subIds);
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

