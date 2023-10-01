/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Expressions.representations.Variable;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

import java.util.List;

public class SClass extends Stmt {


    private Token name;
    private List<Function> functions;

    private Variable superclass;
    public SClass(Token name, List<Function> functions, Variable superclass) {
        this.name = name;
        this.functions = functions;
        this.superclass = superclass;
    }


    public Variable getSuperclass() {
        return superclass;
    }

    public Token getName() {
        return name;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitClassStmt(this);
    }
}
