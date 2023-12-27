import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class SocketsManager {

  private Socket socket;
  
  private BufferedReader br;
  private BufferedWriter rw;

  SocketsManager(Socket socket) {
    this.socket = socket;

    try {
      this.br =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.rw =
        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void send(String msg) throws IOException {
    System.out.println("Sending data");
    this.rw.write(msg);
    this.rw.flush();
  }

  public String recv(int size) throws IOException {
    System.out.println("Receiving data");
    StringBuilder recString = new StringBuilder();

    for (int i = 0; i < size; i++) {
                System.out.println(i);

      int c = br.read();
      if (c != -1) {
        System.out.println((char) c);

        recString.append((char) c);
      } else {
        System.out.println(c);

        return recString.toString();
      }
    }
    return recString.toString();
  }

public void close() throws IOException {
  this.socket.close();
}
}
