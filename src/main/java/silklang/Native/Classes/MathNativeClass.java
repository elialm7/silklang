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
import java.util.Random;

public class MathNativeClass extends SilkInstance {
    public MathNativeClass() {
        super(null);
    }

    @Override
    public Object get(Token name) {
        return switch (name.getLexeme()){
            case "random"-> new RandomNativeMethod(name);
            default -> {
               throw new RuntimeError(name, "Propiedad indefinida.");
            }
        };
    }

    @Override
    public void set(Token name, Object value) {
        throw new RuntimeError(name, "No se puede agregar propiedades a una clase nativa. ");
    }

    class RandomNativeMethod implements SilkCallable{
        private Token name;
        public RandomNativeMethod(Token name){
            this.name = name;
        }
        @Override
        public int arity() {
            return 2;
        }
        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            Object valueMin = arguments.get(0);
            Object valueMax = arguments.get(1);
            if(valueMin  instanceof Double && valueMax instanceof  Double){
                int min = ((Double)valueMin).intValue();
                int max = ((Double)valueMax).intValue();
                Random ramdon =  new Random();
                int rand = ramdon.nextInt(max - min) + min;
                return (double)rand;
            }else{
                throw new RuntimeError(name, "El metodo random acepta dos parametros de tipo <NUMBER>, .random(<NUMBER>, <NUMBER>)");
            }

        }
    }

}
