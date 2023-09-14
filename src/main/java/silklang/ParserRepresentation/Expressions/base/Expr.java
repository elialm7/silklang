
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.base;

public abstract class Expr {
    public abstract <T> T accept(ExprVisitor<T> exprVisitor);
}
