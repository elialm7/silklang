

/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

//bom dia
package silklang.App;

import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.SilkLexer;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;
import silklang.Parser.SilkParser;
import silklang.ParserRepresentation.Statement.base.Stmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Silk{
    private static final Interpreter interpreter = new Interpreter();
    static boolean haderror = false;
    static boolean hadRuntimeError = false;
    public static void main(String[] args) throws IOException{
        if(args.length > 1){
            System.out.println("Uso: [script] ");
            System.exit(64);
        }
        if(args.length == 1 ){
            runFile(args[0]);
        }else{
            runPromt();
        }
    }
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(haderror)
            System.exit(65);
        if(hadRuntimeError)
            System.exit(70);
    }
    private static void runPromt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("SilkLang-Interpreter V1.0.0");
        System.out.println("Para salir escriba 'exit'");
        System.out.println("Copyright (c) under GPL V3.");
        String line = "";
        do{
            System.out.print(">> ");
             line = reader.readLine();
            if(line.equalsIgnoreCase("exit")){
                break;
            }
            run(line);
            haderror = false;
        }while(!line.equalsIgnoreCase("exit"));
    }

    private static void run(String source){
        SilkLexer lexer = new SilkLexer(source);
        List<Token> tokens = lexer.tokenize();
        SilkParser parser = new SilkParser(tokens);
        List<Stmt> statements = parser.parse();
        if(haderror) return;
        interpreter.interpret(statements);
    }

    public static void error(int line, String message){
        report(line, "", message);
    }

    public static void error(Token token, String message){

        if(token.getType()== TokenType.EOF){
            report(token.getLine(), " al final ", message);
        }else{
            report(token.getLine(), " en  '"+token.getLexeme() +"'", message );
        }
    }
    private static void report(int line, String where, String message){
        System.err.println("[Linea " + line + "] error" + where + ": " + message);
        haderror = true;
    }

    public static void runtimeError(RuntimeError error){
            System.err.println(error.getMessage() +
                    "\n[Linea " + error.getToken().getLine() + "]");
            hadRuntimeError = true;
    }
}











