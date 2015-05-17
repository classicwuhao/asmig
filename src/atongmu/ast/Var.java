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
import atongmu.err.VarExistsException;
import atongmu.err.VarNullException;
import atongmu.ast.visitor.AbstractVisitor;

public class Var extends Expression{
	private Type type;
	private String name;
	private Value value;
	private boolean fixed;

	public Var(){
		//default constructor
	}

	public Var(String n, Type t){
		try{
			type=t;
			name=n;
			fixed=false;
			Vars.addVar(this);
		}
		catch (VarExistsException e){
			e.printErrMessage(name);
		}
		catch (VarNullException e){
			e.printErrMessage(name);
		}
	}

	public Var(String n, Type t, Value v){
		try{
			type=t;
			name=n;
			value=v;
			fixed=false;
			Vars.addVar(this);
		}
		catch (VarExistsException e){
			e.printErrMessage(name);
		}
		catch (VarNullException e){
			e.printErrMessage(name);
		}
	}

	public String getName(){return name;}
	public void setName(String n){
		//update var;
		Vars.removeVar(this);
		name=n;
		try{
			Vars.addVar(this);
		}
		catch (VarExistsException e){
			e.printErrMessage(name);
		}
		catch (VarNullException e){
			e.printErrMessage(name);
		}
		
	}
	public Type getType(){return type;}
	public void setType(Type t){type=t;}
	public Value getValue(){return value;}
	public void setValue(Value v){
		if (v==null)
			throw new FormulaException("cannot not set a null value.");
		value=v;
	}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}

	public boolean isVar(){return true;}

	public boolean isIntType(){
		return type.isIntType();
	}

	public boolean isBoolType(){
		return type.isBoolType();
	}
	
	public boolean isEnumType(){return type.isEnumType();}

	public String toString(){
		if (value!=null)
			return "<"+name+":"+type.toString()+"="+value.toString()+">";
		else
			return "<"+name+":"+type.toString()+">";
	}

	public boolean isFixed(){return fixed;}
	public void setFix(boolean t){fixed=t;}
}
