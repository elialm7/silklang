

/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

//bom dia
package silklang.App;

import org.apache.commons.cli.*;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.SilkLexer;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;
import silklang.Linker.SilkLinker;
import silklang.Parser.SilkParser;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.Resolver.Resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Silk{
    private static final Interpreter interpreter = new Interpreter();
    static boolean haderror = false;
    static boolean hadRuntimeError = false;

    public static Options getOptions(){
        Options interpreterOptions = new Options();
        Option InterpretFile = Option.builder("r").longOpt("run").hasArg(true)
                .argName("Archivo silk").desc("Interprete un archivo con codigo silk.").build();
        Option InterpretOnly = Option.builder("repl").hasArg(false).desc("Entra en el REPL[READ - EVAL - PRINT - LOOP]").build();
        Option InterpreteFileDesc = Option.builder("mr").longOpt("mrun")
                .hasArg(true).argName("Archivo descriptor silk").desc("Lee un archivo de descripcion y luego interpreta las dependencias. ").build();
        Option helpOption = Option.builder("h").longOpt("help")
                .hasArg(false).desc("Muesta la lista de ayuda").build();
        Option versionDetails = Option.builder("v").longOpt("version").hasArg(false).desc("Muesta datos de la version y autores.").build();
        interpreterOptions.addOption(InterpretFile).addOption(InterpretOnly).addOption(InterpreteFileDesc).addOption(helpOption);
        interpreterOptions.addOption(versionDetails);
        return interpreterOptions;
    }


    public static void main(String[] args) throws IOException{
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        Options interpreterOptions = getOptions();
        try{
            if(args.length == 0) throw new ParseException("Se debe pasar almenos una opcion. ");
            cmd = parser.parse(interpreterOptions, args);
            if(cmd.hasOption("r")){
                String silkFile = cmd.getOptionValue("r");
                runFile(silkFile);
            }else if(cmd.hasOption("repl")){
                runPromt();
            }else if(cmd.hasOption("mr")){
                String makeFile = cmd.getOptionValue("mr");
                runMakeFile(makeFile);
            }else if(cmd.hasOption("v")){
                String builder = "Version: Unica. " + "\n"+
                        "Autor: R. Elias Ojeda Almada." +"\n"+
                        "Copyright (c) under GPL V3.";
                System.out.println(builder);
                System.exit(0);
            }else if(cmd.hasOption("h")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Ayuda", interpreterOptions);
                System.exit(0);
            }

        }catch (ParseException ex){
            System.out.println("Se produjo un error parseando. "+ex.getMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("Ayuda", interpreterOptions);
            System.exit(0);
        }
    }

    private static void runMakeFile(String source){
        try {
            var linker = new SilkLinker(new File(source).toURI());
            var sourceBuilder = new StringBuilder();
            var filestoLink = linker.getDependencies();
            for (var arch : Collections.unmodifiableList(filestoLink)) {
                if(!arch.exists()){
                    throw new IOException("El archivo "+arch.getName()+ "N no existe, ruta: "+arch.getAbsolutePath());
                }
                var lines = Files.readAllLines(arch.toPath());
                sourceBuilder.append(String.join("", lines));
            }
            run(sourceBuilder.toString());
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    private static void runFile(String path){
        byte[] bytes = new byte[0];
        try {
             bytes = Files.readAllBytes(Paths.get(path));
        }catch (IOException e){
            System.out.println("No se pudo leer "+path+"; El archivo no existe o la ruta es incorrecta. ");
            System.exit(1);
        }
        run(new String(bytes, Charset.defaultCharset()));
        if(haderror)
            System.exit(65);
        if(hadRuntimeError)
            System.exit(70);
    }
    private static void runPromt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("Bienvenido a silki, Interprete linea a linea del lenguaje silk.");
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
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);
        if(haderror)return;
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











