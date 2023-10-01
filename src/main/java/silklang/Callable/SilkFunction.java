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
    private boolean isInitializer;
    private final Environment closure;
    public SilkFunction(Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
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
            if(isInitializer){
                return closure.getAt(0, "this");
            }
            return exception.getValue();
        }
        if(isInitializer ){
            return closure.getAt(0, "this");
        }
        return null;
    }
    public SilkFunction bind(SilkInstance instance){
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new SilkFunction(declaration, environment, isInitializer);
    }
    @Override
    public String toString() {
        return "<fn " + declaration.getName().getLexeme() + ">";
    }
}
