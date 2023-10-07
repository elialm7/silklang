/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;
import silklang.Native.lang.classes.VectorNativeClass;

import java.util.List;

public class ToVectorNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public boolean variadic() {
        return true;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        Object[] obs = arguments.toArray();
        return new VectorNativeClass(obs);
    }
}
