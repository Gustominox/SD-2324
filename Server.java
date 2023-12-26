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
  private Map<String, Utilizador> user;
  private ReentrantReadWriteLock maplock;

  public Server() throws IOException {
    socket = new ServerSocket(9090);
    user = new HashMap<>();
    maplock = new ReentrantReadWriteLock();
  }

  public void start() throws IOException {
    while (true) {
      Socket clientSoc = socket.accept();
      System.out.println("Accepted connection");

      ClientHandler client = new ClientHandler(clientSoc);

      new Thread(client).start();
    }
  }

  public Boolean regist (String username, String password) throws IOException {
    Boolean result = true;
    
    maplock.readLock().lock();
    //verifcar se o username ja existe
    if(user.containsKey(username)){
      System.out.println("o username ja existe\n");
      result = false;
      maplock.readLock().unlock();
    }
    //senao existir criar
    else{
      maplock.readLock().unlock();
      Utilizador newUser = new Utilizador(username, password);
      maplock.writeLock().lock();
      user.put(username, newUser);
      maplock.writeLock().unlock();
    }
    return result;
  }

  public Boolean login (String username, String password) throws IOException {
    Boolean result = false;

    maplock.readLock().lock();
    //verifica se o user name existe
    if(user.containsKey(username)){
      //verifica se a passeword esta correta
      if(user.get(username).getPassword().equals(password)){
        //verifica se o user ja nao esta online
        if(user.get(username).getStatus().equals(false)){
          result = true;
        } else System.out.println("o user já está ativo");
      } else System.out.println("password errada\n");
    } else System.out.println("o user nao existe\n");

    maplock.readLock().unlock();
    return result;
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
