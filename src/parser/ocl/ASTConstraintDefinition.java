package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTConstraintDefinition extends ASTExpression {
    private List fVarNames;   // optional
    private ASTType fType;
    private ArrayList fInvariantClauses; // (ASTInvariantClause)

    public ASTConstraintDefinition() {
    	fVarNames = new ArrayList();
    	fInvariantClauses = new ArrayList();
    }

    public void addInvariantClause(ASTInvariantClause inv) {
        fInvariantClauses.add(inv);
    }

    public void addVarName(Token tok) {
        fVarNames.add(tok);
    }

    public void setType(ASTType t) {
        fType = t;
    }

	public ASTType getType(){return fType;}

	public List<ASTInvariantClause> getInvs(){
		return fInvariantClauses;
	}

	public List<Token> getVars(){
		return fVarNames;
	}

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> V accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

}
