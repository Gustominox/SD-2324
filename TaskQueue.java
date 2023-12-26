import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

  private final int totalMemory = 500;
  private int currentMemory = 0;

  private Queue<Task> taskQueue;

  private Lock lock;
  private Condition notEmpty;

  public TaskQueue() {
    this.taskQueue = new LinkedList<>();

    this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
  }

  public void addTask(Task task) {
    lock.lock();
    try {
      taskQueue.add(task);
      notEmpty.signal(); // Notify any waiting threads that a task is available
    } finally {
      lock.unlock();
    }
  }

  public void reduceMemory(int memoryToReduce) {
    lock.lock();
    try {
      // Ensure the memory reduction won't go below zero
      this.currentMemory = Math.max(0, this.currentMemory - memoryToReduce);
    } finally {
      lock.unlock();
    }
  }

  public Task getTask() throws InterruptedException {
    lock.lock();
    try {
      while (taskQueue.isEmpty()) {
        notEmpty.await(); // Wait until a task is available
      }

      Task nextTask = taskQueue.peek(); // Peek at the next task without removing it

      // List to store tasks that don't fit into available memory
      List<Task> tasksToRemove = new ArrayList<>();

      while (
        nextTask != null &&
        (this.currentMemory + nextTask.getMemory()) > this.totalMemory
      ) {
        // The task doesn't fit into available memory, add it to the list for later addition
        tasksToRemove.add(taskQueue.poll());

        // Check the next task in the queue
        nextTask = taskQueue.peek();
      }

      if (nextTask != null) {
        // Update the current memory and remove the suitable task from the queue
        this.currentMemory += nextTask.getMemory();
        taskQueue.poll();
      }

      // Add the removed tasks back to the queue
      taskQueue.addAll(tasksToRemove);

      return nextTask;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    TaskQueue taskQueue = new TaskQueue();

    // Example of adding tasks to the queue
    taskQueue.addTask(new Task("Task 1", 200));
    taskQueue.addTask(new Task("Task 2", 200));
    taskQueue.addTask(new Task("Task 3", 200));
    taskQueue.addTask(new Task("Task 4", 200));

    taskQueue.addTask(new Task("Task 5", 100));

    // Example of processing tasks from the queue in a separate thread

    for (int i = 0; i < 3; i++) {
      Thread poolThread = new Thread(() -> {
        try {
          while (true) {
            Task task = taskQueue.getTask();
            if (task != null) {
            //   System.out.println(task.toString());
              task.run();
              taskQueue.reduceMemory(task.getMemory());
            } else {
              // System.out.println("No task fits!!!");
            }
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      poolThread.start();
    }
  }
}
