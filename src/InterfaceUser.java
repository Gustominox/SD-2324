import java.io.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class InterfaceUser {

  private Client c;
  Scanner scanner;

  public InterfaceUser() {
    c = new Client();
    scanner = new Scanner(System.in);
  }

  public String buildMenu1() {
    StringBuilder menu1 = new StringBuilder();
    menu1.append("--  LOGIN MENU:  --\n");
    menu1.append("1. Login\n");
    menu1.append("2. Register\n");
    menu1.append("3. Exit\n");
    menu1.append("Escolha a sua opção: ");

    return menu1.toString();
  }

  public void print(String input) {
    System.out.println(input);
  }

  public void menuInicial() throws IOException {
    boolean terminado = false;

    while (!terminado) {
      String menu = buildMenu1();
      print(menu);

      int opcao = scanner.nextInt();
      scanner.nextLine();
      String username;
      String password;
      switch (opcao) {
        case 1:
          print("--LOGIN--\n");
          print("Introduza Username: \n");
          username = scanner.nextLine();

          print("Introduza Password: \n");
          password = scanner.nextLine();

          if (c.login(username, password)) {
            menuPrincipal();
          } else {
            print("Credencias não correspondem, tente novamente!\n");
          }
          break;
        case 2:
          print("--REGISTER--\n");
          print("Introduza Username: \n");
          username = scanner.nextLine();

          print("Introduza Password: \n");
          password = scanner.nextLine();

          if (c.registo(username, password)) {
            menuPrincipal();
          } else {
            print("Registo sem Sucesso, tente novamente!\n");
          }
          break;
        case 3:
          print("Terminar Aplicação!");
          terminado = true;
          break;
        default:
          print("Escolha Inválida. Introduza opção novamente: ");
      }
    }
  }

  public String buildPrincipal() {
    StringBuilder menu2 = new StringBuilder();
    menu2.append("--  MAIN MENU:  --");
    menu2.append("1. Pedir Execução de Um Trabalho\n");
    menu2.append("2. Consulta de Resultados\n");
    menu2.append("3. Guardar Resultados num Ficheiro\n");
    menu2.append("4. Consulta do Estado do Servidor\n");
    menu2.append("5. Exit\n");
    menu2.append("Escolha a sua opção: ");
    return menu2.toString();
  }

  public void menuPrincipal() {
    Scanner scanner = new Scanner(System.in);
    boolean terminado = false;
    while (!terminado) {
      String menu = buildPrincipal();
      print(menu);

      int opcao = scanner.nextInt();

      scanner.nextLine();

      switch (opcao) {
        case 1:
          c.pedido(task, tam, code);
          break;
        case 2:
          break;
        case 3:
          print("Resultados gravados!\n");
          break;
        case 4:
          c.consulta();

          break;
        case 5:
          print("Terminar Aplicação!");

          terminado = true;
          break;
        default:
          print("Escolha Inválida. Introduza opção novamente: ");
      }
    }
  }

  /*  public byte[] buscarTarefas(String filePath) {
        List<byte[]> result = new ArrayList<>();
        try (DataInputStream input = new DataInputStream(new FileInputStream(filePath))){
            String linha;
            while (linha = input.readLine() != null){
                String[] campo = linha.split(";");
                byte[] tercampo = campo[2].getBytes();
                result.add(tercampo);
            }
        }
        return result.toArray(new byte[0]);
    }

    //recebe resultado em bytes --> criar ficheiro com resultados

    public void escreverResultadosTarefa(byte[][] resBytes, String filePath){
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(filePath))){
            for (byte[] linha : resBytes){
                output.write(linha);
                output.write('\n');
            }
        }
    }
*/

  public static void main(String[] args) throws IOException {
    InterfaceUser i = new InterfaceUser();

    i.menuInicial();
  }
}
