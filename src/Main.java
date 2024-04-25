import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;

class Task {
    private final int id;
    private String description;
    private String status;

    public Task(int id, String description, String status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task ID: " + id + ", Description: \"" + description + "\", Status: \"" + status + "\"";
    }
}

class TaskManager {
    private final List<Task> tasks;
    private final AtomicInteger taskIdCounter;

    public TaskManager() {
        tasks = new ArrayList<>();
        taskIdCounter = new AtomicInteger(1);
    }

    public void addTask(String description) {
        int taskId = taskIdCounter.getAndIncrement();
        Task newTask = new Task(taskId, description, "Pending");
        tasks.add(newTask);
        System.out.println("User " + Thread.currentThread().getId() + ": Adding task - \"" + description + "\"");
        System.out.println("User " + Thread.currentThread().getId() + ": Task added successfully. Task ID: " + taskId);
    }

    public void updateTask(int taskId, String newDescription, String newStatus) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setDescription(newDescription);
                task.setStatus(newStatus);
                System.out.println("User " + Thread.currentThread().getId() + ": Updating task - ID: " + taskId +
                        ", Description: \"" + newDescription + "\", Status: \"" + newStatus + "\"");
                System.out.println("User " + Thread.currentThread().getId() + ": Task updated successfully.");
                return;
            }
        }
        System.out.println("User " + Thread.currentThread().getId() + ": Task with ID " + taskId + " not found.");
    }

    public void deleteTask(int taskId) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == taskId) {
                iterator.remove();
                System.out.println("User " + Thread.currentThread().getId() + ": Deleting task - ID: " + taskId);
                System.out.println("User " + Thread.currentThread().getId() + ": Task deleted successfully.");
                return;
            }
        }
        System.out.println("User " + Thread.currentThread().getId() + ": Task with ID " + taskId + " not found.");
    }

    public void listTasks() {
        System.out.println("User " + Thread.currentThread().getId() + ": Listing all tasks:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }


}

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        new Thread(() -> {
            taskManager.addTask("User 1: Complete project report");
            taskManager.updateTask(1, "User 1: Complete project report", "Completed");
        }).start();

        new Thread(() -> {
            taskManager.addTask("User 2: Prepare presentation slides");
            taskManager.deleteTask(2);
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        taskManager.listTasks();
    }
}