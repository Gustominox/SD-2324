import java.util.concurrent.TimeUnit;

public class ThreadPool {

  private final TaskQueue taskQueue;
  private final WorkerThread[] workers;

  public ThreadPool(int numThreads) {
    this.taskQueue = new TaskQueue(2000);
    this.workers = new WorkerThread[numThreads];

    for (int i = 0; i < numThreads; i++) {
      workers[i] = new WorkerThread();
      
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

  private class WorkerThread extends Thread {
    
    @Override
    public void run() {
      try {
        while (true) {
          Task task = taskQueue.getTask();
          if (task != null) {
            task.run();
            // task.output
            // task.id
            // insere na lista de resposta (Completed tasks) 
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
