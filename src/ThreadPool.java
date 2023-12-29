import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {

  private final TaskQueue taskQueue;
  private final WorkerThread[] workers;
  private Lock lCompTasks = new ReentrantLock();
  private final LinkedList<Task> completedTasks = new LinkedList<>();

  public ThreadPool(int numThreads) {
    this.taskQueue = new TaskQueue(2000);
    this.workers = new WorkerThread[numThreads];

    for (int i = 0; i < numThreads; i++) {
      workers[i] = new WorkerThread();
    }
  }

  public Task getCompTask(String username) {
    if (username == null) return null;

    this.lCompTasks.lock();
    try {
      for (Task task : completedTasks) {
        if (task.getUsername() == username) {
          return task;
        }
      }
      return null;
    } finally {
      this.lCompTasks.unlock();
    }
  }

  public void start() {
    for (int i = 0; i < workers.length; i++) {
      workers[i].start();
    }
  }

  public void submitTask(Task task) {
    taskQueue.addTask(task);
  }

  public void shutdown() {
    taskQueue.shutdown();

    for (WorkerThread worker : workers) {
      worker.interrupt();
    }
  }

  public String getEstado() {
    StringBuilder estado = new StringBuilder();

    // Print the state of the task queue
    estado.append("Task Queue State:\n");
    estado.append(taskQueue.getState());
    estado.append("\n");

    // Print the state of each worker thread
    estado.append("Worker Threads State:\n");
    for (int i = 0; i < workers.length; i++) {
      estado.append("Thread ").append(i).append(": ");
      if (workers[i].currentTask != null) {
        estado
          .append("Task ")
          .append(workers[i].currentTask.getName())
          .append(" is being processed\n");
      } else {
        estado.append("Idle\n");
      }
    }
    return estado.toString();
  }

  private class WorkerThread extends Thread {

    Task currentTask = null;

    @Override
    public void run() {
      try {
        while (true) {
          Task task = taskQueue.getTask();
          currentTask = task;
          if (task != null) {
            task.run();
            // task.output
            // task.id
            // insere na lista de resposta (Completed tasks)
            try {
              lCompTasks.lock();
              currentTask = null;
              completedTasks.addLast(task);
            } finally {
              lCompTasks.unlock();
            }
            taskQueue.reduceMemory(task.getMemory());
          }
        }
      } catch (InterruptedException e) {
        // Restore the interrupted status

        Thread.currentThread().interrupt();
      }
    }
  }

  public static void main(String[] args) {
    // Create a thread pool with 3 worker threads
    ThreadPool threadPool = new ThreadPool(5);

    // Submit some tasks to the thread pool// Example of adding tasks to the queue
    threadPool.submitTask(new Task("Task 1", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 2", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 3", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 4", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 5", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 6", 200, 0, new byte[1000]));
    threadPool.submitTask(new Task("Task 7", 200, 5, new byte[1000]));
    threadPool.submitTask(new Task("Task 9", 200, 5, new byte[1000]));
    threadPool.submitTask(new Task("Task 8", 200, 5, new byte[1000]));
    threadPool.submitTask(new Task("Task 10", 200, 5, new byte[1000]));

    // Shutdown the thread pool
    threadPool.start();
    // threadPool.shutdown();
  }
}
