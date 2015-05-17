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

import atongmu.value.Value;
import atongmu.value.BoolValue;
import atongmu.ast.visitor.AbstractVisitor;

public class BoolLiteral extends Constant{
	private BoolValue value;
	
	public BoolLiteral(){
		//default constructor
		value=new BoolValue();
	}

	public BoolLiteral(BoolValue v){
		value=v;
	}
		
	public BoolLiteral (boolean b){
		value=new BoolValue(b);
	}

	public boolean getLiteral(){
		return value.getValue();
	}
	
	public Value getValue(){
		return value;
	}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}

	public String toString(){
		return value.toString();	
	}
}
