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
        this.tryLogin();
      } catch (IOException e) {} finally {
        try {
          this.client.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    private void tryLogin() throws IOException {
      System.out.println("Getting user");
      String username = sManager.recv(1024);
      System.out.println(username);

      if (
        username == "Default"
      ) { // TODO: check if user exists
        sManager.send("Correct User");
        String password = sManager.recv(1024);

        if (
          password == "password"
        ) { // TODO: check if password exists
          sManager.send("Correct Password");
        }
      }else sManager.send("Wrong User");

    }
  }

  public static void main(String[] args) throws IOException {
    Server s = new Server();
    s.start();
  }
}
