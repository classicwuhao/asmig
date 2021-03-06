package parser.ocl;

import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTQueryExpression extends ASTExpression {
    private Token fOp;
    private ASTExpression fRange; // may be null
    private ASTElemVarsDeclaration fDeclList;
    private ASTExpression fExpr;

    public ASTQueryExpression(Token op, 
                              ASTExpression range, 
                              ASTElemVarsDeclaration declList,
                              ASTExpression expr) {
        fOp = op;
        fRange = range;
        fDeclList = declList;
        fExpr = expr;
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LE accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}	

	public ASTExpression range(){
		return fRange;
	}

	public ASTElemVarsDeclaration vars(){
		return fDeclList;
	}
	
	public ASTExpression expr(){
		return fExpr;
	}

	public boolean IsExists(){
		return fOp.getText().compareTo("exists")==0;
	}

	public boolean IsForAll(){
		return fOp.getText().compareTo("forAll")==0;
	}

	public boolean IsSelect(){
		return fOp.getText().compareTo("select")==0;
	}

	public boolean IsUnique(){
		return fOp.getText().compareTo("isUnique")==0;
	}

	public Token operator(){
		return fOp;
	}

    public String toString() {
        return "ASTQueryExpression: (" + fOp + " " + fRange + " " + fDeclList + " " + fExpr + ")";
    }
}
