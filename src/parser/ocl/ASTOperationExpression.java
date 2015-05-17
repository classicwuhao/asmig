package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;
import atongmu.atg.Node;

public class ASTOperationExpression extends ASTExpression {
    private Token fOp;
    private ASTExpression fSrcExpr;
    private List fArgs;
    private boolean fHasParentheses;
    private boolean fFollowsArrow;
	private boolean reference;
    private Token fExplicitRolename;

    public ASTOperationExpression(Token op, 
                                  ASTExpression source, 
                                  boolean followsArrow) {
        fOp = op;
        fSrcExpr = source;
        fArgs = new ArrayList();
        fHasParentheses = false;
		reference =false;
        fFollowsArrow = followsArrow;
    }

    public void addArg(ASTExpression arg) {
        fArgs.add(arg);
    }

	public boolean getRef(){
		return reference;
	}	
	public void setRef(){reference=true;}
    public void hasParentheses() {
        fHasParentheses = true;
    }

    public void setExplicitRolename( Token rolename ) {
        fExplicitRolename = rolename;
    }

	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> LN accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

	public boolean IsAttrOperation(){
		return fFollowsArrow ? false : true;
	}

	public List<ASTExpression> args(){return fArgs;}
	public ASTExpression source(){return fSrcExpr;}
	public Token operator(){return fOp;}
	public boolean IsSelfOp(){return fOp.getText().compareTo("self")==0 ? true : false; }

    /*
      operation may be one of: 
      (1) predefined OCL operation
      (2) attribute operation on object type (no arguments)
      (3) "isQuery" operation on object type (possibly with arguments)
      (4) navigation operation on object type
      (5) shorthand for collect
      (6) set operation on single object resulting from
      navigation over associations with multiplicity zero or one
      (p. 7-13 of OMG UML 1.3)
      (7) variable

      possible combinations of syntax:
      source expression:
      none or
      s: simple value
      o: object
      c: collection

      s.op    110 (1)
      s.op()  111 (1)
      s->op   120 (1) with warning
      s->op() 121 (1) with warning
        
      o.op    210 (2,4,1)
      o.op[nav]  1210 (4)
      o.op()  211 (3,1)
      o->op   220 (6) if s has object type and results from navigating 0..1 end
      o->op() 221 (6) to allow uniform syntax of operation calls
        
      c.op    410 (5) with implicit (2,4,1)
      c.op()  411 (5) with implicit (3,1)
      c->op   420 (1)
      c->op() 421 (1)

      op      000 (2,4,7)
      op()    001 (1,3)
    */

    public String toString() {
	/*This is hacked by Hao Wu*/
		/*StringBuffer sb = new StringBuffer();
		for (int i=0;i<fArgs.size();i++)
			sb.append(fArgs.get(i).toString()+"\n");

		return sb.toString();*/
		return "ASTOperationExpression: (" + fOp + ")" ;
		//StringUtil.fmtSeq(fArgs.iterator(), " ") + ")";
    }
}
