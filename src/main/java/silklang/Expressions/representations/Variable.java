package silklang.Expressions.representations;

import silklang.Expressions.base.Visitor;
import silklang.Expressions.base.Expr;
import silklang.Lexer.Token;

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
