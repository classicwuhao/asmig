package parser.ocl;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTIterateExpression extends ASTExpression {
    private Token fIterateToken;
    private ASTExpression fRange; // may be null
    private ASTElemVarsDeclaration fDeclList;
    private ASTVariableInitialization fInit;
    private ASTExpression fExpr;

    public ASTIterateExpression(Token iterateToken,
                                ASTExpression range, 
                                ASTElemVarsDeclaration declList,
                                ASTVariableInitialization init,
                                ASTExpression expr) {
        fIterateToken = iterateToken;
        fRange = range;
        fDeclList = declList;
        fInit = init;
        fExpr = expr;
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

    public String toString() {
        return "ASTIterateExpression: (\"iterate\" " + fRange + " " + fDeclList + " " + fInit + " " + fExpr + ")";
    }

}
