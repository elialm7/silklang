/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.base;

import silklang.ParserRepresentation.Statement.Representation.Expression;
import silklang.ParserRepresentation.Statement.Representation.Print;

public interface StmtVisitor<T>{

    T visitExpressionStmt(Expression expr);
    T visitPrintStmt(Print pr);

}
