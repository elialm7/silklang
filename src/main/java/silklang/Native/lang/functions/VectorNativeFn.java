/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.functions;

import silklang.Callable.SilkCallable;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;
import silklang.Native.lang.classes.VectorNativeClass;

import java.util.List;

public class VectorNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        Object arg = arguments.get(0);
        if(arg instanceof Double){
            int size = ((Double)arg).intValue();
            return new VectorNativeClass(size);
        }
        throw new RuntimeError(paren, "Array acepta solo tipo <NUMBER> para su tamaño");
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
