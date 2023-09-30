/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Callable;

import silklang.Environment.Environment;
import silklang.Error.ReturnException;
import silklang.Interpreter.Interpreter;
import silklang.ParserRepresentation.Statement.Representation.Function;

import java.util.List;

public class SilkFunction implements SilkCallable{
    private Function declaration;

    private final Environment closure;
    public SilkFunction(Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.getParams().size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i <  declaration.getParams().size() ; i++) {
            environment.define(declaration.getParams().get(i).getLexeme(), arguments.get(i));
        }
        try {
            interpreter.executeBlock(declaration.getBody(), environment);
        }catch (ReturnException exception){
            return exception.getValue();
        }
        return null;
    }
    @Override
    public String toString() {
        return "<fn " + declaration.getName().getLexeme() + ">";
    }
}
