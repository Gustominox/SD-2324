import java.util.Scanner;

public class Interface{
    Scanner s = new Scanner(System.in);
    int escolha = 0;
    do{

        System.out.println("1 - Login");
        System.out.println("2 - Sair");
        escolha = s.nextInt();

    } while (escolha != 0);
}