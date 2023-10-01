
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Expressions.base.ExprVisitor;

public class Super extends Expr {
    private Token keyword;
    private Token method;

    public Super(Token keyword, Token method) {
        this.keyword = keyword;
        this.method = method;
    }

    public Token getKeyword() {
        return keyword;
    }


    public Token getMethod() {
        return method;
    }


    @Override
    public <T> T accept(ExprVisitor<T> exprVisitor) {
        return exprVisitor.visitSuperExpr(this);
    }
}
