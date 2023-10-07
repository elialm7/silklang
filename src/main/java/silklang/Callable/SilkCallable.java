/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Callable;

import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;

import java.util.List;

public interface SilkCallable {

    int arity();
    default boolean variadic(){
        return false;
    }
    Object call(Interpreter interpreter, List<Object> arguments, Token paren);
    default String nativefuncString(){
        return "fn <Native>";
    }
    default String usage(){
        return "No usage";
    }
}
