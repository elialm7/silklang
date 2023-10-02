/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.Functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;

import java.util.List;
import java.util.Scanner;

public class InputlnNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        System.out.println(interpreter.stringify(arguments.get(0)));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
