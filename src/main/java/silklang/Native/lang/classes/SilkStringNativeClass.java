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

import java.util.List;

public class SilkStringNativeClass extends SilkInstance {

    private String stringValue;

    public SilkStringNativeClass(SilkClass sclass) {
        super(null);
    }
    public SilkStringNativeClass(String inputString){
        super(null);
        this.stringValue = inputString;

    }



    @Override
    public void set(Token name, Object value) {
       throw new RuntimeError(name, "No se pueden agregar nuevas propiedades a los objetos nativos. ");
    }

    @Override
    public Object get(Token name) {
        return switch (name.getLexeme()){
            case "equals"-> new EqualsMethod();
            case "append"-> new AppendMethod();
            case "get" -> new GetStringMethod();
            default -> throw new RuntimeError(name, "Propiedad indefinida. ");
        };
    }

    @Override
    public String toString() {
        return stringValue;
    }

    private class GetStringMethod implements SilkCallable{

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            return stringValue;
        }
    }

    private class AppendMethod implements SilkCallable{

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            if(arguments.get(0) instanceof String){
                stringValue+= (String) arguments.get(0);
            }else{
                throw new RuntimeError(paren, "Append solo acepta <STRING> como argumento");
            }
            return null;
        }
    }

    private class EqualsMethod implements SilkCallable {
        @Override
        public int arity() {
            return 3;
        }
        @Override
        public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
            Object first = arguments.get(0);
            Object second = arguments.get(1);
            Object flag = arguments.get(2);
            if(first instanceof String && second instanceof String && flag instanceof Boolean){
                String right = (String) first;
                String left = (String) second;
                Boolean ignore = (Boolean) flag;
                if(ignore){
                    return right.equalsIgnoreCase(left);
                }else {
                    return right.equals(left);
                }
            }else{
                   throw new RuntimeError(paren, "Error al parse de argumentos, se acepta tipos de este orden, <STRING>, <STRING>, <BOOLEAN>");
            }
        }
    }


}
