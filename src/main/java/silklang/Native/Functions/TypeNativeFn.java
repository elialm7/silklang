/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;

import java.util.List;

public class TypeNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object valuetype = arguments.get(0);
        if(valuetype instanceof Double){
            System.out.println("<NUMBER>");
        }else if(valuetype instanceof String){
            System.out.println("<STRING>");
        }else if(valuetype instanceof Boolean){
            System.out.println("<BOOLEAN>");
        }else if(valuetype == null){
            System.out.println("<NIL>");
        }
        return null;
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
