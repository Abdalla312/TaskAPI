package domain_practice;

public class Task extends BaseEntity {
    private String title;
    private String description;
    private TaskStatus status;
    private User assignee;

    public Task(String title) {
        this(title, "No description");
    }

    public Task(String title, String description) {
        super();
        setTitle(title);
        setDescription(description);
        this.status = TaskStatus.TODO;

    }

    // string get title
    public String getTitle() {
        return title;
    }

    // string get description
    public String getDescription() {
        return description;
    }

    // taskstatus get status
    public TaskStatus getStatus() {
        return status;
    }

    // user get assignee
    public User getAssignee() {
        return assignee;
    }

    // assign to-> user
    public void assignTo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        if (this.assignee != null && this.assignee != user) {
            this.assignee.removeTaskInternal(this);
        }

        this.assignee = user;
        user.addTaskInternal(this);
    }

    // start task
    public void start() {
        if (status == TaskStatus.DONE) {
            throw new IllegalArgumentException("Cannot start a completed task.");
        }
        status = TaskStatus.IN_PROGRESS;
    }

    // complete task
    public void complete() {
        status = TaskStatus.DONE;
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title can not be empty.");
        }
        this.title = title;
    }

    private void setDescription(String description) {
        this.description = (description == null || title.isBlank()) ? "No description" : description;
    }

    @Override
    public String toString() {
        return "Task{title= " + title + "', status=" + status + "}";
    }
}
