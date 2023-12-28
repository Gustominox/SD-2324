import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

  private ServerSocket socket;
  private final Map<String, Utilizador> user;
  private final ReentrantReadWriteLock mapLock;

  public Server() throws IOException {
    socket = new ServerSocket(9090);
    user = new HashMap<>();
    mapLock = new ReentrantReadWriteLock();
  }

  public void start() throws IOException {
    while (true) {
      Socket clientSoc = socket.accept();
      System.out.println("Accepted connection");

      ClientHandler client = new ClientHandler(clientSoc);

      new Thread(client).start();
    }
  }

  public Boolean regist(String username, String password) throws IOException {
    Boolean result = true;

    mapLock.readLock().lock();
    //verifcar se o username ja existe
    if (user.containsKey(username)) {
      System.out.println("o username ja existe\n");
      result = false;
      mapLock.readLock().unlock();
    }
    //senao existir criar
    else {
      mapLock.readLock().unlock();
      Utilizador newUser = new Utilizador(username, password);
      mapLock.writeLock().lock();
      user.put(username, newUser);
      mapLock.writeLock().unlock();
    }
    return result;
  }

  public Boolean login(String username, String password) throws IOException {
    Boolean result = false;

    mapLock.readLock().lock();
    //verifica se o user name existe
    if (user.containsKey(username)) {
      //verifica se a password esta correta
      if (user.get(username).getPassword().equals(password)) {
        //verifica se o user ja nao esta online
        if (user.get(username).getStatus().equals(false)) {
          result = true;
        } else System.out.println("o user já está ativo");
      } else System.out.println("password errada\n");
    } else System.out.println("o user nao existe\n");

    mapLock.readLock().unlock();
    return result;
  }

  private class ClientHandler implements Runnable {

    // private final Socket client;
    private SocketsManager sManager;

    //constructor
    public ClientHandler(Socket socket) {
      // this.client = socket;
      this.sManager = new SocketsManager(socket);
    }

    public void run() {
      try {

        logica do servidor 
            while(!sManager.isClosed()) {// le socket
              
              Message message = sManager.getMessage();

              // Logica de diferenciar a mensagem
              if (message.getType() == tryLogging ) {
                
              } else if (message.getType() == registo ) {
                
              }else{
                System.err.println("Mensagem não reconhecida");
              }

            }
            
        }
      
      
      
      } catch (IOException e) {} finally {
        try {
          this.sManager.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    public Boolean tryLogin() throws IOException {
      Boolean result = false;

      String username = sManager.getMessage();
      String password = sManager.getMessage();

      result = login(username, password);

      return result;
    }
  }
}

  public static void main(String[] args) throws IOException {
    Server s = new Server();
    s.start();
  }

}
