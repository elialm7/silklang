/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Environment;

import silklang.Error.RuntimeError;
import silklang.Lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values;

    public Environment(){

        this.values = new HashMap<>();

    }

    public void define(String name, Object value){
        this.values.put(name, value);
    }

    public Object get(Token name){
        if(values.containsKey(name.getLexeme())){
            return values.get(name.getLexeme());
        }

        throw new RuntimeError(name, "La variable '"+name.getLexeme()+"' no esta definida");

    }


    public void assign(Token name, Object value) {

        if(values.containsKey(name.getLexeme())){
            values.put(name.getLexeme(), value);
            return;
        }

        throw new RuntimeError(name, "La variable '"+name.getLexeme()+"' no esta definida");

    }
}
