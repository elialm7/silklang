/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class If extends Stmt {

    private Expr condition;
    private Stmt thenBranch;
    private Stmt elseBranch;

    public If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expr getCondition() {
        return condition;
    }

    public Stmt getThenBranch() {
        return thenBranch;
    }

    public Stmt getElseBranch() {
        return elseBranch;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitIfStmt(this);
    }
}
