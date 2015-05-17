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
 * This software is dedicated to those who support give me great support during my PhD studies in NUI, Maynooth.
 * NOTE: This is part of my phd work.
 * Bug reports to: haowu@cs.nuim.ie
 * 
 */
import java.util.*;
import atongmu.ast.visitor.AbstractVisitor;

public class FunExpression extends Expression{
	protected List <Argument> arguments; //arguments;
	protected int num; //number of arguments;
	protected String name; //fun name;
	protected String getName(){return name;}
	protected void setName(String n){name=n;}

	public FunExpression(String n, Argument args[]){
		if (args==null){
			// throw exception
		}
		num = args.length;
		name = n;
		arguments = new ArrayList<Argument>();
		for (int i=0;i<num;i++)
			arguments.add(args[i]);
	}

	public void accept(AbstractVisitor visitor){
		visitor.visit(this);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append("[\n");
		for (int i=0;i<num;i++)
			sb.append(arguments.get(i).toString());
		sb.append("]\n");
		return sb.toString();
	}
}
