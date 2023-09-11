package silklang.Interpreter;

import silklang.App.silk;
import silklang.Error.RuntimeError;
import silklang.Expressions.base.Expr;
import silklang.Expressions.base.Visitor;
import silklang.Expressions.representations.*;
import silklang.Lexer.Token;

import java.sql.SQLOutput;

public class Interpreter implements Visitor<Object> {

    public void interpret(Expr expression){
        try{
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }catch (RuntimeError error){
            silk.runtimeError(error);
        }

    }
    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    private boolean isTruthy(Object object){
        if (object == null) return false;
        if(object instanceof  Boolean) return(boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b){
        if(a == null && b == null) return true;
        if(a  == null) return false;
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand){
        if(operand instanceof Double)return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void checkNumberOperands(Token operator, Object left, Object right){
        if(left instanceof Double && right instanceof Double)return;
        throw new RuntimeError(operator, "Operands must be numbers ");

    }
    private void divisionbyZero(Token operator, Object left, Object right){
        if((double)right!=0)return;
        throw new RuntimeError(operator, "Division by zero is not allowed");
    }


    @Override
    public Object visitAssignExpr(Assign expr) {
        return null;
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.getLeft());
        Object right = evaluate(expr.getRight());
        switch (expr.getOperator().getType()){
            case GREATER:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left <= (double) right;
            case MINUS:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperands(expr.getOperator(), left, right);
                divisionbyZero(expr.getOperator(), left, right);
                return (double)left / (double) right;
            case STAR:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left * (double) right;
            case PLUS:
                if(left instanceof  Double && right instanceof Double){
                    return (double)left+(double)right;
                }
                if(left instanceof String && right instanceof String){
                    return (String)left + (String)right;
                }
                break;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);

        }
        return null;
    }

    @Override
    public Object visitCallExpr(Call expr) {
        return null;
    }

    @Override
    public Object visitGetExpr(Get expr) {
        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.getExpression());
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.getValue();
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        return null;
    }

    @Override
    public Object visitSetExpr(Set expr) {
        return null;
    }

    @Override
    public Object visitSuperExpr(Super expr) {
        return null;
    }

    @Override
    public Object visitThisExpr(This expr) {
        return null;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.getRight());
        switch (expr.getOperator().getType()){
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.getOperator(), right);
                return -(double)right;
        }
        //unreachable
        return null;
    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        return null;
    }
}
