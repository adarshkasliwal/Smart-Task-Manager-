import java.util.Scanner;

/**
 * SmartTaskManager.java
 * Main entry point for the Smart Task Manager console application.
 * Provides a menu-driven interface for managing daily tasks.
 *
 * Features:
 *  - Add new tasks with name, description, and priority
 *  - View all / pending / completed tasks
 *  - Mark tasks as completed
 *  - Update task details
 *  - Delete tasks
 *  - Search tasks by keyword
 *  - Persistent storage using file I/O
 *
 * @author Adarsh Kasliwal
 */
public class SmartTaskManager {

    private static Scanner scanner = new Scanner(System.in);
    private static TaskManager taskManager = new TaskManager();

    /**
     * Main method — program entry point.
     * Displays the menu in a loop until the user chooses to exit.
     */
    public static void main(String[] args) {
        System.out.println("\n  ****************************************************");
        System.out.println("  *                                                  *");
        System.out.println("  *          SMART TASK MANAGER APPLICATION           *");
        System.out.println("  *                                                  *");
        System.out.println("  *     Manage your daily tasks efficiently!          *");
        System.out.println("  *                                                  *");
        System.out.println("  ****************************************************\n");

        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getValidIntInput("  Enter your choice: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    taskManager.viewAllTasks();
                    break;
                case 3:
                    taskManager.viewPendingTasks();
                    break;
                case 4:
                    taskManager.viewCompletedTasks();
                    break;
                case 5:
                    markTaskCompleted();
                    break;
                case 6:
                    updateTask();
                    break;
                case 7:
                    deleteTask();
                    break;
                case 8:
                    searchTask();
                    break;
                case 9:
                    running = false;
                    System.out.println("\n  ****************************************************");
                    System.out.println("  *   Thank you for using Smart Task Manager!        *");
                    System.out.println("  *   Your tasks have been saved. Goodbye!            *");
                    System.out.println("  ****************************************************\n");
                    break;
                default:
                    System.out.println("\n  >> Invalid choice! Please select a number from 1 to 9.\n");
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu options.
     */
    private static void displayMenu() {
        System.out.println("  ============================================");
        System.out.println("                  MAIN MENU                   ");
        System.out.println("  ============================================");
        System.out.println("    1. Add New Task");
        System.out.println("    2. View All Tasks");
        System.out.println("    3. View Pending Tasks");
        System.out.println("    4. View Completed Tasks");
        System.out.println("    5. Mark Task as Completed");
        System.out.println("    6. Update a Task");
        System.out.println("    7. Delete a Task");
        System.out.println("    8. Search Tasks");
        System.out.println("    9. Exit");
        System.out.println("  ============================================");
    }

    /**
     * Prompts the user to add a new task with name, description, and priority.
     */
    private static void addTask() {
        System.out.println("\n  --- Add New Task ---");

        System.out.print("  Enter task name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\n  >> Task name cannot be empty.\n");
            return;
        }

        System.out.print("  Enter task description: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("\n  >> Task description cannot be empty.\n");
            return;
        }

        String priority = getValidPriority();

        Task task = new Task(name, description, priority);
        taskManager.addTask(task);
    }

    /**
     * Prompts the user to select a task and marks it as completed.
     */
    private static void markTaskCompleted() {
        if (taskManager.getTaskCount() == 0) {
            System.out.println("\n  >> No tasks available to mark.\n");
            return;
        }

        taskManager.viewAllTasks();
        int taskNumber = getValidIntInput("  Enter task number to mark as completed: ");
        taskManager.markTaskCompleted(taskNumber);
    }

    /**
     * Prompts the user to update an existing task.
     * Fields left blank will retain their current values.
     */
    private static void updateTask() {
        if (taskManager.getTaskCount() == 0) {
            System.out.println("\n  >> No tasks available to update.\n");
            return;
        }

        taskManager.viewAllTasks();
        int taskNumber = getValidIntInput("  Enter task number to update: ");

        if (taskNumber < 1 || taskNumber > taskManager.getTaskCount()) {
            System.out.println("\n  >> Invalid task number.\n");
            return;
        }

        System.out.println("\n  --- Update Task (press Enter to skip a field) ---");

        System.out.print("  New task name: ");
        String newName = scanner.nextLine().trim();

        System.out.print("  New description: ");
        String newDescription = scanner.nextLine().trim();

        System.out.print("  New priority (High/Medium/Low or Enter to skip): ");
        String newPriority = scanner.nextLine().trim();
        if (!newPriority.isEmpty()) {
            newPriority = normalizePriority(newPriority);
            if (newPriority == null) {
                System.out.println("\n  >> Invalid priority. Update cancelled.\n");
                return;
            }
        }

        taskManager.updateTask(taskNumber, newName, newDescription, newPriority);
    }

    /**
     * Prompts the user to delete a task by number.
     */
    private static void deleteTask() {
        if (taskManager.getTaskCount() == 0) {
            System.out.println("\n  >> No tasks available to delete.\n");
            return;
        }

        taskManager.viewAllTasks();
        int taskNumber = getValidIntInput("  Enter task number to delete: ");
        taskManager.deleteTask(taskNumber);
    }

    /**
     * Prompts the user to enter a keyword and searches tasks by name.
     */
    private static void searchTask() {
        System.out.print("\n  Enter search keyword: ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("\n  >> Search keyword cannot be empty.\n");
            return;
        }
        taskManager.searchTasks(keyword);
    }

    /**
     * Prompts the user for a valid priority level (High, Medium, or Low).
     *
     * @return the normalized priority string
     */
    private static String getValidPriority() {
        while (true) {
            System.out.print("  Enter priority (High/Medium/Low): ");
            String input = scanner.nextLine().trim();
            String normalized = normalizePriority(input);
            if (normalized != null) {
                return normalized;
            }
            System.out.println("  >> Invalid priority. Please enter High, Medium, or Low.");
        }
    }

    /**
     * Normalizes a priority string to a standard format.
     *
     * @param input the user input
     * @return "High", "Medium", or "Low", or null if invalid
     */
    private static String normalizePriority(String input) {
        if (input == null) return null;
        switch (input.toLowerCase()) {
            case "high":
            case "h":
                return "High";
            case "medium":
            case "med":
            case "m":
                return "Medium";
            case "low":
            case "l":
                return "Low";
            default:
                return null;
        }
    }

    /**
     * Reads and validates an integer input from the user.
     * Re-prompts on invalid input.
     *
     * @param prompt the prompt message
     * @return the valid integer entered by the user
     */
    private static int getValidIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  >> Please enter a valid number.");
            }
        }
    }
}
