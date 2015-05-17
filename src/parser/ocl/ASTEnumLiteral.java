package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTEnumLiteral extends ASTExpression {
    private Token fValue;
	private Token fType;

    public ASTEnumLiteral(Token token) {
        fValue = token;
    }

	public ASTEnumLiteral(Token token0, Token token1){
		fType = token0;
		fValue = token1;
	}

    public String toString() {
	/*This is hacked by Hao Wu*/
        return "ASTEnumLiteral: "+fValue.getText();
    }

	public String getType(){
		return fType.getText();
	}

	public String getLiteral(){
		return fValue.getText();
	}

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
