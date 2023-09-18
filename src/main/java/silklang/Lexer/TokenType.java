
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Lexer;

public enum TokenType {
    //single-character tokens
  LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, MOD,

    //one or two character tokens
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    //literals
    IDENTIFIER, STRING, NUMBER,

    //Keywords
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR, PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE, EOF, INPUT
}
