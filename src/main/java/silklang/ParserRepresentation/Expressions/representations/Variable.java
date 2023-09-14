
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.ExprVisitor;
import silklang.ParserRepresentation.Expressions.base.Expr;
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
    public <T> T accept(ExprVisitor<T> exprVisitor) {
        return exprVisitor.visitVariableExpr(this);
    }
}
