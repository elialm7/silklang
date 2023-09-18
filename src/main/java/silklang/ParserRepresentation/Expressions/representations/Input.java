/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Expressions.base.ExprVisitor;

public class Input extends Expr {
    @Override
    public <T> T accept(ExprVisitor<T> exprVisitor) {
        return exprVisitor.visitInputExpr(this);
    }
}
