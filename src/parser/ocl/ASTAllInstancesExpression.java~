package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTAllInstancesExpression extends ASTExpression {
    private Token fToken;

    public ASTAllInstancesExpression(Token token) {
        fToken = token;
    }

	public String token(){
		return fToken.getText();
	}

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
