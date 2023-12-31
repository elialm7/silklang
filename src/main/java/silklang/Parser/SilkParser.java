
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
            if(match(CLASS)) return classDeclaration();
            if(match(FUN)){
                return function("function");
            }
            return statement();

        }catch (ParseError error){
            synchronize();
            return null;
        }
    }

    private Stmt classDeclaration(){

        Token name = consume(IDENTIFIER, "Se esperaba el nombre de la clase. ");
        Variable superclass = null;
        if(match(LESS)){
            consume(IDENTIFIER, "Se esperaba el nombre de la superclase.");
            superclass = new Variable(previous());
        }
        consume(LEFT_BRACE, "Se esperaba '{' antes del cuerpo de la clase. ");
        List<Function> methods = new ArrayList<>();
        while(!check(RIGHT_BRACE) && !isAtEnd()){
            methods.add((Function) function("metodo"));
        }
        consume(RIGHT_BRACE, "Se esperaba '}' despues del cuerpo de la clase. ");
        return new SClass(name, methods, superclass);
    }

    private Stmt function(String kind){
        Token name =  consume(IDENTIFIER, "Se esperaba el nombre de una "+kind);
        consume(LEFT_PAREN, "Se esperaba '(' despues del  nombre de una " + kind);
        List<Token> parameters = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (parameters.size() >= 100) {
                    error(peek(), "No se puede tener mas de 100 parametros");
                }
                parameters.add(consume(IDENTIFIER, "Se esperaba el nombre de un parametro."));
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Se esperaba  ')' despues de los parametros");
        consume(LEFT_BRACE, "Se esperaba  '{' antes del cuerpo de "+kind);
        List<Stmt> body = block();
        return new Function(name, parameters, body);
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
        if (match(CONTINUE)) return  continueStatement();
        if(match(BREAK)) return  breakStatement();
        if(match(RETURN)) return returnStatement();
        return expressionStatement();

    }

    private Stmt returnStatement(){

        Token keyword = previous();
        Expr value = null;
        if(!check(SEMICOLON)){
            value = expression();
        }
        consume(SEMICOLON, "Se esperaba ';' despues de un return");
        return new Return(keyword, value);
    }

    private Stmt continueStatement(){
        Token keyword = previous();
        consume(SEMICOLON, "Se esperaba un ';' despues de un continue. ");
        return new Continue(keyword);
    }
    private Stmt breakStatement(){
        Token keyword = previous();
        consume(SEMICOLON, "Se esperaba un ';' despues de un break. ");
        return new Break(keyword);
    }

    private Stmt forStatement(){

    	 consume(LEFT_PAREN, "Se esperaba '(' despues de un 'for' ");
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
		 consume(RIGHT_PAREN, "Se esperaba ')' despues de las clausulas. ");
		 Stmt body = statement();
		 Expr condition = null;
		 if (!check(SEMICOLON)) {
			  condition = expression();
		 }
		 consume(SEMICOLON, "Se esperaba ';' despues de la condicion. ");
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
    	 consume(LEFT_PAREN, "Se esperaba '(' despues de un while.");
    	 Expr condition = expression();
    	 consume (RIGHT_PAREN, "Se esperaba ')' despues de una expresion en un while. ");
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
        consume(SEMICOLON, "Se esperaba ';' despues de la expresion.");
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
            }else if( expr instanceof  Get){
                Get get = (Get) expr;
                return new Set(get.getObject(), get.getName(), value);
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
        return call();
    }

    private Expr call(){
        Expr expr = primary();
        while(true){
            if(match(LEFT_PAREN)){
                expr = finishcall(expr);

            }else if(match(DOT)){
                Token name = consume(IDENTIFIER, "Se esperaba el nombre de la propiedad despues del . ");
                expr = new Get(expr, name);
            }else{
                break;
            }
        }
        return expr;
    }

    private Expr finishcall(Expr callee){
        List<Expr> arguments = new ArrayList<>();
        if(!check(RIGHT_PAREN)){
            do{
                if(arguments.size() >= 100){
                    error(peek(), "No se puede tener mas de 100 argumentos. ");
                }
                arguments.add(expression());
            }while(match(COMMA));
        }
        Token paren = consume(RIGHT_PAREN, " Se esperaba ')' despues de los argumentos. ");
        return new Call(callee, paren, arguments);
    }
    private Expr primary(){
        if(match(FALSE)) return new Literal(false);
        if(match(TRUE)) return new Literal(true);
        if(match(NIL)) return new Literal(null);
        if(match(NUMBER, STRING)){
            return new Literal(previous().getLiteral());
        }

        if(match(SUPER)){
            Token keyword = previous();
            consume(DOT, "Se esperaba '.' despues de la palabra reservada 'super' .");
            Token method = consume(IDENTIFIER, "Se esperaba el nombre de un metodo de la super clase. ");
            return new Super(keyword, method);
        }

        if(match(THIS)) return new This(previous());

        if(match(IDENTIFIER)){
            return new Variable(previous());
        }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, " Se esperaba una  ')' despues de una expresion. ");
            return new Grouping(expr);
        }
        if(match(INPUT)){
            return new Input();
        }
       throw error(peek(), " Se esperaba una expresion. ");
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
