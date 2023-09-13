
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Error;

import silklang.Lexer.Token;

public class RuntimeError extends  RuntimeException{
    private Token token;

    public RuntimeError(Token token, String message){
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
