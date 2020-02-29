package niebieska.orka.server.data;

import java.sql.Timestamp;

class Task {
    private Integer id;
    private Timestamp deadline;
    private String name;
    private String description;
    private Status status;

    Task(int id, Timestamp deadline, String name, String description) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.deadline = deadline;
        this.status = Status.TO_DO;
    }

    public int getId() {
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
}