/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class Var extends Stmt {

    private final Token name;

    private final Expr expression;
    public Var(Token name, Expr expression){
        this.name = name;
        this.expression = expression;
    }

    public Token getName() {
        return name;
    }

    public Expr getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitVarStmt(this);
    }
}
