import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

public class InterfaceUser{


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