import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TaskManager.java
 * Manages a collection of Task objects. Provides operations such as adding,
 * viewing, updating, deleting, and marking tasks as completed.
 * Handles file-based persistence using FileWriter and BufferedReader.
 *
 * @author Adarsh Kasliwal
 */
public class TaskManager {

    private ArrayList<Task> tasks;
    private static final String FILE_NAME = "tasks.txt";

    /**
     * Constructs a new TaskManager and loads any previously saved tasks from file.
     */
    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasksFromFile();
    }

    /**
     * Adds a new task to the list and persists data to file.
     *
     * @param task the Task object to add
     */
    public void addTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
        System.out.println("\n  >> Task added successfully!\n");
    }

    /**
     * Displays all tasks in a formatted list.
     * Shows a message if no tasks are available.
     */
    public void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\n  >> No tasks found. Add a task to get started!\n");
            return;
        }

        System.out.println("\n  ============================================");
        System.out.println("               ALL TASKS (" + tasks.size() + ")");
        System.out.println("  ============================================");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\n  [Task #" + (i + 1) + "]");
            System.out.println(tasks.get(i));
            System.out.println("  --------------------------------------------");
        }
        System.out.println();
    }

    /**
     * Displays only pending (incomplete) tasks.
     */
    public void viewPendingTasks() {
        ArrayList<Task> pending = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                pending.add(task);
            }
        }

        if (pending.isEmpty()) {
            System.out.println("\n  >> No pending tasks. Great job!\n");
            return;
        }

        System.out.println("\n  ============================================");
        System.out.println("            PENDING TASKS (" + pending.size() + ")");
        System.out.println("  ============================================");

        int index = 1;
        for (Task task : pending) {
            System.out.println("\n  [Task #" + index + "]");
            System.out.println(task);
            System.out.println("  --------------------------------------------");
            index++;
        }
        System.out.println();
    }

    /**
     * Displays only completed tasks.
     */
    public void viewCompletedTasks() {
        ArrayList<Task> completed = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed.add(task);
            }
        }

        if (completed.isEmpty()) {
            System.out.println("\n  >> No completed tasks yet.\n");
            return;
        }

        System.out.println("\n  ============================================");
        System.out.println("           COMPLETED TASKS (" + completed.size() + ")");
        System.out.println("  ============================================");

        int index = 1;
        for (Task task : completed) {
            System.out.println("\n  [Task #" + index + "]");
            System.out.println(task);
            System.out.println("  --------------------------------------------");
            index++;
        }
        System.out.println();
    }

    /**
     * Marks a task as completed by its 1-based index.
     *
     * @param taskNumber the 1-based task number
     */
    public void markTaskCompleted(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("\n  >> Invalid task number. Please try again.\n");
            return;
        }

        Task task = tasks.get(taskNumber - 1);
        if (task.isCompleted()) {
            System.out.println("\n  >> Task \"" + task.getName() + "\" is already completed.\n");
        } else {
            task.markCompleted();
            saveTasksToFile();
            System.out.println("\n  >> Task \"" + task.getName() + "\" marked as completed!\n");
        }
    }

    /**
     * Updates an existing task's name, description, or priority.
     *
     * @param taskNumber     the 1-based task number
     * @param newName        the new name (empty string to keep existing)
     * @param newDescription the new description (empty string to keep existing)
     * @param newPriority    the new priority (empty string to keep existing)
     */
    public void updateTask(int taskNumber, String newName, String newDescription, String newPriority) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("\n  >> Invalid task number. Please try again.\n");
            return;
        }

        Task task = tasks.get(taskNumber - 1);

        if (!newName.isEmpty()) {
            task.setName(newName);
        }
        if (!newDescription.isEmpty()) {
            task.setDescription(newDescription);
        }
        if (!newPriority.isEmpty()) {
            task.setPriority(newPriority);
        }

        saveTasksToFile();
        System.out.println("\n  >> Task updated successfully!\n");
    }

    /**
     * Deletes a task by its 1-based index.
     *
     * @param taskNumber the 1-based task number
     */
    public void deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("\n  >> Invalid task number. Please try again.\n");
            return;
        }

        Task removed = tasks.remove(taskNumber - 1);
        saveTasksToFile();
        System.out.println("\n  >> Task \"" + removed.getName() + "\" deleted successfully!\n");
    }

    /**
     * Returns the total number of tasks.
     *
     * @return the size of the task list
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Searches tasks by name (case-insensitive, partial match).
     *
     * @param keyword the keyword to search for
     */
    public void searchTasks(String keyword) {
        ArrayList<Task> results = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(task);
            }
        }

        if (results.isEmpty()) {
            System.out.println("\n  >> No tasks found matching \"" + keyword + "\".\n");
            return;
        }

        System.out.println("\n  ============================================");
        System.out.println("        SEARCH RESULTS (" + results.size() + ")");
        System.out.println("  ============================================");

        int index = 1;
        for (Task task : results) {
            System.out.println("\n  [Result #" + index + "]");
            System.out.println(task);
            System.out.println("  --------------------------------------------");
            index++;
        }
        System.out.println();
    }

    // ---- File Handling ----

    /**
     * Saves all tasks to the file using FileWriter.
     * Each task is written as a pipe-delimited line.
     */
    private void saveTasksToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("  >> Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the file using BufferedReader.
     * Populates the tasks ArrayList from saved data.
     */
    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromFileString(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet — first run. This is expected.
        }
    }
}
