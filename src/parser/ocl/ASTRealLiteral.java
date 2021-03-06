package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTRealLiteral extends ASTExpression {
    private double fValue;

    public ASTRealLiteral(Token token) {
        fValue = Double.valueOf(token.getText()).doubleValue();
    }

    public String toString() {
	/*This is hacked by Hao Wu*/
        return "ASTRealLiteral: "+Double.toString(fValue);
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
