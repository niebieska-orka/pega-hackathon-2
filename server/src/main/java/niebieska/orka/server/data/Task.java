package niebieska.orka.server.data;

import java.sql.Timestamp;

public class Task {
    private String id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;
    private int xp;
    private byte[] content;

    Task(String id, Timestamp deadline, String name, String description, int xp) {
        this.id = id;
        this.deadline = deadline;
        this.status = Status.TO_DO;
        this.name = name;
        this.description = description;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getXp() {
        return xp;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}