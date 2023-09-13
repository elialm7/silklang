

/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.App;

import silklang.Error.RuntimeError;
import silklang.Expressions.base.Expr;
import silklang.Ast.AstPrinter;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.SilkLexer;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;
import silklang.Parser.SilkParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class silk {

    private static Interpreter interpreter = new Interpreter();
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
    static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(haderror)
            System.exit(65);
        if(hadRuntimeError)
            System.exit(70);
    }
    static void runPromt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("Bienvenido a silk interpreter version Alpha.[No ANTLR] ");
        System.out.println("Esta es una version de funcionamiento. El Interprete aun no esta terminado. ");
        System.out.println("Funcionalidad disponibles: Operaciones aritmeticas y logicas basicas. Para salir escriba 'exit' ");
        System.out.println("Developer: R. Elias Ojeda Almada.");
        System.out.println("Copyright (c) under GPL V3. ");
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
        Expr expression = parser.parse();
        if(haderror) return;
        interpreter.interpret(expression);
    }

    public static void error(int line, String message){
        report(line, "", message);
    }

    public static void error(Token token, String message){

        if(token.getType()== TokenType.EOF){
            report(token.getLine(), " al final ", message);
        }else{
            report(token.getLine(), "en  '"+token.getLexeme() +"'", message );
        }
    }
    private static void report(int line, String where, String message){
        System.err.println("[linea " + line + "] error" + where + ": " + message);
        haderror = true;
    }

    public static void runtimeError(RuntimeError error){
            System.err.println(error.getMessage() +
                    "\n[linea " + error.getToken().getLine() + "]");
            hadRuntimeError = true;
    }
}











