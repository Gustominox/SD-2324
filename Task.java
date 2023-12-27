import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import sd23.*;

public class Task implements Runnable, Comparable<Task> {

    private String name;
    private int memory;
    private int priority; // Added priority field
    private byte[] bytes;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getMemory() {
        return memory;
    }


    public void setMemory(int memory) {
        this.memory = memory;
    }


    public byte[] getBytes() {
        return bytes;
    }


    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }


    public Task(String name, int memory, int priority, byte[] bytes) {
        this.name = name;
        this.memory = memory;
        this.priority = priority;
        this.bytes = bytes;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        System.out.println(
                "Start Task '" + this.name + "' using " + this.memory + " bytes " + this.priority + " priority" + " -> " + Thread.currentThread().getName()
        );
        try {
            // execute the task
            byte[] output = JobFunction.execute(this.bytes);

            // use the result or report the error
            // System.err.println("success, returned " + output.length + " bytes");

        } catch (JobFunctionException e) {

            System.err.println(
                    // "job failed: code=" + e.getCode() + " message=" + e.getMessage()
            );

        } catch (Exception e) {

            System.err.println("error reading file: " + e);

        }

        // System.out.println(
        //         "Finished Task '" + this.name + "' using " + this.memory + " bytes"
        // );
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", memory=" + memory +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }


    public void increasePriority() {
        this.priority += 1;
    }
}
