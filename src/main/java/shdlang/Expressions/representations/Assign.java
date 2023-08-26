package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Assign extends Expr {
    private Token name;
    private Expr value;

    public Assign(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitAssignExpr(this);
    }
}
