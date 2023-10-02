/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;

import java.util.List;

public class IsStringNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object argument = arguments.get(0);
        if(argument instanceof String){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
