/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Native.lang.functions;

import silklang.Callable.SilkCallable;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.util.List;
import java.util.Scanner;

public class InputNativeFn implements SilkCallable {
    @Override
    public int arity() {
        return 1;
    }
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, Token paren) {
        System.out.print(interpreter.stringify(arguments.get(0)));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public String toString() {
        return nativefuncString();
    }
}
