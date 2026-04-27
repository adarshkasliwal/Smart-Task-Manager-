/**
 * Task.java
 * Represents an individual task with a name, description, priority, and completion status.
 * Implements Serializable-style toFileString/fromFileString for file-based persistence.
 *
 * @author Adarsh Kasliwal
 */
public class Task {
    private String name;
    private String description;
    private String priority; // High, Medium, Low
    private boolean completed;

    /**
     * Constructs a new Task with the given details.
     *
     * @param name        the name/title of the task
     * @param description a brief description of the task
     * @param priority    the priority level (High, Medium, Low)
     */
    public Task(String name, String description, String priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.completed = false;
    }

    /**
     * Constructs a Task with all fields, including completion status.
     * Used when loading tasks from a file.
     *
     * @param name        the name/title of the task
     * @param description a brief description of the task
     * @param priority    the priority level (High, Medium, Low)
     * @param completed   whether the task is completed
     */
    public Task(String name, String description, String priority, boolean completed) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
    }

    // ---- Getters ----

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    // ---- Setters ----

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Marks this task as completed.
     */
    public void markCompleted() {
        this.completed = true;
    }

    /**
     * Converts the task to a pipe-delimited string for file storage.
     * Format: name|description|priority|completed
     *
     * @return a string representation suitable for writing to a file
     */
    public String toFileString() {
        return name + "|" + description + "|" + priority + "|" + completed;
    }

    /**
     * Creates a Task object from a pipe-delimited file string.
     *
     * @param line the pipe-delimited string read from a file
     * @return a Task object, or null if the line is invalid
     */
    public static Task fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length == 4) {
            String name = parts[0].trim();
            String description = parts[1].trim();
            String priority = parts[2].trim();
            boolean completed = Boolean.parseBoolean(parts[3].trim());
            return new Task(name, description, priority, completed);
        }
        return null;
    }

    /**
     * Returns a formatted string representation of the task for console display.
     */
    @Override
    public String toString() {
        String status = completed ? "Completed" : "Pending";
        return String.format("  Name        : %s\n  Description : %s\n  Priority    : %s\n  Status      : %s",
                name, description, priority, status);
    }
}
