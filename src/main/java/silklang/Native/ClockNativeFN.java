/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;

import java.util.List;

public class ClockNativeFN implements SilkCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double)System.currentTimeMillis()/1000.0;
    }

    @Override
    public String toString() {
        return "<Native Fn>";
    }
}
