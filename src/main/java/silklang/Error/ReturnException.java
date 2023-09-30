/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Error;

public class ReturnException extends RuntimeException{

    private Object value;

    public ReturnException(Object value) {
        super(null, null, false, false);
        this.value = value;
    }

    public Object getValue(){
        return this.value;
    }
}
