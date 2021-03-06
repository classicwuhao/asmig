package parser.ocl;

import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTBooleanLiteral extends ASTExpression {
    private boolean fValue;

    public ASTBooleanLiteral(boolean val) {
        fValue = val;
    }

	public boolean value(){return fValue;}

    public String toString() {
        return fValue ? "ASTBooleanLiteral: true" : "ASTBooleanLiteral: false";
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
