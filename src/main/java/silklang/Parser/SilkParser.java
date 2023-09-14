
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Parser;

import silklang.Error.ParseError;
import silklang.Expressions.base.Expr;
import silklang.Expressions.representations.Binary;
import silklang.Expressions.representations.Grouping;
import silklang.Expressions.representations.Literal;
import silklang.Expressions.representations.Unary;
import silklang.App.Silk;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;

import java.util.List;

import static silklang.Lexer.TokenType.*;

public class SilkParser {

    private  List<Token> tokens;
    private int current = 0;

    public SilkParser(List<Token> tokens){
        this.tokens = tokens;

    }

    private Expr expression(){
        return equality();
    }

    private Expr equality(){

        Expr expr = comparison();

        while(match(BANG_EQUAL, EQUAL_EQUAL)){
            Token operator = previous();
            Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison(){
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            Token operator = previous();
            Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term(){

        Expr expr = factor();

        while(match(MINUS, PLUS)){
            Token operator = previous();
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }

        return expr;

    }

    private Expr factor(){
      Expr expr = unary();
      while(match(SLASH, STAR)){
          Token operator = previous();
          Expr right = unary();
          expr = new Binary(expr, operator, right);
      }
      return expr;
    }

    private Expr unary(){
        if(match(BANG, MINUS)){
            Token operator = previous();
            Expr right = unary();
            return new Unary(operator, right);
        }
        return primary();
    }

    private Expr primary(){
        if(match(FALSE)) return new Literal(false);
        if(match(TRUE)) return new Literal(true);
        if(match(NIL)) return new Literal(null);
        if(match(NUMBER, STRING)){
            return new Literal(previous().getLiteral());
        }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Se espera una  ')' despues de una expresion. ");
            return new Grouping(expr);
        }
       throw error(peek(), "Se esperaba una expresion. ");
    }
    public Expr parse(){
        try{
            return expression();
        }catch (ParseError error){
            return null;
        }
    }
    private Token consume(TokenType type, String message){
        if(check(type)) return advance();
        throw error(peek(),message);
    }

    private ParseError error(Token token, String message){
        Silk.error(token, message);
        return new ParseError();
    }
    private void synchronize(){
        advance();
        while(!isAtEnd()){
            if(previous().getType() == SEMICOLON) return;
            switch (peek().getType()){
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }


    private boolean match(TokenType... types){
        for(TokenType type: types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type){
        if(isAtEnd()){
            return false;
        }
        return peek().getType() == type;
    }

    private Token advance(){

        if(!isAtEnd()){
            current++;
        }
        return previous();
    }

    private boolean isAtEnd(){
        return peek().getType() == EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }


    private Token previous(){
        return tokens.get(current-1);

    }
}
