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

public class ToNumberNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        try {
            Object input = arguments.get(0);
            if (input instanceof String) {
                return Double.parseDouble((String) input);
            } else {
                return null;
            }
        }catch (NumberFormatException ex){
            throw new RuntimeError(paren, "Error de formato en la entrada de la funcion 'toNumber();' ");
        }
    }
}
