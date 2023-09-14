
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Lexer;

import silklang.App.Silk;

import java.util.ArrayList;
import java.util.List;

import static silklang.Lexer.TokenType.*;


public class SilkLexer {

    private final String source;
    private final List<Token> tokens;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public SilkLexer(String source){
        this.source = source;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    private void scanToken(){
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
            }
            case ' ', '\r', '\t' -> {
            }
            // Ignore whitespace.
            case '\n' -> line++;
            case '"' -> string();
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Silk.error(line, "Unexpected character. ");
                }
            }
        }
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c ){
        return  (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }


    private void identifier(){
        while(isAlphaNumeric(peek())){
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = KeyWords.get(text);
        if(type==null){
            type = IDENTIFIER;
        }
        addToken(type);
    }


    private void number(){
        while(isDigit(peek())){
            advance();
        }
        if(peek() == '.' && isDigit(peekNext())){
            advance();
            while(isDigit(peek())){
                advance();
            }
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext(){
        if(current + 1 >= source.length()){
            return '\0';
        }
        return source.charAt(current + 1);
    }



    private boolean isDigit(char c){
        return c  >= '0' && c <='9';
    }
    private void string(){
        while(peek()!= '"' && !isAtEnd()){
            if(peek() == '\n'){
                line++;
            }
            advance();
        }
        if(isAtEnd()){
            Silk.error(line, "Unterminated String");
            return;
        }
        advance();

        String value = source.substring(start + 1,current - 1);
        addToken(STRING, value);
    }
    private boolean match(char expected){
        if(isAtEnd()){
            return false;
        }
        if(source.charAt(current) != expected){
            return false;
        }
        current++;
        return true;
    }
    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char peek(){
        if(isAtEnd()){
            return '\0';
        }
        return source.charAt(current);
    }

    private char advance(){
        current++;
        return source.charAt(current-1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}









