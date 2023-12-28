import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class InterfaceUser{


    Scanner scanner = new Scanner(System.in);
    while (true){
        System.out.println("--  LOGIN MENU:  --");
        System.out.println("1. Login\n");
        System.out.println("2. Register\n");
        System.out.println("3. Exit\n");
        System.out.println("Escolha a sua opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch(opcao){
            case 1:
                System.out.println("Introduza Username: \n");
                String username = scanner.nextLine();
                System.out.println("Introduza Password: \n");


                login();
            case 2:
                regist();
            case 3:
                System.out.println("Obrigado, Volte Sempre!");
            default:
                System.out.println("Escolha Inválida. Introduza opção novamente: ");
        }
    }

    public byte[][] buscarTarefas(String filePath) {
        List<byte[]> result = new ArrayList<>();
        try (DataInputStream input = new DataInputStream(new FileInputStream(filePath))){
            String linha;
            while (linha = input.readLine() != null){
                String[] campo = linha.split(";");
                byte[] tercampo = campo[2].getBytes();
                result.add(tercampo);
            }
        }
        return result.toArray(new byte[0][]);
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


}