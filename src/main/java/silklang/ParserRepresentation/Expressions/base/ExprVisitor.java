
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.ParserRepresentation.Expressions.base;
import silklang.ParserRepresentation.Expressions.representations.*;

public interface ExprVisitor<T> {
    T visitAssignExpr(Assign expr);
    T visitBinaryExpr(Binary expr);
    T visitCallExpr(Call expr);
    T visitGetExpr(Get expr);
    T visitGroupingExpr(Grouping expr);
    T visitLiteralExpr(Literal expr);
    T visitLogicalExpr(Logical expr);
    T  visitSetExpr(Set expr);
    T visitSuperExpr(Super expr);
    T visitThisExpr(This expr);
    T visitUnaryExpr(Unary expr);
    T visitVariableExpr(Variable expr);
    T visitInputExpr(Input in);
}
