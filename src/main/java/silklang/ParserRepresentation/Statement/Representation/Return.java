/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class Return extends Stmt {

    private Token keyword;
    private Expr value;

    public Return(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }


    public Token getKeyword() {
        return keyword;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
