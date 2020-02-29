import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Child
{
    Integer id;
    String childUsername;
    String parentUsername;

    Map<Integer, Task> tasks;

    int level;
    int XP;
    CharacterType characterType;

    public Child(Integer id, String childUsername, String parentUsername, CharacterType characterType)
    {
        this.id = id;
        this.childUsername = childUsername;
        this.parentUsername = parentUsername;
        tasks = new HashMap<>();
        level = 1;
        XP = 0;
        this.characterType = characterType;
    }

    public void addTask(int id, Timestamp deadline, String contenst, Type type, int XP)
    {
        Task task = new Task(id, deadline, contenst, type, XP);
        tasks.put(id, task);
    }

    public void setStatus(int id, Status status)
    {
        Task task = tasks.get(id);
        task.setStatus(status);
        if(status == Status.COMPLETED)getReward(task);
    }

    public List<Task> getChildUpdateAndUpdateStatus()
    {
        List<Task> confirmedTasks = new LinkedList<>();
        for (Task task: tasks.values()) {
            if(task.getStatus() == Status.CONFIRMED)
            {
                task.setStatus(Status.COMPLETED);
                confirmedTasks.add(task);
            }
        }
        return confirmedTasks;
    }

    public List<Task> getParentUpdate()
    {
        List<Task> confirmedTasks = new LinkedList<>();
        for (Task task: tasks.values()) {
            if(task.getStatus() == Status.TO_CONFIRM)
            {
                confirmedTasks.add(task);
            }
        }
        return confirmedTasks;
    }

    public void confirmTasks()
    {
        //TO DO
    }

    private void getReward(Task task)
    {
        XP = XP + task.getXP();
        while(XP >= level * 100)
        {
            XP = XP - level * 100;
            level++;
        }
    }

    public int getXP(){return XP;}

    public int getLevel(){return level;}

}