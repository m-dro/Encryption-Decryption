package encryptdecrypt;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String mode = "enc";
        String data = "";
        String in = "";
        String out = "";
        String input = "";
        int key = 0;

        for(int i = 0; i < args.length; i++){
            switch(args[i]){
                case "-mode":
                    mode = args[i+1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i+1]);
                    break;
                case "-data":
                    data = args[i+1];
                    break;
                case "-in":
                    in = args[i+1];
                    break;
                case "-out":
                    out = args[i+1];
                    break;
            }
        }

        StringBuilder output = new StringBuilder();

        if (!data.equals("")) {
            input = data;
        } else if (!in.equals("")) {
            try {
                input = readFileAsString(in);
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found");;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Error: invalid input");
        }

        if (mode.toLowerCase().equals("enc"))
            output = encrypt(input, key, output);
        if (mode.toLowerCase().equals("dec"))
            output = decrypt(input, key, output);

        if (!out.equals("")) {
            try (PrintWriter writer = new PrintWriter(out)){
                writer.println(output.toString());
            } catch (IOException e){
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println(output.toString());
        }


    }

    public static StringBuilder encrypt(String input, int key, StringBuilder output){
        char c;
        int ascii;
        for(int i = 0; i < input.length(); i++){
            c = input.charAt(i);
            ascii = c;
            ascii += key;
            c = (char) ascii;

            output.append(c);
        }
        return output;
    }

    public static StringBuilder decrypt(String input, int key, StringBuilder output){
        char c;
        int ascii;
        for(int i = 0; i < input.length(); i++){
            c = input.charAt(i);
            ascii = c;
            ascii -= key;
            c = (char) ascii;

            output.append(c);
        }
        return output;
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
