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
