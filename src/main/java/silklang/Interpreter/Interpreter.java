
/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Interpreter;

import silklang.App.Silk;
import silklang.Callable.SilkCallable;
import silklang.Callable.SilkClass;
import silklang.Callable.SilkFunction;
import silklang.Callable.SilkInstance;
import silklang.Environment.Environment;
import silklang.Error.JumpError;
import silklang.Error.JumpType;
import silklang.Error.ReturnException;
import silklang.Error.RuntimeError;
import silklang.Lexer.Token;
import silklang.Lexer.TokenType;
import silklang.Native.lang.functions.*;
import silklang.Native.math.MathNativeFn;
import silklang.ParserRepresentation.Expressions.base.Expr;
import silklang.ParserRepresentation.Expressions.base.ExprVisitor;
import silklang.ParserRepresentation.Expressions.representations.Set;
import silklang.ParserRepresentation.Expressions.representations.*;
import silklang.ParserRepresentation.Statement.Representation.*;
import silklang.ParserRepresentation.Statement.base.Stmt;
import silklang.ParserRepresentation.Statement.base.StmtVisitor;

import java.util.*;


public class Interpreter implements ExprVisitor<Object>, StmtVisitor<Void> {

    private Environment env;
    private Environment globals;
    private Map<Expr, Integer> locals;
    public Interpreter(){
        this.globals = new Environment();
        this.locals = new HashMap<>();
        this.env = globals;
        defineNativeFunctions();
    }
    private void defineNativeFunctions(){
        this.globals.define("clock", new ClockNativeFn());
        this.globals.define("input", new InputNativeFn());
        this.globals.define("inputln", new InputlnNativeFn());
        this.globals.define("type", new TypeNativeFn());
        this.globals.define("echo", new EchoNativeFn());
        this.globals.define("isNumber", new IsNumberNativeFn());
        this.globals.define("isString", new IsStringNativeFn());
        this.globals.define("isBoolean", new IsBooleanNativeFn());
        this.globals.define("exit", new ExitNativeFn());
        this.globals.define("toNumber", new ToNumberNativeFn());
        this.globals.define("toString", new ToStringNativeFn());//
        this.globals.define("toBoolean", new ToBooleanNativeFn());//
        this.globals.define("toVector", new ToVectorNativeFn());//
        this.globals.define("math", new MathNativeFn());
        this.globals.define("vector", new VectorNativeFn());
        this.globals.define("string", new SilkStringNativeFunction());
        this.globals.define("file", new FileNativeFn());

    }
    public Environment getGlobals(){
        return this.globals;
    }


    private Object lookUpVariable(Token name, Expr expr){
        Integer distance = locals.get(expr);
        if(distance != null){
            return env.getAt(distance, name.getLexeme());
        }else{
            return globals.get(name);
        }
    }


    public void resolve(Expr expr, int size){
        locals.put(expr, size);
    }

    public void interpret(List<Stmt> statements){

        try{
            for(Stmt st: statements){
                execute(st);
            }
        }catch (RuntimeError error){
            Silk.runtimeError(error);
        }
    }
    private void execute(Stmt st){
        st.accept(this);
    }
    public void executeBlock(List<Stmt> stmts, Environment environment){
        Environment previous = this.env;
        try{
            this.env = environment;
            for(Stmt st : stmts){
                execute(st);
            }
        }finally {
            this.env = previous;
        }
    }


