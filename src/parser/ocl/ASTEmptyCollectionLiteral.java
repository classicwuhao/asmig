package parser.ocl;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTEmptyCollectionLiteral extends ASTExpression {
    private ASTType fType;

    public ASTEmptyCollectionLiteral(ASTType t) {
        fType = t;
    }


	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
