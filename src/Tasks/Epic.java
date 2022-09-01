package Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subIds;

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        subIds = new ArrayList<>();
    }

    public void addSubTaskId(int id) {
        subIds.add(id);
    }

    public void removeSubTaskId(Integer id) {
        if (subIds.contains(id)) {
            subIds.remove(id);
        }
    }

    public void clearingSubIds() {
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

    public List<Integer> getSubIds() {
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

