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
package atongmu.atg;
import atongmu.util.*;
import atongmu.value.*;

public class DataNode implements Node {
	NodeType type;
	PrimitiveType ptype;
	String name;
	Value value;
	String attr;

	public DataNode(String dataname){
		name = dataname;
	}

	public DataNode(String d, NodeType t,String attribute){
		name=d;
		type=t;
		attr=attribute;
		ptype=null;
	}
	
	public DataNode(String d, PrimitiveType t,String attribute){
		name=d;
		ptype=t;
		attr=attribute;
		type=null;	
	}

	public String getAttrName(){
		return attr;
	}

	public String getName(){
		return name;
	}

	public NodeType getType(){
		return type;
	}

	public PrimitiveType getPType(){
		return ptype;
	}

	public Value getValue(){
		return value;
	}

	public int compareTo(Node d){
		return name.compareTo(d.getName());
	}

	public String toString(){		
		return (type!=null) ?
			name+":"+type.toString() 
			:
			name+":"+ptype.toString();
	}
}
