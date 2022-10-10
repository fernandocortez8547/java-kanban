package tests;

import manager.InMemoryTaskManager;
import manager.Manager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = (InMemoryTaskManager) Manager.getDefaultTask();
    }
}
