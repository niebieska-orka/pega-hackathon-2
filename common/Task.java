import java.sql.Timestamp;

class Task
{
    private Integer id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Type type;
    private Status status;
    private int XP;

    Task(int id, Timestamp deadline, String contents, Type type, int XP)
    {
        this.id = id;
        this.contents = contents;
        this.deadline = deadline;
        this.type = type;
        this.status = Status.TO_DO;
    }

    public int getId()
    {
        return id;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Timestamp getDeadline()
    {
        return deadline;
    }

    public String getContents()
    {
        return contents;
    }

    public Type getType()
    {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public int getXP(){return XP;}
}