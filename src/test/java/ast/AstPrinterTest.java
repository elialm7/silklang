package ast;

import org.junit.Test;
import silklang.Expressions.base.Expr;
import silklang.Expressions.representations.Binary;
import silklang.Expressions.representations.Grouping;
import silklang.Expressions.representations.Literal;
import silklang.Expressions.representations.Unary;
import silklang.Ast.AstPrinter;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;

public class AstPrinterTest {


    @Test
    public void testAstPrint(){
        Expr expression = new Binary(
                new Unary(new Token(TokenType.MINUS, "-", null, 1), new Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Grouping(
                        new Literal(45.67)));
        System.out.println(new AstPrinter().print(expression));
    }
}
