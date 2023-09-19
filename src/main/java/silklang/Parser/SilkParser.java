
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Parser;

import silklang.App.Silk;
import silklang.Error.ParseError;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Expressions.representations.*;
import silklang.ParserRepresentation.Statement.Representation.*;
import silklang.ParserRepresentation.Statement.base.Stmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static silklang.Lexer.TokenType.*;

public class SilkParser {

    private  List<Token> tokens;
    private int current = 0;

    public SilkParser(List<Token> tokens){
        this.tokens = tokens;

    }
    public List<Stmt>  parse(){
        List<Stmt> statements = new ArrayList<>();
        while(!isAtEnd()){
            statements.add(declaration());
        }
        return statements;
    }
    private Stmt declaration(){
        try{
            if(match(VAR)){
                return varDeclaration();
            }
            return statement();

        }catch (ParseError error){
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration(){

        Token name = consume(IDENTIFIER, "Se esperaba el nombre de una variable. ");

        Expr initializer = null;
        if(match(EQUAL)){
            initializer = expression();
        }
        consume(SEMICOLON, "Se esperaba ';' despues de la declaracion de variable. ");
        return new Var(name, initializer);

    }

    private Stmt statement(){

        if(match(IF)) return ifStatement();
        if(match(PRINT)) return printStatement();
        if(match(WHILE)) return whileStatement();
        if(match(FOR)) return forStatement();
        if(match(LEFT_BRACE)) return new Block(block());

        return expressionStatement();

    }

    private Stmt forStatement(){

    	 consume(LEFT_PAREN, "Se espera '(' despues de un 'for' ");
		 Stmt initializer;
		 if (match(SEMICOLON)) {
			  initializer = null;
		 } else if (match(VAR)) {
			  initializer = varDeclaration();
		 } else {
			  initializer = expressionStatement();
		 }
		 Expr increment = null;
		 if (!check(RIGHT_PAREN)) {
			  increment = expression();
		 }
		 consume(RIGHT_PAREN, "Se espera ')' despues de las clausulas. ");
		 Stmt body = statement();
		 Expr condition = null;
		 if (!check(SEMICOLON)) {
			  condition = expression();
		 }
		 consume(SEMICOLON, "Se espera ';' despues de la condicion. ");
		 if (increment != null) {
			  body = new Block(Arrays.asList(body,new Expression(increment)));
		 }
		 if (condition == null) condition = new Literal(true);
		 body = new While(condition, body);
		 if (initializer != null) {
			  body = new Block(Arrays.asList(initializer, body));
		 }

		 return body;


	}

    private Stmt whileStatement(){
    	 consume(LEFT_PAREN, "Se espera '(' despues de un while.");
    	 Expr condition = expression();
    	 consume (RIGHT_PAREN, "Se espera ')' despues de una expresion en un while. ");
    	 Stmt body = statement();
    	 return new While(condition, body);
	}

    private Stmt ifStatement(){
        consume(LEFT_PAREN, "Se esperaba '(' despues de un 'if'. ");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Se esperaba ')' despues una condicion. ");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if(match(ELSE)){
            elseBranch = statement();
        }

        return new If(condition, thenBranch, elseBranch);

    }

    private Stmt printStatement(){
        Expr value = expression();
        consume(SEMICOLON, "Se esperaba ';' despues del valor.");
        return new Print(value);
    }

    private Stmt expressionStatement(){
        Expr expression = expression();
        consume(SEMICOLON, "se esperaba ';' despues de la expresion.");
        return new Expression(expression);
    }

    private List<Stmt> block(){

        List<Stmt> statements = new ArrayList<>();

        while(!check(RIGHT_BRACE) && !isAtEnd()){
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Se esperaba '}' despues de un bloque");
        return statements;

    }

    private Expr expression(){
        return assignment();
    }

    private Expr assignment(){
        Expr expr = or();
        if(match(EQUAL)){
            Token equals = previous();
            Expr value = assignment();
            if(expr instanceof Variable){
                Token name = ((Variable)expr).getName();
                return new Assign(name, value);
            }
            error(equals, "Objetivo de asignamiento invalido. ");
        }
        return expr;
    }


    private Expr or(){
        Expr expr = and();

        while(match(OR)){
            Token operator = previous();
            Expr right = and();
            expr = new Logical(expr, operator, right);
        }

        return expr;
    }

    private Expr and(){

        Expr expr = equality();
        while(match(AND)){
            Token operator = previous();
            Expr right = equality();
            expr = new Logical(expr, operator, right);
        }

        return expr;

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
      while(match(SLASH, STAR, MOD)){
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
        if(match(IDENTIFIER)){
            return new Variable(previous());
        }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Se espera una  ')' despues de una expresion. ");
            return new Grouping(expr);
        }
        if(match(INPUT)){
            return new Input();
        }
       throw error(peek(), "Se esperaba una expresion. ");
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
