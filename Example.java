import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sd23.*;

public class Example {
    public static void main(String[] args) {
         
        try {
            // obter a tarefa de ficheiro, socket, etc...
            Path filePath = Paths.get("out/TaskQueue.class"); 

            byte[] job = Files.readAllBytes(filePath);
            
            System.out.println(job.length);


            // executar a tarefa
            byte[] output = JobFunction.execute(job);

            // utilizar o resultado ou reportar o erro
            System.err.println("success, returned "+output.length+" bytes");
        } catch (JobFunctionException e) {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
        } catch (Exception e) {
            System.err.println("error reading file: " + e);
        }
    }
}