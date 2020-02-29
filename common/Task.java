import java.sql.Timestamp;

class Task
{
    private Integer id;
    private Timestamp deadline;
    private String contents;
    private Type type;
    private Status status;

    Task(int id, Timestamp deadline, String contents, Type type)
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
}