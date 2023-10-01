/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Callable;

import silklang.Error.RuntimeError;
import silklang.Lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class SilkInstance {

    private SilkClass Sclass;
    private Map<String, Object> fields = new HashMap<>();
    public SilkInstance(SilkClass sclass) {
        Sclass = sclass;
    }

    public Object get(Token name){
        if(fields.containsKey(name.getLexeme())){
            return fields.get(name.getLexeme());
        }

        SilkFunction method = Sclass.findMethod(name.getLexeme());
        if(method != null) return method.bind(this);

        throw new RuntimeError(name, name.getLexeme() + " propiedad no definida.");
    }



    public void set(Token name, Object value){
        fields.put(name.getLexeme(), value);
    }

    public SilkClass getSclass() {
        return Sclass;
    }

    @Override
    public String toString() {
        return this.Sclass.getName() + " instance";
    }
}
