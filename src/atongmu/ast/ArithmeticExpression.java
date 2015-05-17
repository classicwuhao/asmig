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
/**
 * Written by Hao Wu
 * NOTE: This is part of my phd work.
 * Bug reports to: haowu@cs.nuim.ie
 * 
 * April-2012
 */
import java.util.*;
import atongmu.ast.visitor.AbstractVisitor;

public class ArithmeticExpression extends Expression{
	private Arithmetic operator;
	private Expression operand1;
	private Expression operand2;
	
	public ArithmeticExpression(Arithmetic o, Expression e1, Expression e2){
		operator=o;
		operand1=e1;		
		operand2=e2;
	}

	public Expression getOperand1(){return operand1;}	
	public Expression getOperand2(){return operand2;}
	
	public String getOperator2String(){return operator.toString();}
	public Arithmetic getOperator(){return operator;}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}

	public String toString(){
		return operator.toString()+"["+operand1.toString()+","+operand2.toString()+"]";
	}
		
}
