package shdlang.Expressions.representations;

import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.base.Expr;

public class Grouping extends Expr {
   private Expr expression;

    public Grouping(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

    public void setExpression(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }
}
