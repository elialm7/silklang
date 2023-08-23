package shdlang.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shdlang.lexer.ShdLexer;
import shdlang.lexer.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class shd {

    static boolean haderror = false;
    public static void main(String[] args) throws IOException{
        if(args.length > 1){
            System.out.println("Usage: shd [Script] ");
            System.exit(64);
        }
        if(args.length == 1 ){
            runFile(args[0]);
        }else{
            runPromt();
        }
    }
    static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(haderror)
            System.exit(65);
    }
    static void runPromt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String line = "";
        do{
            System.out.print("> ");
             line = reader.readLine();
            if(line.equalsIgnoreCase("exit")){
                break;
            }
            run(line);
            haderror = false;
        }while(!line.equalsIgnoreCase("exit"));
    }

    private static void run(String source){
        ShdLexer lexer = new ShdLexer(source);
        List<Token> tokens = lexer.tokenize();
        for(Token token : tokens){
            System.out.println(token);
        }
    }

    public static void error(int line, String message){
        report(line, "", message);

    }

    private static void report(int line, String where, String message){
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        haderror = true;
    }
}











