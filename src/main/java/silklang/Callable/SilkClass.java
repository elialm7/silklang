/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Callable;

import silklang.Interpreter.Interpreter;

import java.util.List;
import java.util.Map;

public class SilkClass implements SilkCallable {

    private SilkClass superclass;
    private String name;
    private Map<String, SilkFunction> methods;

    public SilkClass(String name, Map<String, SilkFunction> methods, SilkClass superclass){
        this.name = name;
        this.methods = methods;
        this.superclass = superclass;
    }

    public SilkFunction findMethod(String lexeme ){
        if(methods.containsKey(lexeme)){
            return methods.get(lexeme);
        }
        if(superclass != null){
            return superclass.findMethod(lexeme);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int arity() {
        SilkFunction initializer = findMethod("init");
        if(initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        SilkInstance instance = new SilkInstance(this);
        SilkFunction initializer = findMethod("init");
        if(initializer != null){
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
