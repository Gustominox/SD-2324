import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class Server {

  private ServerSocket socket;
  private Map<String, String> user;

  public Server() throws IOException {
    socket = new ServerSocket(9090);
  }

  public void start() throws IOException {
    while (true) {
      Socket clientSoc = socket.accept();
      System.out.println("Accepted connection");

      ClientHandler client = new ClientHandler(clientSoc);

      new Thread(client).start();
    }
  }

  public static class ClientHandler implements Runnable {

    private final Socket client;
    private SocketsManager sManager;

    //constructor
    public ClientHandler(Socket socket) {
      this.client = socket;
      this.sManager = new SocketsManager(socket);
    
    }

    public void run() {
      try {
        // DataOutputStream dOutStream = new DataOutputStream(clientSoc.getOutputStream());

        String recString = sManager.recv(1024);
        System.out.println(recString);
      } catch (IOException e) {} finally {
        try {
          this.client.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  
  }

  public static void main(String[] args) throws IOException {
    Server s = new Server();
    s.start();
  }
}
