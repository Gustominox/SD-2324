import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
  private final ReentrantReadWriteLock mapLock;

  public Client() {
    try {
      socket = new Socket("localhost", 9090);
    } catch (IOException e) {
      e.printStackTrace();
    }
    sManager = new SocketsManager(socket);
    tasksMap = new HashMap<String, String>();
    mapLock = new ReentrantReadWriteLock();

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
    mapLock.writeLock().lock();
    tasksMap.put(task, "A espera de resposta...");
    mapLock.writeLock().unlock();

    sManager.sendPedido(task, tam, code);
  }

  public void consulta() throws IOException {
    sManager.sendConsulta();
  }

  public void quit() throws IOException {
    sManager.sendQuit();
  }

  public String consultarMap() {
    Map<String, String> map = this.tasksMap;
    StringBuilder sb = new StringBuilder();
    // Entry<TaskName , TaskStatus>
    this.mapLock.readLock().lock();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      sb.append("  Task: " + entry.getKey()).append(", Status: ");
      sb.append(entry.getValue()).append("\n");
    }
    this.mapLock.readLock().unlock();
    return sb.toString();
  }

  public void executePedido(String input) throws IOException {
    String task[] = input.split(";");
    String taskName;
    byte code[];
    int size;

    if (task.length != 2) {
      System.out.println("o input nao esta no formato correto");
    } else {
      taskName = task[0];
      code = task[1].getBytes(StandardCharsets.UTF_8);
      size = code.length;

      this.pedido(taskName, size, code);
    }
  }

  public void receive(SocketsManager sManager) throws IOException {
    char type = sManager.readChar();
    if (type == 'x') { //resposta dum pedido
      char type2 = sManager.readChar();
      String taskName = sManager.readString();

      mapLock.writeLock().lock();
      if (type2 == 'S') {
        byte output[] = sManager.readBytes(type);
        try (FileOutputStream fos = new FileOutputStream(taskName)) {
            // Write the byte array to the file
            fos.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskName))) {
          // Write the content to the file
          writer.write("Recebido sem Sucesso, code: " + code + ", msg: " + msg);
      } catch (IOException e) {
          e.printStackTrace();
      }
      }
      mapLock.writeLock().unlock();
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

  public void consultaServidor() {
    this.statusLock.lock();
    try {
      sManager.sendConsulta();
      serverStatusUpdate.await();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.statusLock.unlock();
    }
  }
}
