package parser.ocl;

import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;

import atongmu.ast.Expression;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;

public abstract class ASTExpression extends AST {

    // first token of expression useful for error reporting
    private Token fStartToken; 
	private boolean fIsPre;
	public ASTContext context;	
	
    public void setIsPre() {
        fIsPre = true;
    }

    public boolean isPre() {
        return fIsPre;
    }

	//public ASTContext context(){return fContext;}
	//public void setContext(ASTContext newcontext){fContext=newcontext;}

    public void setStartToken(Token pos) {
        fStartToken = pos;
    }

    public Token getStartToken() {
        return fStartToken;
    }
	
	public abstract void accept(PrintVisitor v);
	public abstract <E,LN,LE,V> Object accept(ReturnVisitor<E,LN,LE,V> v);
}
