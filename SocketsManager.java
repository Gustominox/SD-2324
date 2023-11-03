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
    this.rw.write(msg);
    this.rw.flush();
  }

  public String recv(int size) throws IOException {
    StringBuilder recString = new StringBuilder();

    for (int i = 0; i < size; i++) {
      int c = br.read();
      if (c != -1) recString.append((char) c); else return recString.toString();
    }

    return recString.toString();
  }
}
