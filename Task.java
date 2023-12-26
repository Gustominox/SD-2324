public class Task implements Runnable {

    private String name;
    private int memory;

    public Task(String name, int memory) {
        this.name = name;
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    @Override
    public void run() {
        System.out.println("Start Task '" + this.name + "' using " + this.memory + " bytes");
        try {
            // Sleep for 5000 milliseconds (5 seconds)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Handle interrupted exception if needed
            e.printStackTrace();
        }
        System.out.println("Finished Task '" + this.name + "' using " + this.memory + " bytes");

    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", memory=" + memory +
                '}';
    }
}
