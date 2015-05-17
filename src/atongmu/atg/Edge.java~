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
import atongmu.util.Pair;

public class Edge implements Comparable<Edge>{
	private Pair<Node,Node> pair;
	private Type type;
	private String name;
	private String info;  // optional for extra information about the edge.
	private boolean flag; // optional for extra usage.

	public Edge(){
		//default constructor
	}

	public Edge(String n, Node A, Node B){
		this(n,"",A,B,false);
	}

	public Edge(String n,String i, Node A, Node B, boolean f){
		name=n;
		info=i;
		flag=f;
		pair=new Pair<Node,Node>(A,B);
	}

	public Edge(Node A, Node B){
		pair=new Pair<Node, Node>(A,B);
	}
	
	public Node source(){
		return pair.first();
	}

	public Node dest(){
		return pair.second();
	}

	public void setNodes(Node n1, Node n2){
		pair.setFirst(n1);
		pair.setSecond(n2);
	}

	public void setInfo(String i){info=i;}
	public String getInfo(){return info;}
	public String getName(){return name;}

	public int compareTo(Edge e){
		if (e.source()==this.source() 
			&& e.dest()==this.dest())
			return 0;
		else if (e.source()!=this.source())
			return 1;
		else
			return -1;
	}

	public void setF(boolean b){flag=b;}
	public boolean getF(){return flag;}

	public String toString(){
		return (name!=null) ?
		name+":<" + pair.first().toString()+","+pair.second().toString()+">"
		:
		"No Name:<" + pair.first().toString()+","+pair.second().toString()+">";
	}
	
}
