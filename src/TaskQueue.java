import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

  private final int totalMemory;
  private int currentMemory = 0;

  private Queue<Task> taskQueue;

  ReentrantLock lock = new ReentrantLock();
  Condition notEmpty;
  Condition memUpdated;

  public TaskQueue(int totalMemory) {

    this.totalMemory = totalMemory;
    this.taskQueue = new PriorityQueue<>();

    // this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
    this.memUpdated = lock.newCondition();
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
      this.memUpdated.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public Task getTask() throws InterruptedException {
    lock.lock();
    try {
      while (taskQueue.isEmpty()) {
        System.out.println(
          "Fila vazia Thread a espera!! -> " + Thread.currentThread().getName()
        );
        notEmpty.await(); // Wait until a task is available
      }

      Task nextTask = taskQueue.peek();
      Task currentTask = taskQueue.peek();

      if (nextTask.getPriority() >= 5) {
        // System.out.println("Checking " + nextTask.getName()
              // + "and Checking " + nextTask.getName() );
        while (
          (nextTask.getName() == currentTask.getName()) && // se a task ja saiu do topo da queue
          (this.currentMemory + nextTask.getMemory()) > this.totalMemory // se ela ainda esta no topo da queue e ja ha memoria para executar
        ) {
          System.out.println(
            "Awaiting to start " +
            nextTask.getName() +
            " -> " +
            Thread.currentThread().getName()
          );
          memUpdated.await();
          currentTask = taskQueue.peek();
        }
        // nextTask.setPriority(4); // try to
        // return null;
      }

      nextTask = taskQueue.poll();

      // List to store tasks that don't fit into available memory
      List<Task> tasksToRemove = new ArrayList<>();

      while (
        nextTask != null && // chegou ao fim da queue nenhuma da
        (this.currentMemory + nextTask.getMemory()) > this.totalMemory // encontrou uma que da para executar
      ) {
        // The task doesn't fit into available memory, add it to the list for later addition

        tasksToRemove.add(nextTask);

        // Check the next task in the queue
        nextTask = taskQueue.poll();
      }

      // saiu queue
      if (
        nextTask != null
      ) for (Task task : tasksToRemove) task.increasePriority();

      taskQueue.addAll(tasksToRemove);

      if (nextTask != null) {
        // Update the current memory and remove the suitable task from the queue
        this.currentMemory += nextTask.getMemory();
      } else { // MEMORIA CHEIA
        System.out.println(
          "Memoria cheia thread a espera!!!" +
          " -> " +
          Thread.currentThread().getName()
        );
        memUpdated.await(); // Wait until a task is available
      }

      // Add the removed tasks back to the queue

      return nextTask;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    TaskQueue taskQueue = new TaskQueue(600);

    // Example of adding tasks to the queue
    taskQueue.addTask(new Task("Task 1", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 2", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 3", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 4", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 5", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 9", 200, 5, new byte[1000]));

    taskQueue.addTask(new Task("Task 6", 200, 0, new byte[1000]));
    taskQueue.addTask(new Task("Task 7", 200, 5, new byte[1000]));
    taskQueue.addTask(new Task("Task 8", 200, 5, new byte[1000]));
    taskQueue.addTask(new Task("Task 10", 200, 0, new byte[1000]));

    // taskQueue.addTask(new Task("Task 10", 200, 0, new byte[1000]));

    // Example of processing tasks from the queue in a separate thread

    for (int i = 0; i < 3; i++) {
      Thread poolThread = new Thread(() -> {
        try {
          while (true) {
            Task task = taskQueue.getTask();

            if (task != null) {
              task.run();

              taskQueue.reduceMemory(task.getMemory());
            }
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          System.out.println("INTERROMPIDA!!");
        }
      });
      poolThread.start();
    }
  }

  public void shutdown() {}
}