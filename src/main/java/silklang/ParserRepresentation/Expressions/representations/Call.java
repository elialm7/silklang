
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.representations;

import silklang.ParserRepresentation.Expressions.base.Visitor;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.Lexer.Token;

import java.util.List;

public class Call extends Expr {
    private Expr callee;
    private Token paren;
    private List<Expr> arguments;
    public Call(Expr callee, Token paren, List<Expr> arguments){
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    public Expr getCallee() {
        return callee;
    }

    public void setCallee(Expr callee) {
        this.callee = callee;
    }

    public Token getParen() {
        return paren;
    }

    public void setParen(Token paren) {
        this.paren = paren;
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    public void setArguments(List<Expr> arguments) {
        this.arguments = arguments;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCallExpr(this);
    }
}
