package silklang.Expressions.representations;

import silklang.Expressions.base.Visitor;
import silklang.Expressions.base.Expr;
import silklang.Lexer.Token;

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
