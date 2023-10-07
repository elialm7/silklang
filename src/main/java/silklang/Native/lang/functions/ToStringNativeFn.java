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

public class ToStringNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        Object input = arguments.get(0);
        if(input instanceof String){
            return input;
        }else if(input instanceof Double){
            Double data = (Double)input;
            return String.valueOf(data);
        }else if(input instanceof Boolean){
            Boolean data = (Boolean)input;
            return String.valueOf(data);
        }else{
            throw new RuntimeError(paren, "Error de parseo en la funcion 'toString();'");
        }
    }
}
