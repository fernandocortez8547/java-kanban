package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static util.FileConverter.FORMATTER;


public class Task /*implements Cloneable, Comparable<Task>*/ {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    protected long duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(int id, String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

        calculateEndTime();
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        calculateEndTime();
    }

    public void calculateEndTime() {
        endTime = startTime.plusMinutes(duration);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, description, status);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, duration, startTime, endTime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration=" + duration +
                '}';
    }

//    @Override
//    public int compareTo(Task task) {
//        if(getStartTime().isAfter(task.getStartTime())) {
//            return -1;
//        } else if (getStartTime().isBefore(task.getStartTime())) {
//            return 1;
//        }
//        return 0;
//    }
}

