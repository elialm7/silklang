
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.ExprVisitor;
import silklang.ParserRepresentation.Expressions.base.Expr;

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
    public <T> T accept(ExprVisitor<T> exprVisitor) {
        return exprVisitor.visitGroupingExpr(this);
    }
}
