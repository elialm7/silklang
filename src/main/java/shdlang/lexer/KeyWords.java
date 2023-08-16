package shdlang.lexer;

import java.util.HashMap;
import java.util.Map;

public class KeyWords {
    public static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("let", TokenType.LET);
    }
}
