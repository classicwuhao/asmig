package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTBinaryExpression extends ASTExpression {
    private Token fToken;
    private ASTExpression fLeft;
    private ASTExpression fRight;

    public ASTBinaryExpression(Token token, 
                               ASTExpression left, 
                               ASTExpression right) {
        fToken = token;
        fLeft = left;
        fRight = right;
    }

	public ASTExpression left(){return fLeft;}
	public ASTExpression right(){return fRight;}
	public Token getToken(){return fToken;}

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}	

    public String toString() {
	return "Left: "+fLeft+"\n" +
		"Token:" + fToken+"\n" +
		"Right:"+fRight+"\n";

    }

}
