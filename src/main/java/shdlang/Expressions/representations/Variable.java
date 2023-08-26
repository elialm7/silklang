package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Variable extends Expr {
    private Token name;

    public Variable(Token name) {
        this.name = name;

    }

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
