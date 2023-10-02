/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Classes;

import silklang.Callable.SilkCallable;
import silklang.Callable.SilkInstance;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.util.List;

public class ArrayNativeClass extends SilkInstance {

    private Object[] elements;

    public ArrayNativeClass(int size){
        super(null);
        elements = new Object[size];
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) buffer.append(", ");
            if (elements[i] == null) {
                buffer.append("nil");
                continue;
            }
            buffer.append(elements[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

    @Override
    public void set(Token name, Object value) {
        throw new RuntimeError(name, "No se puede definir propiedades a un array. ");
    }

    @Override
    public Object get(Token name) {

        return switch (name.getLexeme()) {
            case "get" -> new ArrayGetMethod(name);
            case "set" -> new ArraySetMethod(name);
            case "size" -> new ArraySizeMethod(name);
            default -> throw new RuntimeError(name, " Propeidad indefinida. ");
        };

    }
     class ArrayGetMethod implements SilkCallable{

        private Token name;
        public ArrayGetMethod(Token name){
            this.name = name;
        }
        @Override
        public int arity() {
            return 1;
        }
        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if(arguments.get(0) instanceof Double) {
                int index = ((Double) arguments.get(0)).intValue();
                return elements[index];
            }else {
                throw new RuntimeError(name, "El metodo get de la clase array espera un tipo number. get(<NUMBER>). Valor pasado: " + interpreter.stringify(arguments.get(0)));
            }
        }
    }

    class ArraySetMethod implements SilkCallable{
        private Token name;

        public ArraySetMethod(Token name) {
            this.name = name;
        }

        @Override
        public int arity() {
            return 2;
        }
        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            Object indexObj = arguments.get(0);
            int index;
            if(indexObj instanceof Double){
                index = ((Double) arguments.get(0)).intValue();
                Object value = arguments.get(1);
                return elements[index]=value;
            }else {
                return new RuntimeError(name, "El primer elemento del metodo set de la clase array, espera un tipo NUMBER, .set(<NUMBER>, <CUALQUIERA>);");
            }

        }
    }

    class ArraySizeMethod implements SilkCallable{

        private Token name;

        public ArraySizeMethod(Token name) {
            this.name = name;
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            double size= (double) elements.length;
            return size;
        }
    }
}
