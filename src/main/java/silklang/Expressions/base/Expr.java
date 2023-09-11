package silklang.Expressions.base;

public abstract class Expr {
    public abstract <T> T accept(Visitor<T> visitor);
}
