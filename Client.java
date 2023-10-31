import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {

  private Socket socket; // = new Socket("Legion", 9090);
  private Interface i = new Interface();

  public Client() throws IOException {
    socket = new Socket("localhost", 9090);
    BufferedWriter rw = new BufferedWriter(
      new OutputStreamWriter(socket.getOutputStream())
    );
    rw.write("Hello, world!\n");
    rw.flush();

    System.out.println(socket.getPort());
  }

  /**
   * handles login logic, try's a username and a password
   * only finishes if login is successful.
   *
   * @throws IOException
   */
  public void login() throws IOException {}
  
  /**
   * 
   * @param msg
   */
  public void send(String msg) {}

  /**
   * 
   * @param size
   */
  public void recv(int size) {}

  public static void main(String[] args) throws IOException {
    Client c = new Client();
  }
}
