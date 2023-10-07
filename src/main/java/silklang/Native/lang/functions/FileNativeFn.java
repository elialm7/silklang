/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.functions;

import silklang.Callable.SilkCallable;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;
import silklang.Native.lang.classes.FileNativeClass;

import java.util.List;

public class FileNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        Object object = arguments.get(0);
        if(object instanceof String){
            String data = (String)object;
            return new FileNativeClass(data);
        }else{
            throw new RuntimeError(paren, "El argumento pasado es invalido. Debe ser de tipo <STRING>");
        }
    }
}
