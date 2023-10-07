/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.math;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.util.List;

public class MathNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        return new MathNativeClass();
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
