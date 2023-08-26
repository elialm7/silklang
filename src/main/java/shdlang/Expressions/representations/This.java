package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;
import shdlang.lexer.Token;

public class This extends Expr {
    private Token keyword;

    public This(Token keyword) {
        this.keyword = keyword;
    }

    public Token getKeyword() {
        return keyword;
    }

    public void setKeyword(Token keyword) {
        this.keyword = keyword;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitThisExpr(this);
    }
}
