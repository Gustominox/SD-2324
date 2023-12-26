
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {
    private Queue<Runnable> taskQueue;
    
    private Lock lock;
    private Condition notEmpty;

    public TaskQueue() {

        this.taskQueue = new LinkedList<>();

        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
    }

    public void addTask(Runnable task) {
        lock.lock();
        try {
            taskQueue.add(task);
            notEmpty.signal(); // Notify any waiting threads that a task is available
        } finally {
            lock.unlock();
        }
    }

    public Runnable getTask() throws InterruptedException {
        lock.lock();
        try {
            while (taskQueue.isEmpty()) {
                notEmpty.await(); // Wait until a task is available
            }
            return taskQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        TaskQueue taskQueue = new TaskQueue();

        // Example of adding tasks to the queue
        taskQueue.addTask(() -> System.out.println("Task 1"));
        taskQueue.addTask(() -> System.out.println("Task 2"));

        // Example of processing tasks from the queue in a separate thread
        Thread workerThread = new Thread(() -> {
            try {
                while (true) {
                    Runnable task = taskQueue.getTask();
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        workerThread.start();
    }
}