    public String stringify(Object object) {
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

    private Object parseDouble(String input){
        try{
            return Double.parseDouble(input);
        }catch (NumberFormatException exception){
            return input;
        }
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
        throw new RuntimeError(operator, " El operando debe ser un numero. ");
    }

    private void checkNumberOperands(Token operator, Object left, Object right){
        if(left instanceof Double && right instanceof Double)return;
        throw new RuntimeError(operator, " Los operandos tienen que ser numeros. ");

    }
    private void divisionbyZero(Token operator, Object left, Object right){
        if((double)right!=0)return;
        throw new RuntimeError(operator, " La division por cero no esta permitida.");
    }


    @Override
    public Object visitAssignExpr(Assign expr) {

        Object value = evaluate(expr.getValue());
        Integer distance = locals.get(expr);
        if(distance != null){
            env.assignAt(distance, expr.getName(), value);
        }else{
            globals.assign(expr.getName(), value);
        }

        return value;
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
            case MOD:
                checkNumberOperands(expr.getOperator(), left, right);
                return (double)left % (double)right;
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

        Object callee = evaluate(expr.getCallee());
        Token token = expr.getParen();
        List<Object> arguments = new ArrayList<>();
        for(Expr argument: expr.getArguments()){
            arguments.add(evaluate(argument));
        }
        SilkCallable function = (SilkCallable) callee;
        if(arguments.size() != function.arity() && !function.variadic()){
            throw new RuntimeError(expr.getParen(), "Se esperaba " +
                    function.arity() + " de argumentos pero se tuvo " +
                    arguments.size() + " . ");
        }
        if(!(callee instanceof SilkCallable)){
            throw new RuntimeError(expr.getParen(), " Solo se pueden llamar funciones y clases. ");
        }
        return function.call(this,arguments, token);
    }

    @Override
    public Object visitGetExpr(Get expr) {
        Object value = evaluate(expr.getObject());
        if(value instanceof SilkInstance){
            return ((SilkInstance)value).get(expr.getName());
        }
        throw new RuntimeError(expr.getName(), " Solo las instancias pueden tener propiedades. ");
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

        Object left = evaluate(expr.getLeft());

        if(expr.getOperator().getType() == TokenType.OR){
            if(isTruthy(left)){
                return left;
            }
        }else{
            if(!isTruthy(left)){
                return left;
            }
        }

        return evaluate(expr.getRight());
    }

    @Override
    public Object visitSetExpr(Set expr) {

        Object object = evaluate(expr.getObject());

        if(!(object instanceof  SilkInstance)){
            throw new RuntimeError(expr.getName(), "Solo las instancias tienen campos. ");
        }
        Object value = evaluate(expr.getValue());
        ((SilkInstance)value).set(expr.getName(), value );
        return value;
    }


    @Override
    public Object visitSuperExpr(Super expr) {
        int distance = locals.get(expr);
        SilkClass superclass = (SilkClass) env.getAt(distance, "super");
        SilkInstance object = (SilkInstance) env.getAt(distance - 1, "this");
        SilkFunction method = superclass.findMethod(expr.getMethod().getLexeme());
        if(method == null){
            throw new RuntimeError(expr.getMethod(), "Propiedad no definida: '"+expr.getMethod().getLexeme()+"'.");
        }
        return method.bind(object);
    }

    @Override
    public Object visitThisExpr(This expr) {

        return lookUpVariable(expr.getKeyword(), expr);
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
        return lookUpVariable(expr.getName(), expr);
    }

    @Override
    public Object visitInputExpr(Input in) {
        if(in != null){
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            return parseDouble(input);
        }

        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression expr) {
        evaluate(expr.getExpr());
        return null;
    }

    @Override
    public Void visitPrintStmt(Print pr) {
        Object value = evaluate(pr.getExpr());
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Var vr) {
        Object value = null;
        if(vr.getExpression()!= null){
            value = evaluate(vr.getExpression());
        }
        env.define(vr.getName().getLexeme(), value);
        return null;
    }

    @Override
    public Void visitBlockStmt(Block bl) {
        executeBlock(bl.getStatements(), new Environment(env));
        return null;
    }

    @Override
    public Void visitIfStmt(If ifstmt) {
        if(isTruthy(evaluate(ifstmt.getCondition()))){
            execute(ifstmt.getThenBranch());
        }else if(ifstmt.getElseBranch() != null){
            execute(ifstmt.getElseBranch());
        }
        return null;
    }

	 @Override
	 public Void visitWhileStmt(While wh) {
    	 while(isTruthy(evaluate(wh.getCondition()))){
    	 	 try {
				  execute(wh.getBody());
			 }catch (JumpError error){
    	 	 	 if(error.getType() == JumpType.BREAK){
    	 	 	 	 break;
				 }
			 }
		 }
		  return null;
	 }

	 @Override
	 public Void visitBreakStmt(Break br) {
		  throw new JumpError(JumpType.BREAK);
	 }

	 @Override
	 public Void visitContinueStmt(Continue ct) {
		  throw new JumpError(JumpType.CONTINUE);
	 }

    @Override
    public Void visitFunctionStmt(Function ft) {
        SilkFunction function = new SilkFunction(ft, env, false);
        env.define(ft.getName().getLexeme(), function);
        return null;
    }

    @Override
    public Void visitReturnStmt(Return rt) {
        Object value = null;
        if(rt.getValue() != null) value = evaluate(rt.getValue());
        throw new ReturnException(value);
    }

    @Override
    public Void visitClassStmt(SClass cl) {
        Object superclass = null;
        if(cl.getSuperclass()!= null){
            superclass = evaluate(cl.getSuperclass());
            if(!(superclass instanceof SilkClass)){
                throw new RuntimeError(cl.getSuperclass().getName(), "La superclase tiene que ser una clase. ");
            }
        }
        env.define(cl.getName().getLexeme(), null);
        if(cl.getSuperclass() != null){
            env = new Environment(env);
            env.define("super",superclass);
        }
        Map<String, SilkFunction> methods = new HashMap<>();
        for(Function method: cl.getFunctions()){
            SilkFunction ft = new SilkFunction(method, env, method.getName().getLexeme().equals("init"));
            methods.put(method.getName().getLexeme(), ft);
        }
        SilkClass silkClass = new SilkClass(cl.getName().getLexeme(), methods, (SilkClass) superclass);
        if(superclass!= null){
            env = env.getEnclosing();
        }
        env.assign(cl.getName(), silkClass);
        return null;
    }
}
