/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.base;

import silklang.ParserRepresentation.Statement.Representation.*;

public interface StmtVisitor<T>{

    T visitExpressionStmt(Expression expr);
    T visitPrintStmt(Print pr);
    T visitVarStmt(Var vr);
    T visitBlockStmt(Block bl);
    T visitIfStmt(If ifstmt);
    T visitWhileStmt(While wh);
}
