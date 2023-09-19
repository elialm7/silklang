/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Statement.Representation;

import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

public class While extends Stmt {

	private Expr condition;
	private Stmt body;

	 public While(Expr condition, Stmt body) {
		  this.condition = condition;
		  this.body = body;
	 }


	 public Expr getCondition() {
		  return condition;
	 }

	 public Stmt getBody() {
		  return body;
	 }

	 @Override
	 public <T> T accept(StmtVisitor<T> visitor) {
		  return visitor.visitWhileStmt(this);
	 }
}
