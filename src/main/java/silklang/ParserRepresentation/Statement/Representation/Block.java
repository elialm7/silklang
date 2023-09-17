/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

import java.util.List;

public class Block extends Stmt {

	 private List<Stmt> statements;

	 public Block(List<Stmt> statements){
	 	 this.statements = statements;
	 }

	 public List<Stmt> getStatements() {
		  return statements;
	 }

	 @Override
	 public <T> T accept(StmtVisitor<T> visitor) {
		  return visitor.visitBlockStmt(this);
	 }
}
