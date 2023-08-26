package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Get extends Expr {
    private Expr object;
    private Token name;

    public Get(Expr object, Token name) {
        this.object = object;
        this.name = name;
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGetExpr(this);
    }
}
