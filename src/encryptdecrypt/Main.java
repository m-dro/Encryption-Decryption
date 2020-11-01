package encryptdecrypt;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Course: JetBrains Academy, Java Developer Track
 * Project: Encryption-Decryption
 * Purpose: A console-based program to encrypt and/or decrypt text using simple shifting or unicode-based algorithms.
 *          When starting the program, the necessary algorithm should be specified by an argument (-alg).
 *          The first algorithm is named 'shift', the second one is named 'unicode'.
 *          If there is no -alg, the program defaults to 'shift'.
 *          The program parses five arguments: -mode, -key, -data, -in, and -out  (can be passed in any order).
 *              -mode determines the program’s mode (enc for encryption, dec for decryption)
 *              -key is an integer key to modify the message
 *              -data is a text or ciphertext to encrypt or decrypt
 *              -in specifies the full name of a file to read data
 *              -out specifies the full name of a file to write the result
 *          If there is no -mode, the program works in enc mode.
 *          If there is no -key, the program assumes that key = 0.
 *          If there is no -data, the program assumes that the data is an empty string.
 *
 *          Example argument list:
 *          -mode enc -key 5 -data "This is your text" -alg unicode // this is for encryption
 *          -key 5 -alg unicode -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec // this is for decryption
 *          -mode enc -in road_to_treasure.txt -out protected.txt -key 5 // this uses files
 *
 * @author Mirek Drozd
 * @version 1.1
 */
class AlgorithmSelector {
    private Algorithm algorithm;

    public void setAlgorithm(Algorithm algorithm){
        this.algorithm = algorithm;
    }

    public StringBuilder encrypt(String input, int key, StringBuilder output) {
        return this.algorithm.encrypt(input, key, output);
    }
    public StringBuilder decrypt(String input, int key, StringBuilder output) {
        return this.algorithm.decrypt(input, key, output);
    }
}

interface Algorithm {
    StringBuilder encrypt(String input, int key, StringBuilder output);
    StringBuilder decrypt(String input, int key, StringBuilder output);
}

/**
 * Encryption/Decryption algorithm based on shifting characters
 * by specific number of positions down the alphabet.
 * (NOTE: it DOES NOT modify non-letter characters!)
 *
 * This algorithm is also known as the Caesar Cipher.
 */
class ShiftAlgorithm implements Algorithm {

    /**
     * Encrypts input text by replacing each character by a letter
     * some fixed number of positions down the alphabet.
     *
     * @param input Text to be encrypted.
     * @param key Number of positions by which to shift each character.
     * @param output The encrypted text (in a StringBuilder).
     * @return StringBuilder reference with result of encryption.
     */
    @Override
    public StringBuilder encrypt(String input, int key, StringBuilder output) {
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result;
    }

    /**
     * Decrypts a previously encrypted text by shifting back each character by a specific number
     * of characters (key).
     *
     * @param input The encrypted message.
     * @param key Number of positions by which to shift each character.
     * @param output The decrypted text (in a StringBuilder).
     * @return StringBuilder reference with result of decryption.
     */
    @Override
    public StringBuilder decrypt(String input, int key, StringBuilder output) {
        return encrypt(input, 26 - (key % 26), output);
    }
}

/**
 * Encryption/Decryption algorithm based on ASCII table.
 * (each character is represented as an integer)
 */
class UnicodeAlgorithm implements Algorithm {

    /**
     * Encrypts input text by replacing each character by adding
     * the key to the int representation of the character (using ASCII table).
     *
     * @param input Text to be encrypted.
     * @param key Number of positions by which to shift each character.
     * @param output The encrypted text (in a StringBuilder).
     * @return StringBuilder reference with result of encryption.
     */
    @Override
    public StringBuilder encrypt(String input, int key, StringBuilder output) {
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

    /**
     * Decrypts a previously encrypted text by shifting back each character by a specific number
     * of characters (key) in the ASCII table.
     *
     * @param input The encrypted message.
     * @param key Number of positions by which to shift each character.
     * @param output The decrypted text (in a StringBuilder).
     * @return StringBuilder reference with result of decryption.
     */
    @Override
    public StringBuilder decrypt(String input, int key, StringBuilder output) {
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
}

public class Main {

    /**
     * The main method begins execution of the program.
     *
     * @param args Command-line arguments which control the program.
     */
    public static void main(String[] args) {

        String mode = "enc";
        String data = "";
        String in = "";
        String out = "";
        String input = "";
        String algorithm = "shift";
        int key = 0;

        for(int i = 0; i < args.length; i++){ // reading command-line arguments to determine program flow
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
                case "-alg":
                    algorithm = args[i+1];
                    break;

            }
        }

        StringBuilder output = new StringBuilder();

        if (!data.equals("")) { // reading input
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

        AlgorithmSelector selector = new AlgorithmSelector(); // selecting algorithm (shift or unicode)
        if(algorithm.equals("shift")){
            selector.setAlgorithm(new ShiftAlgorithm());
        } else if (algorithm.equals("unicode")) {
            selector.setAlgorithm(new UnicodeAlgorithm());
        }


        if (mode.toLowerCase().equals("enc")) // selecting mode (encryption or decryption)
            output = selector.encrypt(input, key, output); // encrypting
        if (mode.toLowerCase().equals("dec"))
            output = selector.decrypt(input, key, output); // decrypting

        if (!out.equals("")) { // producing final result (to file or to standard output)
            try (PrintWriter writer = new PrintWriter(out)){
                writer.println(output.toString());
            } catch (IOException e){
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println(output.toString());
        }

    }

    /**
     * Reads text from file and converts it to String.
     *
     * @param fileName Name of the file.
     * @return String representation of the text in the file.
     * @throws IOException if there is problem accessing or reading file.
     */
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }


}
