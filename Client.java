import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {

  private Socket socket; // = new Socket("Legion", 9090);
  private BufferedReader br;
  private BufferedWriter rw;
  private Interface i = new Interface();

  public Client() throws IOException {
    socket = new Socket("localhost", 9090);
    this.br =
      new BufferedReader(new InputStreamReader(socket.getInputStream()));

    rw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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

    this.send(username);
  }

  /**
   *
   * @param msg
   * @throws IOException
   */
  public void send(String msg) throws IOException {
    this.rw.write(msg);
    this.rw.flush();
  }

  /**
   *
   * @param size
   */
  public void recv(int size) {}

  public static void main(String[] args) throws IOException {
    Client c = new Client();
    c.send("HELLO WORLD");
  }
}
