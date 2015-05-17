/**
 *	Coloring a petersen graph
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 *
 */

package examples.petersen;

import java.util.List;
import java.util.ArrayList;
import atongmu.ast.Var;
import atongmu.ast.ConstDecl;
import atongmu.type.IntType;
import atongmu.util.Pair;

public final class Petersen{
	
	private List<Var> outter = new ArrayList<Var>();	
	private List<Var> inner = new ArrayList<Var>();
	private List<Pair<Var,Var>> edges = new ArrayList<Pair<Var,Var>>();
	private int vertices=0;
	private final int outter_vertices=5;
	private final int inner_vertices=5;

	public Petersen(){connect();}

	private void connect(){
		for (int i=0;i<outter_vertices;i++)
			outter.add(createVertex());

		for (int i=0;i<inner_vertices;i++)
			inner.add(createVertex());

		/* build outter connection */
		edges.add(new Pair(outter.get(0),outter.get(1)));
		edges.add(new Pair(outter.get(0),outter.get(2)));
		edges.add(new Pair(outter.get(1),outter.get(3)));
		edges.add(new Pair(outter.get(2),outter.get(4)));
		edges.add(new Pair(outter.get(3),outter.get(4)));

		/* build inner connection */
		edges.add(new Pair(inner.get(0),inner.get(3)));
		edges.add(new Pair(inner.get(0),inner.get(4)));
		edges.add(new Pair(inner.get(1),inner.get(2)));
		edges.add(new Pair(inner.get(2),inner.get(3)));
		edges.add(new Pair(inner.get(1),inner.get(4)));
		
		/* build outter-inner connection */
		edges.add(new Pair(outter.get(0),inner.get(0)));
		edges.add(new Pair(outter.get(1),inner.get(1)));
		edges.add(new Pair(outter.get(3),inner.get(3)));
		edges.add(new Pair(outter.get(4),inner.get(4)));
		edges.add(new Pair(outter.get(2),inner.get(2)));
	}

	private Var createVertex(){
		Var v = new Var("v"+vertices++,new IntType());
		ConstDecl c = new ConstDecl(v);
		return v;
	}

	public List<Var> getOutter(){return outter;}
	public List<Var> getInner(){return inner;}
	public List<Pair<Var,Var>> getEdges(){return edges;}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<edges.size();i++)
			sb.append("("+edges.get(i).first()+","+edges.get(i).second()+")\n");
		
		return sb.toString();
	}
	
}
