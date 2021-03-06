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
import atongmu.type.*;
import atongmu.value.*;
import atongmu.ast.visitor.AbstractVisitor;

public class Argument extends Expression{
	Type type;
	Value value; //optional
	String name;
	
	public Argument(){
		//default constructor
	}
	
	public Argument(Type t, String n){
		type=t;
		name=n;
	}

	public Argument(Type t, Value v, String n){
		type=t;
		value=v;
		name=n;
	}

	public Argument(Var var){
		type=var.getType();
		name=var.getName();	
	}

	public Type getType(){return type;}
	public void setType(Type t){type=t;}
	public Value getValue(){return value;}
	public void setValue(Value v){value=v;}
	public String getName(){return name;}
	public void setName(String n){name=n;}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);			
	}

	public String toString(){
		return (value !=null) ? 
		"<"+ name+","+type+","+value+">" :
		"<"+ name+","+type+">";
	}
}
