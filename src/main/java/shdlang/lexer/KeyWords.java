package shdlang.lexer;

import java.util.HashMap;
import java.util.Map;

public class KeyWords {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    public static TokenType get(String key){
        return KEYWORDS.get(key);
    }
    static {
        KEYWORDS.put("let", TokenType.LET);
    }
}
