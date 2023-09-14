
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.Visitor;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.Lexer.Token;

public class Set extends Expr {
    private Expr object;
    private Token name;

    private Expr value;

    public Set(Expr object, Token name, Expr value) {
        this.object = object;
        this.name = name;
        this.value = value;
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

    public Expr getValue() {
        return value;
    }

    public void setValue(Expr value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitSetExpr(this);
    }
}
