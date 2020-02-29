import java.sql.Timestamp;
import java.util.HashMap;
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

    Child(Integer id, String childUsername, String parentUsername, CharacterType characterType)
    {
        this.id = id;
        this.childUsername = childUsername;
        this.parentUsername = parentUsername;
        tasks = new HashMap<>();
        level = 1;
        XP = 0;
        this.characterType = characterType;
    }

    public void addTask(int id, Timestamp deadline, String contenst, Type type)
    {
        Task task = new Task(id, deadline, contenst, type);
        tasks.put(id, task);
    }

    public void setStatus(int id, Status status)
    {
        Task task = tasks.get(id);
        task.setStatus(status);
    }

}