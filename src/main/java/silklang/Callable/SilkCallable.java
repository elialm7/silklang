/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Callable;

import silklang.Interpreter.Interpreter;

import java.util.List;

public interface SilkCallable {

    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
