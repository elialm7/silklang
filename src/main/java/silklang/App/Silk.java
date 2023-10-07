

/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

//bom dia
package silklang.App;

import com.diogonunes.jcolor.Attribute;
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

import static com.diogonunes.jcolor.Ansi.colorize;

public class Silk{
    private static final Interpreter interpreter = new Interpreter();
    static boolean haderror = false;
    static boolean hadRuntimeError = false;
    private static PathAlias aliasses = new PathAlias();

    public static Options getOptions(){
        Options interpreterOptions = new Options();
        Option  listFunctions =  Option.builder("l").longOpt("list").desc(colorize("Lista la funciones nativas y clases nativas que soporta el inteprete.", Attribute.GREEN_TEXT())).hasArg(false).build();
        Option InterpretFile = Option.builder("r").longOpt("run").hasArg(true)
                .argName("Archivo silk").desc(colorize("Interprete un archivo con codigo silk.", Attribute.GREEN_TEXT())).build();
        Option InterpretOnly = Option.builder("repl").hasArg(false).
                desc(colorize("Entra en el REPL[READ - EVAL - PRINT - LOOP]", Attribute.GREEN_TEXT())).build();
        Option helpOption = Option.builder("h").longOpt("help")
                .hasArg(false).desc(colorize("Muesta la lista de ayuda", Attribute.GREEN_TEXT())).build();
        Option versionDetails = Option.builder("v").longOpt("version").
                hasArg(false).desc(colorize("Muesta datos de la version y autores.", Attribute.GREEN_TEXT())).build();
        Option addAlias = Option.builder("ar").longOpt("addroot").argName("Nombre de la raiz").hasArg(true).desc(colorize("Genera un nombre alias para una direccion raiz.", Attribute.GREEN_TEXT())).build();
        Option addAliasValue = Option.builder("av").longOpt("addvalue").argName("Ubicacion de la raiz").hasArg(true).desc(colorize("Agrega el valor a la raiz.", Attribute.GREEN_TEXT())).build();
        Option fromAlias = Option.builder("f").longOpt("from").argName("Nombre del alias").hasArg(true).desc(colorize("Empieza buscar desde el alias.", Attribute.GREEN_TEXT())).build();
        interpreterOptions.addOption(InterpretFile).addOption(InterpretOnly).addOption(helpOption).addOption(listFunctions);
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
                    throw new ParseException("El comando -r |--run necesita del comando 'from' para correr el programa.");
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
                System.out.println(colorize(builder, Attribute.GREEN_TEXT()));
                System.exit(0);
            }else if(cmd.hasOption("h")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Ayuda", interpreterOptions);
                System.exit(0);
            }else if(cmd.hasOption("l")){
                listNatives();
            }
        }catch (ParseException ex){
            System.out.println(colorize("Se produjo un error parseando. "+ex.getMessage(), Attribute.RED_TEXT()));
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("Ayuda", interpreterOptions);
            System.exit(0);
        }
    }

    private static void addnewPair(String alias, String value){
        aliasses.add(alias, value);
    }

    private static void listNatives(){
        NativeList nat = new NativeList();
        nat.addTitle("#FUNCIONES NATIVAS");
        nat.addFunction("clock();","<NUMBER>", "Devuelve el tiempo actual en segundos.");
        nat.addFunction("input(<ANY> mensaje);", "<STRING>", "Imprime el mensaje y luego pide al usuario una entrada.");
        nat.addFunction("inputln(<ANY> mensaje);", "<STRING>", "Imprime el mensaje con salto de linea y luego pide al usuario una entrada.");
        nat.addFunction("type(<ANY> arg);", "<NIL>", "Imprime el tipo de dato del argumento.");
        nat.addFunction("echo(<ANY>arg, *);", "<NIL>","Imprime el/los argumentos pasados.");
        nat.addFunction("isNumber(<ANY> arg);", "<BOOLEAN>", "Verifica que un argumento sea un numero.");
        nat.addFunction("isString(<ANY> arg);", "<BOOLEAN>", "Verifica que un argumento sea un string.");
        nat.addFunction("isBoolean(<ANY> arg);", "<BOOLEAN>", "Verifica que un argumento sea un booleano.");
        nat.addFunction("exit(<NUMBER> arg?);", "<BOOLEAN>", "Termina el programa con un codigo de salida.");
        nat.addFunction("toNumber(<String> arg);", "<NUMBER>", "Convierte el argumento en tipo numero.");
        nat.addFunction("toString(<ANY> arg);", "<STRING>", "Convierte el argumento en tipo string");
        nat.addFunction("toBoolean(<String> arg);", "<BOOLEAN>", "Convierte el argumento en tipo booleano.");
        nat.addFunction("toVector(<ANY> arg, *);", "vector", "Convierte el/los argumentos en un objeto de la clase vector.");
        nat.addTitle("#CLASES NATIVAS");
        nat.addSubTitle("#Math");
        nat.addFunction("math();", "math", "Constructor de la clase math.");
        nat.addFunction(".random(<NUMBER> min, <NUMBER> max);", "<NUMBER>", "Devuelve un numero random entre min y max.");
        nat.addSubTitle("#Vector");
        nat.addFunction("vector(<NUMBER> tam);", "vector", "Constructor de la clase vector. ");
        nat.addFunction(".get(<NUMBER> posicion);", "<ANY>", "Devuelve el objeto almecenado en esa posicion.");
        nat.addFunction(".add(<NUMBER> posicion, <ANY> arg);", "<NIL> ", "Guarda un elemento en la posicion inidicada.");
        nat.addFunction(".size();", "<NUMBER> ", "Devuelve el tamano del array.");
        nat.addSubTitle("#String");
        nat.addFunction("string(<STRING> str);", "string", "Constructor de la clase string.");
        nat.addFunction(".append(<STRING> str);", "<NIL>", "Agrega un string al string original.");
        nat.addFunction(".equals(<STRING> str1, <STRING> str2, <BOOLEAN> flagignore); ", "<NIL>", "verifica que str1 sea igual a str2");
        nat.addFunction(".get();", "<STRING> ", "Devuelve el string.");
        nat.addSubTitle("#File");
        nat.addFunction("file(<STRING> path): ", "file", "Constructor de la clase file.");
        nat.addFunction(".exists();", "<BOOLEAN>", "Verifica que el archivo existe.");
        nat.addFunction(".load(); ", "<NIL>", "Carga el archivo.");
        nat.addFunction(".close();", "<NIL>", "Cierra el archivo. ");
        nat.addFunction(".hasNext();", "<BOOLEAN>", "Verifica que el arhivo tenga una siguiente linea.");
        nat.addFunction(".readLine();","<STRING> ", "Lee la linea del archivo.");
        nat.addFunction(".write(<STRING> str);","<NIL> ", "Escribe la linea en el archivo sin salto de linea.");
        nat.print();
    }
    private static void runFile(String path, String alias){
        try {
            if(!aliasses.exist(alias)) {
                System.out.println(colorize("El alias  dado no existe. {"+alias+"}", Attribute.RED_TEXT()));
                System.exit(1);
            }
            Path pathassigned = aliasses.getPath(alias);
            Path finalPath = PathResolver.resolvePath(pathassigned.toString(), path);
            if(finalPath == null){
                System.out.println(colorize("No se pudo encontrar el archivo: {"+"} "+"en el path: {"+pathassigned.toString()+"}", Attribute.RED_TEXT()));
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
        System.out.println(colorize("Bienvenido a silki, Interprete linea a linea del lenguaje silk.", Attribute.GREEN_TEXT()));
        System.out.println(colorize("Copyright (c) under GPL V3.", Attribute.GREEN_TEXT()));
        String line = "";
        while (true) {
            System.out.print(colorize(">> ", Attribute.GREEN_TEXT()));
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

        System.err.println(colorize("[Linea " + line + "] error " + where + ": " + message, Attribute.RED_TEXT()));
        haderror = true;
    }
    public static void runtimeError(RuntimeError error){

        if(error.getToken() == null){
            System.err.println(colorize(error.getMessage(), Attribute.RED_TEXT()));
        }else {
            System.err.println(colorize(error.getMessage() + "\n[Linea " + error.getToken().getLine() + " ]", Attribute.RED_TEXT()));
        }
            hadRuntimeError = true;
    }


}











