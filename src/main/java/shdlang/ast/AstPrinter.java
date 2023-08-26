package shdlang.ast;

import shdlang.Expressions.base.Expr;
import shdlang.Expressions.base.Visitor;
import shdlang.Expressions.representations.*;

public class AstPrinter implements Visitor<String> {

    public String print(Expr expr){
        return expr.accept(this);
    }

    private String parenthezsize(String name, Expr... exprs){
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for(Expr expr: exprs){
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }


    @Override
    public String visitAssignExpr(Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthezsize(expr.getOperator().getLexeme(), expr.getLeft(), expr.getRight());
    }

    @Override
    public String visitCallExpr(Call expr) {
        return null;
    }

    @Override
    public String visitGetExpr(Get expr) {
        return null;
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthezsize("group", expr.getExpression());
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if(expr.getValue() == null) return "nil";
        return expr.getValue().toString();
    }

    @Override
    public String visitLogicalExpr(Logical expr) {
        return null;
    }

    @Override
    public String visitSetExpr(Set expr) {
        return null;
    }

    @Override
    public String visitSuperExpr(Super expr) {
        return null;
    }

    @Override
    public String visitThisExpr(This expr) {
        return null;
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthezsize(expr.getOperator().getLexeme(), expr.getRight());
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return null;
    }
}
