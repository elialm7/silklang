/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;

import java.util.List;

public class EchoNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public boolean variadic() {
        return true;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        arguments.stream().map(interpreter::stringify).forEach(System.out::println);
        return null;
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
