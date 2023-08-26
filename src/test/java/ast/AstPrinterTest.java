package ast;

import org.junit.Test;
import shdlang.Expressions.base.Expr;
import shdlang.Expressions.representations.Binary;
import shdlang.Expressions.representations.Grouping;
import shdlang.Expressions.representations.Literal;
import shdlang.Expressions.representations.Unary;
import shdlang.ast.AstPrinter;
import shdlang.lexer.Token;
import shdlang.lexer.TokenType;

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
