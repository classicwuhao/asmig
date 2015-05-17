/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  APR-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package atongmu.ast;
import atongmu.ast.visitor.AbstractVisitor;

//unary boolean expression
public class NegFun extends Expression{
	private Expression expr;
	private Connective conn;
	
	public NegFun(){
		//default constructor
		conn=Connective.NOT;
	}
	
	public NegFun(Expression e){
		conn=Connective.NOT;
		expr=e;
	}

	public Expression getExpression(){
		return expr;
	}

	public void setNegExpr(Expression e){
		expr=e;
	}

	public Connective getConnective(){
		return conn;
	}

	public String getConnectiveString(){
		return conn.toString();
	}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}
	
	public String toString(){
		return conn.toString()+"["+expr.toString()+"]";
	}
		
}
