/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Error.RuntimeError;
import silklang.Interpreter.Interpreter;
import silklang.Native.Classes.ArrayNativeClass;

import java.util.List;

public class ArrayNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(arg instanceof Double){
            int size = ((Double)arg).intValue();
            return new ArrayNativeClass(size);
        }
        throw new RuntimeError(null, "Array acepta solo tipo <NUMBER> para su tamaño");
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
