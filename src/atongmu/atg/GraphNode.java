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
//Comparable<GraphNode>
public class GraphNode implements Node{
	private NodeType type;
	private String name;
		
	public GraphNode(String nodename, NodeType t){
		name = nodename;
		type = t;
	}

	public String getName(){return name;}
	public NodeType getType(){return type;}

	public void setType(NodeType t){
		type=t;
	}

	public String toString(){
		return name+":"+type.toString();
	}

	public int compareTo(Node n){
		if (name.hashCode()==n.hashCode())
			return 0;
		else if(name.hashCode()>n.hashCode())
			return -1;
		else
			return 1;
	}

}
