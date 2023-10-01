/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Resolver;

import silklang.App.Silk;
import silklang.Interpreter.Interpreter;
import silklang.Lexer.Token;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Expressions.base.ExprVisitor;
import silklang.ParserRepresentation.Expressions.representations.*;
import silklang.ParserRepresentation.Statement.Representation.*;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver  implements ExprVisitor<Void>, StmtVisitor<Void> {


    private ClassType currentClass = ClassType.NONE;
    private Interpreter interpreter;
    private Stack<Map<String, Boolean>> scopes = new Stack<>();

    private FunctionType currentFunction= FunctionType.NONE;

    public Resolver(Interpreter interpreter){
        this.interpreter = interpreter;
    }

    private void define(Token name) {
        if (scopes.isEmpty()) return;
        scopes.peek().put(name.getLexeme(), true);
    }
    private void declare(Token name) {
        if (scopes.isEmpty()) return;
        Map<String, Boolean> scope = scopes.peek();
        if(scope.containsKey(name.getLexeme())){
            Silk.error(name, "Ya existe una variable con este nombre en este scope.");
        }
        scope.put(name.getLexeme(), false);
    }

    private void resolve(Expr expr) {
        expr.accept(this);
    }

    public void resolve(List<Stmt> staments){
        for (Stmt st: staments) {
            resolve(st);
        }
    }
    public void resolve(Stmt st){
        st.accept(this);
    }

    public void resolveLocal(Expr expr, Token Name){
        for(int i = scopes.size() - 1;i>= 0;i++){
            if(scopes.get(i).containsKey(Name.getLexeme())){
                interpreter.resolve(expr, scopes.size()-1-i);
                return;
            }
        }
    }

    public void resolveFunction(Function ft, FunctionType type){
        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;
        beginScope();
        for(Token param: ft.getParams()){
            declare(param);
            define(param);
        }
        resolve(ft.getBody());
        endScope();
        currentFunction = enclosingFunction;
    }
    public void beginScope(){
        scopes.push(new HashMap<>());
    }

    public void endScope(){
        scopes.pop();
    }
    @Override
    public Void visitAssignExpr(Assign expr) {
        resolve(expr.getValue());
        resolveLocal(expr, expr.getName());
        return null;
    }

    @Override
    public Void visitBinaryExpr(Binary expr) {
        resolve(expr.getLeft());
        resolve(expr.getRight());
        return null;
    }

    @Override
    public Void visitCallExpr(Call expr) {
        resolve(expr.getCallee());
        for(Expr argument: expr.getArguments()){
            resolve(argument);
        }
        return null;
    }

    @Override
    public Void visitGetExpr(Get expr) {
        resolve(expr.getObject());
        return null;
    }

    @Override
    public Void visitGroupingExpr(Grouping expr) {

        resolve(expr.getExpression());
        return null;
    }

    @Override
    public Void visitLiteralExpr(Literal expr) {
        return null;
    }

    @Override
    public Void visitLogicalExpr(Logical expr) {
        resolve(expr.getLeft());
        resolve(expr.getRight());
        return null;
    }

    @Override
    public Void visitSetExpr(Set expr) {

        resolve(expr.getValue());
        resolve(expr.getObject());
        return null;
    }

    @Override
    public Void visitSuperExpr(Super expr) {
        if(currentClass == ClassType.NONE){
            Silk.error(expr.getKeyword(), " 'super' no puede ser usado afuera de una clase.");
        }else if(currentClass != ClassType.SUBCLASS){
            Silk.error(expr.getKeyword(), " 'super' no puede ser usado dentro de una clase sin superclase.");
        }
        resolveLocal(expr, expr.getKeyword());
        return null;
    }

    @Override
    public Void visitThisExpr(This expr) {
        if(currentClass == ClassType.NONE){
            Silk.error(expr.getKeyword(), "No se puede usar 'this' fuera de una clase. ");
            return null;
        }
        resolveLocal(expr, expr.getKeyword());
        return null;
    }

    @Override
    public Void visitUnaryExpr(Unary expr) {

        resolve(expr.getRight());
        return null;
    }

    @Override
    public Void visitVariableExpr(Variable expr) {

        if(!scopes.isEmpty() && scopes.peek().get(expr.getName().getLexeme()) == Boolean.FALSE){
            Silk.error(expr.getName(), "No se puede leer una variable local en su propia initiilizacion. ");
        }
        resolveLocal(expr, expr.getName());
        return null;
    }

    @Override
    public Void visitInputExpr(Input in) {
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression expr) {
        resolve(expr.getExpr());
        return null;
    }

    @Override
    public Void visitPrintStmt(Print pr) {
        resolve(pr.getExpr());
        return null;
    }

    @Override
    public Void visitVarStmt(Var vr) {

        declare(vr.getName());
        if(vr.getExpression() != null){
            resolve(vr.getExpression());
        }
        define(vr.getName());
        return null;
    }

    @Override
    public Void visitBlockStmt(Block bl) {
        beginScope();
        resolve(bl.getStatements());
        endScope();
        return null;
    }

    @Override
    public Void visitIfStmt(If ifstmt) {
        resolve(ifstmt.getCondition());
        resolve(ifstmt.getThenBranch());
        if(ifstmt.getElseBranch()!= null){
            resolve(ifstmt.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(While wh) {

        resolve(wh.getCondition());
        resolve(wh.getBody());
        return null;
    }

    @Override
    public Void visitBreakStmt(Break br) {
        return null;
    }

    @Override
    public Void visitContinueStmt(Continue ct) {
        return null;
    }

    @Override
    public Void visitFunctionStmt(Function ft) {

        declare(ft.getName());
        define(ft.getName());
        resolveFunction(ft, FunctionType.FUNCTION);
        return null;
    }

    @Override
    public Void visitReturnStmt(Return rt) {

        if(currentFunction == FunctionType.NONE){
            Silk.error(rt.getKeyword(), "No se puede retornar de codigo Top-Level.");
        }
        if(rt.getValue() != null){
            if(currentFunction == FunctionType.INITIALIZER){
                Silk.error(rt.getKeyword(), "No se puede retornar un valor desde un constructor. ");
            }
            resolve(rt.getValue());
        }
        return null;
    }

    @Override
    public Void visitClassStmt(SClass cl) {
        ClassType enclosingclass = currentClass;
        currentClass = ClassType.CLASS;
        declare(cl.getName());
        define(cl.getName());
        if(cl.getSuperclass() != null && cl.getName().getLexeme().equals(cl.getSuperclass().getName().getLexeme())){
            Silk.error(cl.getSuperclass().getName(), "Una clase no puede heredarse a si mismo. ");
        }
        if(cl.getSuperclass() != null){
            currentClass = ClassType.SUBCLASS;
            resolve(cl.getSuperclass());
        }
        if(cl.getSuperclass() != null){
            beginScope();
            scopes.peek().put("super", true);
        }
        beginScope();
        scopes.peek().put("this", true);
        for(Function method: cl.getFunctions()){
            FunctionType type = FunctionType.METHOD;
            if(method.getName().getLexeme().equals("init")){
                type = FunctionType.INITIALIZER;
            }
            resolveFunction(method, type);
        }
        endScope();
        if(cl.getSuperclass() != null){
            endScope();
        }
        currentClass = enclosingclass;
        return null;
    }
}
