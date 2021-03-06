package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTCollectionLiteral extends ASTExpression {
    private Token fToken;
    private List fItems;    // (ASTCollectionItem)
    private boolean fHasRanges;

    public ASTCollectionLiteral(Token token) {
        fToken = token;
        fItems = new ArrayList();
    }

    public void addItem(ASTCollectionItem item) {
        fItems.add(item);
        if (item.fSecond != null )
            fHasRanges = true;
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}
	// @FIX ME
    public String toString() {
		/*This is hacked by hao wu*/
        //return "ASTCollectionLiteral: (" + fToken + " " + 
         //   StringUtil.fmtSeq(fItems.iterator(), " ") + ")";
		return "@ASTCollectionLiteral";
    }
}
