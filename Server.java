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

    while (true) {
      Socket clientSoc = socket.accept();
      System.out.println("Accepted connection");

      ClientHandler client = new ClientHandler(clientSoc);

      new Thread(client).start();
    }
  }

  public static class ClientHandler implements Runnable {

    private final Socket client;
    private BufferedReader br;

    //constructor
    public ClientHandler(Socket socket) {
      this.client = socket;
      try {
        this.br =
          new BufferedReader(new InputStreamReader(client.getInputStream()));
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }

    public void run() {
      try {
        // DataOutputStream dOutStream = new DataOutputStream(clientSoc.getOutputStream());

        String recString = br.readLine();
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
  }
}
