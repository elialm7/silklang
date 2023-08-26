package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Unary extends Expr {

    private Token operator;
    private Expr right;

    public Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }


    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public Expr getRight() {
        return right;
    }

    public void setRight(Expr right) {
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
