import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class InterfaceUser{

    static Client c = new Client();

    public static void main(String[] args) throws IOException{
        menu1();
    }


    public static void menu1() throws IOException{
        Scanner scanner = new Scanner(System.in);
        while (true) {
            StringBuilder menu1 = new StringBuilder();
            menu1.append("--  LOGIN MENU:  --\n");
            menu1.append("1. Login\n");
            menu1.append("2. Register\n");
            menu1.append("3. Exit\n");
            menu1.append("Escolha a sua opção: ");
            System.out.println(menu1.toString());
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch(opcao){
                case 1:
                    menuLogin();
                    break;
                case 2:
                    menuRegist();
                    break;
                case 3:
                    System.out.println("Obrigado, Volte Sempre!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Escolha Inválida. Introduza opção novamente: ");
            }
        }
    }

    public static void menuLogin() throws IOException{
        Scanner scanner = new Scanner(System.in);
        StringBuilder loginMenu = new StringBuilder();
        loginMenu.append("--LOGIN--\n");
        loginMenu.append("Introduza Username: \n");
        String username = scanner.nextLine();
        loginMenu.append("Introduza Password: \n");
        String password = scanner.nextLine();
        if (c.login(username, password)) menu2();
        else System.out.println("Credencias não correspondem, tente novamente!\n");
    }


    public static void menuRegist() throws IOException{
        Scanner scanner = new Scanner(System.in);
        StringBuilder registMenu = new StringBuilder();
        registMenu.append("--REGISTER--\n");
        registMenu.append("Introduza Username: \n");
        String username = scanner.nextLine();
        registMenu.append("Introduza Password: \n");
        String password = scanner.nextLine();
        if (c.registo(username, password)) menu2();
        else System.out.println("Registo sem Sucesso, tente novamente!\n");
    }



    public static void menu2(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            StringBuilder menu2 = new StringBuilder();
            menu2.append("--  MAIN MENU:  --");
            menu2.append("1. Pedir Execução de Um Trabalho\n");
            menu2.append("2. Consulta de Resultados\n");
            menu2.append("3. Guardar Resultados num Ficheiro\n");
            menu2.append("4. Consulta do Estado do Servidor\n");
            menu2.append("5. Exit\n");
            menu2.append("Escolha a sua opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch(opcao){
                case 1:
                    
                    c.pedido(task,tam,code);
                    break;
                case 2:

                    
                    break;
                case 3:
                    System.out.println("Resultados gravados!\n");
                    break;
                case 4:
                    c.consulta();
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Obrigado, Volte Sempre!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Escolha Inválida. Introduza opção novamente: ");
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
}

