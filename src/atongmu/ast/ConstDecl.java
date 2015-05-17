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
import atongmu.err.DuplicatedDeclaration;
import atongmu.ast.visitor.AbstractVisitor;

public class ConstDecl extends Declaration{
	private Var pvar;
	public ConstDecl(){//default constrctor
	}

	public ConstDecl(Var var){
		super(var.getName(),var.getType(),null);
		pvar=var;
		try{
			Vars.addDecl(this,var);
		}
		catch(DuplicatedDeclaration e){
			e.printErrMessage(var.getName());
		}
	}

	public ConstDecl(String n, Type t){
		super(n,t,null);
		pvar= new Var(n,t);
		
	}
	
	public ConstDecl(String n, Type t, Value v){
		super(n,t,v);	
		pvar=new Var(n,t,v);
	}

	public Var getVar(){return pvar;}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}
	
	public String toString(){
		return (super.value==null) ?
		"declare-const<"+name+":"+type.toString()+">" :
		"<"+name+":"+type.toString()+"->"+value.toString()+">";
	}
}
