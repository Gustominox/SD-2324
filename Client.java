import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


class Client{

    private Socket socket; // = new Socket("Legion", 9090);

    public Client() throws IOException{

        socket = new Socket("localhost", 9090);
        BufferedWriter rw = new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream())
        );
        rw.write("Hello, world!\n");

        System.out.println(socket.getPort());
    }

    public static void main(String[] args) throws IOException{
        
        Client c = new Client();
        

    }
 
    

}