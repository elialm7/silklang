/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.functions;

import silklang.Callable.SilkCallable;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.util.List;

public class ExitNativeFn implements SilkCallable {


    @Override
    public int arity() {
        return 1;
    }

    @Override
    public boolean variadic() {
        return true;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        if(arguments.size() == 0){
            System.exit(0);
        }
        Object value = arguments.get(0);
        if(value instanceof Double){
            Double newValue = (Double) value;
            int endValue = newValue.intValue();
            System.exit(endValue);
        }else{
           throw new RuntimeError(paren, "se esperaba un tipo NUMBER, no un " + interpreter.stringify(value));
        }
        return null;
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
