package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Set extends Expr {
    private Expr object;
    private Token name;

    private Expr value;

    public Set(Expr object, Token name, Expr value) {
        this.object = object;
        this.name = name;
        this.value = value;
    }

    public Expr getObject() {
        return object;
    }

    public void setObject(Expr object) {
        this.object = object;
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
        return visitor.visitSetExpr(this);
    }
}
