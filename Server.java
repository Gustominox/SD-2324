import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    private ServerSocket socket;

    public Server() throws IOException {

        socket = new ServerSocket(9090);
        Socket clientSoc = socket.accept();
        System.out.println("Accepted connection");
        BufferedReader br = new BufferedReader(
        new InputStreamReader(clientSoc.getInputStream())
        );

        // DataOutputStream dOutStream = new DataOutputStream(clientSoc.getOutputStream());

        String recString = br.readLine();
        System.out.println(recString);
    }

    public void run() {
        while (true) {
            
        }
    }



    public static void main(String[] args) throws IOException {
        Server s = new Server();
    }

}
