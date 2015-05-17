package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTInvariantClause extends ASTExpression {
	Token fName;      // optional
    ASTExpression fExpr;

    public ASTInvariantClause(Token name, ASTExpression e) {
        fName = name;
        fExpr = e;
    }

	public String getName(){
		return fName.getText();
	}

    public String toString() {
        return fExpr.toString();
    }

	public ASTExpression getExpression(){
		return fExpr;
	}

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}
}
