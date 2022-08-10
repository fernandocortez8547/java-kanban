package Manager;

import Tasks.Task;

public abstract class Manager <T extends Task> {
    T getDefault() {
       return T;
    }
}
