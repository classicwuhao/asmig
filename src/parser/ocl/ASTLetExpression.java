package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTLetExpression extends ASTExpression {
    private Token fVarToken;
    private ASTType fVarType;   // optional: may be null
    private ASTExpression fVarExpr;
    private ASTExpression fInExpr;

    public ASTLetExpression(Token varToken, 
                            ASTType type,
                            ASTExpression varExpr) {
        fVarToken = varToken;
        fVarType = type;
        fVarExpr = varExpr;
    }

    public void setInExpr(ASTExpression inExpr) {
        fInExpr = inExpr;
    }   

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
