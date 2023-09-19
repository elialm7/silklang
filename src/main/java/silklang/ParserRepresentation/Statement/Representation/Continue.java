/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.Lexer.Token;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class Continue  extends Stmt {

	 private Token keyword;

	 public Continue(Token keyword) {
		  this.keyword = keyword;
	 }

	 public Token getKeyword() {
		  return keyword;
	 }

	 @Override
	 public <T> T accept(StmtVisitor<T> visitor) {
		  return visitor.visitContinueStmt(this);
	 }
}
