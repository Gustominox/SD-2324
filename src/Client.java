import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Client {

  private static volatile boolean running = true;
  private Socket socket; // = new Socket("Legion", 9090);
  private SocketsManager sManager;
  private String ServerStatus;
  private Lock statusLock = new ReentrantLock();
  private Condition serverStatusUpdate = statusLock.newCondition();

  /*
   * {key: "TASKNAME" : Value: String -> Sem resultado
   *                                  -> Recebido com Sucesso, tmh: 1000 bytes
   *                                  -> Recebido sem Sucesso, code:138 , msg:
   * }
   */
  private final Map<String, String> tasksMap;

  public Client() throws IOException {
    socket = new Socket("localhost", 9090);
    sManager = new SocketsManager(socket);
    tasksMap = new HashMap<String, String>();

    System.out.println(socket.getPort());
  }

  /**
   * handles login logic, try's a username and a password
   * only finishes if login is successful.
   *
   * @throws IOException
   */
  public void start() throws IOException {
    Thread receiveThread = new Thread(() -> {
      try {
        receive(sManager);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
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

  public void receive(SocketsManager sManager) throws IOException {
    char type = sManager.readChar();
    if (type == 'x') { //resposta dum pedido
      char type2 = sManager.readChar();
      String taskName = sManager.readString();

      if (type2 == 'S') {
        byte output[] = sManager.readBytes(type);

        tasksMap.put(
          taskName,
          "Recebido com Sucesso, tmh: " + output.length + " bytes"
        );
        // TODO: write output to file taskName.txt
      } else if (type2 == 'I') {
        int code = sManager.readInt();
        String msg = sManager.readString();
        tasksMap.put(
          taskName,
          "Recebido sem Sucesso, code: " + code + ", msg: " + msg
        );
      }
    } else if (type == 'w') { //resposta duma consulta
      this.ServerStatus = sManager.readString();
      serverStatusUpdate.signal();
    }
  }

  public static void main(String[] args) throws IOException {
    Client c = new Client();
    c.pedido("ola", 15, new byte[15]);
    // c.registo("UserName", "Password");
    // c.login("UserName", "Password");
  }
}
