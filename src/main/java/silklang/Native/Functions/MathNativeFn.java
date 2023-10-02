/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;
import silklang.Native.Classes.MathNativeClass;

import java.util.List;

public class MathNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return new MathNativeClass();
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
