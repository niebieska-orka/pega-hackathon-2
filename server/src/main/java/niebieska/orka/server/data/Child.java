package niebieska.orka.server.data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Child {
    private int level;
    private int XP;
    private CharacterType characterType;
    private String id;
    private String childUsername;
    private String parentUsername;
    private Map<Integer, Task> tasks;

    public Child(String id, String childUsername, String parentUsername, CharacterType characterType) {
        this.id = id;
        this.childUsername = childUsername;
        this.parentUsername = parentUsername;
        tasks = new HashMap<>();
        level = 1;
        XP = 0;
        this.characterType = characterType;
    }

    public void addTask(int id, Timestamp deadline, String name, String description) {
        Task task = new Task(id, deadline, name, description);
        tasks.put(id, task);
    }

    public void setStatus(int id, Status status) {
        Task task = tasks.get(id);
        task.setStatus(status);
    }
}