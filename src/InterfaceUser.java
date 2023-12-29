import java.io.*;
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

  /**
   * Método que apresenta no ecrã uma mensagem para informar o utilzador que deve fazer press Enter
   */
  public void pressEnter() {
    System.out.println("Press Enter to Continue");
  }

  /**
   * Método que limpa o ecrã
   */
  public void clearScreen() {
    System.out.println("\033[H\033[2J");
  }

  public void print(String input) {
    System.out.print(input);
  }

  public void menuInicial() throws IOException {
    boolean terminado = false;

    while (!terminado) {
      String menu = buildMenu1();
      clearScreen();
      print(menu);

      int opcao = scanner.nextInt();
      scanner.nextLine();
      String username;
      String password;
      switch (opcao) {
        case 1:
          clearScreen();
          print("--LOGIN--\n");
          print("Introduza Username: ");
          username = scanner.nextLine();

          print("Introduza Password: ");
          password = scanner.nextLine();

          if (c.login(username, password)) {
            terminado = menuPrincipal();
          } else {
            print("Credencias não correspondem, tente novamente!\n");
          }
          break;
        case 2:
          clearScreen();
          print("--REGISTER--\n");
          print("Introduza Username: ");
          username = scanner.nextLine();

          print("Introduza Password: ");
          password = scanner.nextLine();

          if (c.registo(username, password)) {
            terminado = menuPrincipal();
          } else {
            print("Registo sem Sucesso, tente novamente!\n");
          }
          break;
        case 3:
          print("Terminar Aplicação!");
          //   c.close();
          terminado = true;
          break;
        default:
          print("Escolha Inválida. Introduza opção novamente: ");
      }
    }
  }

  public String buildPrincipal() {
    StringBuilder menu2 = new StringBuilder();
    menu2.append("--  MAIN MENU:  --\n");
    menu2.append("1. Pedir Execução de Um Trabalho\n");
    menu2.append("2. Consulta de Resultados\n");
    menu2.append("3. Guardar Resultados num Ficheiro\n");
    menu2.append("4. Consulta do Estado do Servidor\n");
    menu2.append("5. Exit\n");
    menu2.append("Escolha a sua opção: ");
    return menu2.toString();
  }

  public Boolean menuPrincipal() {
    boolean terminado = false;

    try {
      c.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    while (!terminado) {
      String menu = buildPrincipal();
      clearScreen();
      print(menu);

      int opcao = scanner.nextInt();

      scanner.nextLine();

      switch (opcao) {
        case 1:
          print("Insira o ficheiro de Tasks a processar: ");
          String filePath = scanner.nextLine();

          try (
            BufferedReader reader = new BufferedReader(new FileReader(filePath))
          ) {
            String line;
            while ((line = reader.readLine()) != null) {
              // Process the line as needed
              c.executePedido(line);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }

          break;
        case 2:
          String output = c.consultarMap();
          print("RESULTADOS: \n");
          print(output);
          pressEnter();
          scanner.nextLine();

          break;
        case 3:
          print("Insira o nome do ficheiro onde guardar os resultados: ");
          String filePath2 = scanner.nextLine();
          String outputMap = c.consultarMap();

          try (
            BufferedWriter writer = new BufferedWriter(
              new FileWriter(filePath2)
            )
          ) {
            writer.write(outputMap);
          } catch (IOException e) {
            e.printStackTrace();
          }

          print("Resultados gravados!\n");
          break;
        case 4:
          c.consultaServidor();
          print(c.getServerStatus());
          pressEnter();
          scanner.nextLine();

          break;
        case 5:
          print("Terminar Aplicação!!\n");
          terminado = true;
          break;
        default:
          print("Escolha Inválida. Introduza opção novamente: ");
      }
    }
    return terminado;
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
