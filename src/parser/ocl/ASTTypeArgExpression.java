package parser.ocl;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTTypeArgExpression extends ASTExpression {
    private Token fOpToken;
    private ASTExpression fSourceExpr; // may be null
    private ASTType fTargetType;
    private boolean fFollowsArrow;

    public ASTTypeArgExpression(Token opToken, 
                                ASTExpression source, 
                                ASTType targetType,
                                boolean followsArrow) {
        fOpToken = opToken;
        fSourceExpr = source;
        fTargetType = targetType;
        fFollowsArrow = followsArrow;
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
