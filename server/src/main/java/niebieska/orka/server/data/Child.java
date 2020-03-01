package niebieska.orka.server.data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Child {
    private Map<String, Task> tasks;
    private int level;
    private int XP;
    private CharacterType characterType;
    private String id;
    private String childUsername;
    private String parentUsername;

    public Child(String id, String childUsername, String parentUsername, CharacterType characterType) {
        this.id = id;
        this.childUsername = childUsername;
        this.parentUsername = parentUsername;
        tasks = new HashMap<>();
        level = 1;
        XP = 0;
        this.characterType = characterType;
    }

    public void addTask(String id, Timestamp deadline, String name, String description, int xp) {
        Task task = new Task(id, deadline, name, description, xp);
        tasks.put(id, task);
    }

    public void setTaskStatus(String id, Status status) {
        Task task = tasks.get(id);
        task.setStatus(status);
        if (status == Status.COMPLETED) updateXpAndLevel(task);
    }

    public List<Task> getChildUpdateAndUpdateStatus() {
        List<Task> confirmedTasks = new LinkedList<>();
        tasks.values().stream()
                .filter(task -> !task.getStatus().equals(Status.COMPLETED))
                .forEach(task -> {
                    task.setStatus(Status.TO_CONFIRM);
                    confirmedTasks.add(task);
                });
        return confirmedTasks;
    }

    public List<Task> getParentUpdate() {
        List<Task> confirmedTasks = new LinkedList<>();
        tasks.values().stream()
                .filter(task -> task.getStatus().equals(Status.TO_CONFIRM))
                .forEach(confirmedTasks::add);
        return confirmedTasks;
    }

    private void updateXpAndLevel(Task task) {
        XP = XP + task.getXp();
        while (XP >= level * 100) {
            XP = XP - level * 100;
            level++;
        }
    }

    public int getXP() {
        return XP;
    }

    public int getLevel() {
        return level;
    }

    public void addTaskContent(String id, byte[] content) {
        tasks.get(id).setContent(content);
    }
}