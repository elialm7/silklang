package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Logical extends Expr {
    private Expr left;
    private Token operator;
    private Expr right;

    public Logical(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public void setLeft(Expr left) {
        this.left = left;
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
        return null;
    }
}
