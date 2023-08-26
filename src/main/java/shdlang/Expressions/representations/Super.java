package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class Super extends Expr {
    private Token keyword;
    private Token method;

    public Super(Token keyword, Token method) {
        this.keyword = keyword;
        this.method = method;
    }

    public Token getKeyword() {
        return keyword;
    }

    public void setKeyword(Token keyword) {
        this.keyword = keyword;
    }

    public Token getMethod() {
        return method;
    }

    public void setMethod(Token method) {
        this.method = method;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitSuperExpr(this);
    }
}
