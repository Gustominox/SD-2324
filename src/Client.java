import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;

class Client {
  
  private static volatile boolean running = true;
  private Socket socket; // = new Socket("Legion", 9090);
  private SocketsManager sManager;
  private String ServerStatus;
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
  public void start() throws IOException {
    Thread receiveThread = new Thread(() -> receive(sManager));
    receiveThread.start();

  }


  public Boolean login(String username, String password) throws IOException {
    sManager.sendLogin(username, password);
    return sManager.recLogin();
  }

  public Boolean registo(String username, String password) throws IOException {
    sManager.sendRegist(username, password);
    return sManager.recRegist();
  }
 
  public void pedido(String task, int tam, byte[] code) throws IOException {
    sManager.sendPedido(task, tam, code);
  }

  public void consulta() throws IOException {
    sManager.sendConsulta();
  }

  public void quit() throws IOException {
    sManager.sendQuit();
  }


  public void receive() throws IOException{
    char type = sManager.readChar();
    if (type == 'x') { //resposta dum pedido

    }
    else if (type == 'w') { //resposta duma consulta
      

    }
  }




  public static void main(String[] args) throws IOException {
    Client c = new Client();
    c.pedido("ola", 15, new byte[15]);
    c.login();
  }
}
