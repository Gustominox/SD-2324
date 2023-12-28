import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
 flags:
    login = l;
    registo = r;
    pedido de execução = e;
    consulta = c;
    quit = q;
    login response = z;
    register response = y;
    pedido response = x;
    consulta response = w;

 */



class SocketsManager {

  private Socket socket;

  private DataInputStream in;
  private DataOutputStream out;

  SocketsManager(Socket socket) {
    this.socket = socket;

    try {
      this.in =
        new DataInputStream(socket.getInputStream());
      this.out =
        new DataOutputStream(socket.getOutputStream())
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void sendLogin(String user, String password) throws IOException {
    System.out.println("Sending data");
    out.writeChar('l');
    out.writeUTF(user);
    out.writeUTF(password);
    out.flush();
  }


  public void sendLoginResponse(Boolean b, String msg) throws IOException {
    System.out.println("Sending data");
    out.writeChar('z');
    out.writeBoolean(b);
    out.writeUTF(msg);
    out.flush();
  }

  public void sendRegist(String user, String password) throws IOException {
    System.out.println("Sending data");
    out.writeChar('r');
    out.writeUTF(user);
    out.writeUTF(password);
    out.flush();
  }

  public void sendRegistResponse(Boolean b, String msg) throws IOException {
    System.out.println("Sending data");
    out.writeChar('y');
    out.writeBoolean(b);
    out.writeUTF(msg);
    out.flush();
  }

  //verficar
  public void sendPedido(String task, int tam, byte[] code) throws IOException {
    System.out.println("Sending data");
    out.writeChar('p');
    out.writeUTF(task);
    out.writeInt(tam);
    out.write(code);
    out.flush();
  }

  public void sendPedidoResponse(String task, String result) throws IOException {
    System.out.println("Sending data");
    out.writeChar('x');
    out.writeUTF(task);
    out.writeUTF(result);
    out.flush();
  }

  public void sendConsulta() throws IOException {
    System.out.println("Sending data");
    out.writeChar('c');
    out.flush();
  }

  public void sendConsultaResponse(String estado) throws IOException {
    System.out.println("Sending data");
    out.writeChar('w');
    out.writeUTF(estado);
    out.flush();
  }



  public void sendQuit() throws IOException{
    System.out.println("Sending data");
    out.writeChar('q');
    out.flush();
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

  public boolean isClosed() {
    return this.socket.isClosed();
  }
}
