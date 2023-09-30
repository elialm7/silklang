/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

import java.util.List;

public class Function extends Stmt {

    private Token name;
    private List<Token> params;
    private List<Stmt> body;

    public Function(Token name, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }


    public Token getName() {
        return name;
    }

    public List<Token> getParams() {
        return params;
    }

    public List<Stmt> getBody() {
        return body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitFunctionStmt(this);
    }
}
