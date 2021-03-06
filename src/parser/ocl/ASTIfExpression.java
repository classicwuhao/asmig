package parser.ocl;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTIfExpression extends ASTExpression {
    private Token fIfToken;
    private ASTExpression fCond;
    private ASTExpression fThen;
    private ASTExpression fElse;

    public ASTIfExpression(Token ifToken,
                           ASTExpression cond,
                           ASTExpression t, 
                           ASTExpression e) {
        fIfToken = ifToken;
        fCond = cond;
        fThen = t;
        fElse = e;
    }

    public String toString() {
        return "ASTifExpression: (" + fCond + " " + fThen + " " + fElse + ")";
    }
	
	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}
	

}
