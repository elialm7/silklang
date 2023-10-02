

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
import silklang.Linker.PathAlias;
import silklang.Linker.PathResolver;
import silklang.Parser.SilkParser;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.Resolver.Resolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Silk{
    private static final Interpreter interpreter = new Interpreter();
    static boolean haderror = false;
    static boolean hadRuntimeError = false;
    private static PathAlias aliasses = new PathAlias();

    public static Options getOptions(){
        Options interpreterOptions = new Options();
        Option InterpretFile = Option.builder("r").longOpt("run").hasArg(true)
                .argName("Archivo silk").desc("Interprete un archivo con codigo silk.").build();
        Option InterpretOnly = Option.builder("repl").hasArg(false).
                desc("Entra en el REPL[READ - EVAL - PRINT - LOOP]").build();
        Option helpOption = Option.builder("h").longOpt("help")
                .hasArg(false).desc("Muesta la lista de ayuda").build();
        Option versionDetails = Option.builder("v").longOpt("version").
                hasArg(false).desc("Muesta datos de la version y autores.").build();
        Option addAlias = Option.builder("ar").longOpt("addroot").argName("Nombre de la raiz").hasArg(true).desc("Genera un nombre alias para una direccion raiz.").build();
        Option addAliasValue = Option.builder("av").longOpt("addvalue").argName("Ubicacion de la raiz").hasArg(true).desc("Agrega el valor a la raiz.").build();
        Option fromAlias = Option.builder("f").longOpt("from").argName("Nombre del alias").hasArg(true).desc("Empieza buscar desde el alias.").build();
        interpreterOptions.addOption(InterpretFile).addOption(InterpretOnly).addOption(helpOption);
        interpreterOptions.addOption(versionDetails).addOption(addAlias).addOption(addAliasValue).addOption(fromAlias);
        return interpreterOptions;
    }


    public static void main(String[] args) throws IOException{
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        Options interpreterOptions = getOptions();
        try{
            //this code is smelly, but now too lazy to refactor, if the proof of concept works, then i'll spend time to refactor.
            if(args.length == 0) throw new ParseException("Se debe pasar almenos una opcion. ");
            cmd = parser.parse(interpreterOptions, args);
            if(cmd.hasOption("r")){
                String silkFile = cmd.getOptionValue("r");
                if(!cmd.hasOption("f")){
                    throw new ParseException("El comando -r|--run necesita del comando 'from' para correr el programa.");
                }
                String alias = cmd.getOptionValue("f");
                runFile(silkFile, alias);
            }else if(cmd.hasOption("repl")){
                repl();
            } else if(cmd.hasOption("ar")){
                 String aliasname = cmd.getOptionValue("ar");
                 if(!cmd.hasOption("av")){
                     throw new IOException("El comando addroot necesaita de addvalue para poder agregar");
                 }
                 String aliasValue= cmd.getOptionValue("av");
                 addnewPair(aliasname, aliasValue);
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

    private static void addnewPair(String alias, String value){
        aliasses.add(alias, value);
    }

    private static void runMakeFile(String source){
     /*   try {
            var linker = new SilkLinker(Paths.get(System.getProperty("user.dir"), source).toUri());
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

      */
    }

    private static void runFile(String path, String alias){
        try {
            if(!aliasses.exist(alias)) {
                System.out.println("El alias  dado no existe. {"+alias+"}");
                System.exit(1);
            }
            Path pathassigned = aliasses.getPath(alias);
            Path finalPath = PathResolver.resolvePath(pathassigned.toString(), path);
            if(finalPath == null){
                System.out.println("No se pudo encontrar el archivo: {"+"} "+"en el path: {"+pathassigned.toString()+"}");
                System.exit(0);
            }
            byte[] bytes = Files.readAllBytes(finalPath);
            run(new String(bytes, Charset.defaultCharset()));
            if (haderror)
                System.exit(65);
            if (hadRuntimeError)
                System.exit(70);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
    private static void repl() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("Bienvenido a silki, Interprete linea a linea del lenguaje silk.");
        System.out.println("Para salir escriba 'exit(arg), tal que arg sea nada o algun valor de tipo NUMBER.'");
        System.out.println("Copyright (c) under GPL V3.");
        String line = "";
        while (true) {
            System.out.print(">> ");
            line = reader.readLine();
            run(line);
            haderror = false;
        }
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
        }else {
            report(token.getLine(), " en  '" + token.getLexeme() + "'", message);
        }
    }
    public static void error(String where, String message){
        report(where, message);
    }
    private static void report(String where, String message){
        System.err.println("Error: " + where + " : "+message);
        haderror = true;
    }
    private static void report(int line, String where, String message){
        System.err.println("[Linea " + line + "] error " + where + ": " + message);
        haderror = true;
    }
    public static void runtimeError(RuntimeError error){

        if(error.getToken() == null){
            System.err.println(error.getMessage());
        }else {
            System.err.println(error.getMessage() +
                    "\n[Linea " + error.getToken().getLine() + " ]");
        }
            hadRuntimeError = true;
    }

}











