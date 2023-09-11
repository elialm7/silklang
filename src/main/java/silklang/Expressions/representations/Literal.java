package silklang.Expressions.representations;

import silklang.Expressions.base.Visitor;
import silklang.Expressions.base.Expr;

public class Literal extends Expr {

    private Object value;

    public Literal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}
