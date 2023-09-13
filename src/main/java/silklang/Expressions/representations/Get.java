
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Expressions.representations;

import silklang.Expressions.base.Visitor;
import silklang.Expressions.base.Expr;
import silklang.Lexer.Token;

public class Get extends Expr {
    private Expr object;
    private Token name;

    public Get(Expr object, Token name) {
        this.object = object;
        this.name = name;
    }

    public Expr getObject() {
        return object;
    }

    public void setObject(Expr object) {
        this.object = object;
    }

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGetExpr(this);
    }
}
