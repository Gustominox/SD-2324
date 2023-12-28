import java.util.Scanner;

public class Interface {

  Scanner s = new Scanner(System.in);
  int escolha = 0;

  // do{

  //     System.out.println("1 - Login");
  //     System.out.println("2 - Sair");
  //     escolha = s.nextInt();

  // } while (escolha != 0);

public void loopMenu() {
    Boolean Finished = false;
    while(!Finished){
        printMenu();
        this.readline();

    }
}


  public void printMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("---MENU---\n");
    sb.append("1 - Login\n");
    sb.append("2 - Login\n");

    this.print(sb.toString());
  }

  public String readline() {
    Scanner s = new Scanner(System.in);

    String input = s.next();

    s.close();

    return input;
  }

  public void print(String output) {
    System.out.print(output);
  }

  public void printPrompt(int nivel) {
    this.print("Client > ");
  }

  private static int getEscolha(){
    return 0;
  }
}
