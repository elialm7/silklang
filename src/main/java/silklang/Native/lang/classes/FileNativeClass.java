/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.classes;

import silklang.Callable.SilkCallable;
import silklang.Callable.SilkClass;
import silklang.Callable.SilkInstance;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class FileNativeClass extends SilkInstance {

    private File file;
    private Scanner scanner;
    private PrintWriter writer;
    public FileNativeClass(SilkClass sclass) {
        super(null);
    }
    public FileNativeClass(String arch){
        super(null);
        this.file = new File(arch);
    }

    @Override
    public Object get(Token name) {
        return switch (name.getLexeme()){
          case "exists" -> new ExistsMethod();
          case "load" -> new LoadMethod();
          case "close" -> new CloseMethod();
          case "hasNext" -> new HasNextMethod();
          case "readLine" -> new ReadNextMethod();
          case "write" -> new WriteMethod();
            default -> throw new RuntimeError(name, "Propiedad inexistente");
        };
    }

    @Override
    public void set(Token name, Object value) {
        throw new RuntimeError(name,"No se le puede agregar propiedades a un objeto nativo.");
    }

    private class ExistsMethod implements SilkCallable {
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            return file.exists();
        }
    }
    private class LoadMethod implements SilkCallable{
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {

            try {
                scanner = new Scanner(file);
                writer = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeError(paren, "Se produjo un error al intentar cargar al archivo.");
            }
            return null;
        }
    }
    private class CloseMethod implements SilkCallable {


        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {

            if(scanner != null && writer != null){
                scanner.close();;
                writer.close();
            }else {
                throw new RuntimeError(paren, "No hay archivo para cerrar.");
            }
            return null;
        }
    }
    private class HasNextMethod implements SilkCallable{
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            if(scanner != null){
                return scanner.hasNext();
            }
            return null;
        }
    }
    private class ReadNextMethod implements SilkCallable{
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            if(scanner != null){
                return scanner.nextLine();
            }else{
                throw new RuntimeError(paren, "No se puede leer un archivo no cargado.");
            }
        }
    }
    private class WriteMethod implements SilkCallable{
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            if(!(arguments.get(0) instanceof String)) throw new RuntimeError(paren, "El  argumento debe ser de tipo <STRING>");
            if(writer == null) throw new RuntimeError(paren, "No se escribir en una archivo no cargado. ");
            String toWrite = (String) arguments.get(0);
            writer.write(toWrite);
            return null;
        }
    }

}
