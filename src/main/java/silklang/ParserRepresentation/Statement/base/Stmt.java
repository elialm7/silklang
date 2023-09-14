/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.base;

public abstract class Stmt {

    public abstract <T> T accept(StmtVisitor<T> visitor);

}
