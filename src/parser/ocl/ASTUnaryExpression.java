package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTUnaryExpression extends ASTExpression {
    private Token fToken;
    private ASTExpression fExpr;

    public ASTUnaryExpression(Token token, 
                              ASTExpression expr) {
        fToken = token;
        fExpr = expr;
    }

    public String toString() {
        return "ASTUnaryExpression: (" + fToken + " " + fExpr + ")";
    }

	public String operator(){return fToken.getText();}

	public boolean IsNot(){
		return fToken.getText().compareTo("not")==0;
	}

	public ASTExpression expr(){return fExpr;}

	public void accept(PrintVisitor v){
		v.visit(this);
	}
	
	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
