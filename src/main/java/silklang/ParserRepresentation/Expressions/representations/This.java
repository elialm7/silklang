
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.ExprVisitor;
import silklang.ParserRepresentation.Expressions.base.Expr;
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
    public <T> T accept(ExprVisitor<T> exprVisitor) {
        return exprVisitor.visitThisExpr(this);
    }
}
