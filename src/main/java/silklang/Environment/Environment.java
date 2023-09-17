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
    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;
    public Environment(){
        this.enclosing = null;
    }
    public Environment(Environment enclosing){
        this.enclosing = enclosing;
    }
    public void define(String name, Object value){
        this.values.put(name, value);
    }

    public Object get(Token name){
        if(values.containsKey(name.getLexeme())){
            return values.get(name.getLexeme());
        }
        if(enclosing!=null){
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "La variable '"+name.getLexeme()+"' no esta definida");
    }


    public void assign(Token name, Object value) {

        if(values.containsKey(name.getLexeme())){
            values.put(name.getLexeme(), value);
            return;
        }
        if(enclosing!=null){
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "La variable '"+name.getLexeme()+"' no esta definida");

    }
}
