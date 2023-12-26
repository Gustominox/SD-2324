import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {

  private Socket socket; // = new Socket("Legion", 9090);
  private SocketsManager sManager;
  private Interface i = new Interface();

  public Client() throws IOException {

    socket = new Socket("localhost", 9090);
    sManager = new SocketsManager(socket);

    System.out.println(socket.getPort());
  }

  /**
   * handles login logic, try's a username and a password
   * only finishes if login is successful.
   *
   * @throws IOException
   */
  public void login() throws IOException {
    String username = i.readline();
    System.out.println("Sending username");
    sManager.send(username);
    System.out.println("Wating for response");

    i.print(sManager.recv(12));
    

    String password = i.readline();
    sManager.send(password);
    i.print(sManager.recv(12));




  }


  public static void main(String[] args) throws IOException {
    Client c = new Client();
    c.login();
  }
}
