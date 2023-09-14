/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class Expression extends Stmt {
    public final Expr expr;

    public Expression(Expr expr){
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
