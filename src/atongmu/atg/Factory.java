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
import java.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.common.util.*;
import atongmu.translator.Bound;

/*
 * Factory for making your bounded graph objects.
 */
public final class Factory{	

	private static HashMap<String,TreeSet<GraphNode>> VG = new HashMap<String,TreeSet<GraphNode>>();   //unbounded VG, disabled.
	private static HashMap<String,TreeSet<Edge>> EG = new HashMap<String,TreeSet<Edge>>();
	private static HashMap<EClass,TreeSet<GraphNode>> BVG = new HashMap<EClass,TreeSet<GraphNode>>(); //bounded VG
	private static HashMap<GraphNode, TreeSet<Edge>> ENA = new HashMap<GraphNode,TreeSet<Edge>>(); //data nodes for Int,Bool and Real
	private static HashMap<DataNode, EEnum> ED = new HashMap<DataNode,EEnum>(); //data nodes for enum type
	private static HashMap<DataNode, EAttribute> EA = new HashMap<DataNode, EAttribute>();// data nodes for string
	private static LinkedHashMap<EReference, TreeSet<Edge>> ST = new LinkedHashMap<EReference,TreeSet<Edge>>(); //source & target functions
	private static TreeSet<GraphNode> empty = new TreeSet<GraphNode>();
	private static int datacount = 0;
	private static int nodecount = 0;
	private static int edgecount = 0;
	private Bound bound; // bound information
	
	/* a creation of a factory without a bound is not allowed */
	private Factory(){}

	/* Everything has to be assigned with a bound */
	public Factory(Bound b){
		if (b==null)
			bound = Bound.DEFAULT;
		else
			bound = b;
	}

	public GraphNode makeGraphNode(String name, String name1){
		return new GraphNode(name, makeNodeType(name1));
	}

	/* NOTE: this creates a node with no respect to bound,
     * TODO: it will be disabled.
     */
	public void makeGraphNode(String clsname, int times){
		TreeSet<GraphNode> s;
		if (!VG.containsKey(clsname))
			s = new TreeSet<GraphNode>();
		else
			s = VG.get(clsname);

		for (int i=0, k=s.size();i<times;i++,nodecount++)
			s.add(makeGraphNode(clsname.toLowerCase()+(k+i+1),clsname));

		VG.put(clsname,s);
	}
	
	/* bound has to be set */
	public void makeGraphNode(EClass cls, boolean isempty){
		TreeSet<GraphNode> s;
		NONEMPTY:{

		if (isempty){
			s = empty;
			break NONEMPTY; 
		}	

		if (!BVG.containsKey(cls))
			s = new TreeSet<GraphNode>();
		else
			s = BVG.get(cls);
		
		for (int i=0;i<bound.getBound(cls);i++,nodecount++)
			s.add(makeGraphNode(cls.getName().toLowerCase()+(i+1),cls.getName()));

		}

		BVG.put(cls,s);
	}

	public NodeType makeNodeType(String name, long typeid){
		return new NodeType(name,typeid);
	}
	
	public NodeType makeNodeType(String typename){
		return new NodeType(typename, typename.hashCode());
	}

	public Edge makeNodeAttribute(GraphNode node, DataNode data){
		return new Edge(node,data);
	}

	public void makeNodeAttribute(EClass cls, String attrname, PrimitiveType type){
		TreeSet<Edge> e;
		TreeSet<GraphNode> n;

		if (!BVG.containsKey(cls)){
			//throw an error: no corresponding graph node found.
  			//it should allow program to continue.
			System.err.println("error: no graph node found.");
			return;
		}
		
		//get BVG
		n = BVG.get(cls);
		Iterator<GraphNode> it = n.iterator();
		
		for (GraphNode node=null;it.hasNext();){
			node = it.next();
			if (ENA.containsKey(node))
				e = ENA.get(node);
			else
				e = new TreeSet<Edge>();
			
			DataNode data = new DataNode(attrname+String.valueOf(datacount++),type,attrname);
			e.add(new Edge(attrname,node,data));
			edgecount++;
			ENA.put(node,e);
		}
	}

	public void makeNodeAttribute(EClass cls, EAttribute attr, String attrname, PrimitiveType type){
		TreeSet<Edge> e;
		TreeSet<GraphNode> n;

		if (!BVG.containsKey(cls)){
			//throw an error: no corresponding graph node found.
  			//it should allow program to continue.
			System.err.println("error: no graph node found.");
			return;
		}
		
		//get BVG
		n = BVG.get(cls);
		Iterator<GraphNode> it = n.iterator();
		
		for (GraphNode node=null;it.hasNext();){
			node = it.next();
			if (ENA.containsKey(node))
				e = ENA.get(node);
			else
				e = new TreeSet<Edge>();
			
			DataNode data = new DataNode(attrname+String.valueOf(datacount++),type,attrname);
			EA.put(data,attr);
			e.add(new Edge(attrname,node,data));
			edgecount++;
			ENA.put(node,e);
		}
	}

	public void makeNodeAttribute(EClass cls, EEnum en,String attrname, PrimitiveType type){
		TreeSet<Edge> e;
		TreeSet<GraphNode> n;

		if (!BVG.containsKey(cls)){
			//throw an error: no corresponding graph node found.
  			//it should allow program to continue.
			System.err.println("error: no graph node found.");
			return;
		}
		
		//get BVG
		n = BVG.get(cls);
		Iterator<GraphNode> it = n.iterator();
		
		for (GraphNode node=null;it.hasNext();){
			node = it.next();
			if (ENA.containsKey(node))
				e = ENA.get(node);
			else
				e = new TreeSet<Edge>();
			
			DataNode data = new DataNode(attrname+String.valueOf(datacount++),type,attrname);
			ED.put(data,en);
			e.add(new Edge(attrname,node,data));
			edgecount++;
			ENA.put(node,e);
		}
	}

	
	/**
     * NOTE: this method does not take care of clsa and clsb's descendants.
     * To look after descendants of clsa and clsb, use the overloading makeSrcDest.
     */
	public void makeSrcDest(EReference ref, EClass clsa, EClass clsb){
		TreeSet<GraphNode> seta;
		TreeSet<GraphNode> setb;
		TreeSet<Edge> setc;
		
		if (!BVG.containsKey(clsa) || !BVG.containsKey(clsb))
			throw new FactoryException("error: cannot perform this operation on the empty set.");
		
		seta = BVG.get(clsa);
		setb = BVG.get(clsb);
		
		if (!ST.containsKey(ref))
			setc = new TreeSet<Edge>();
		else
			setc = ST.get(ref);
		
		for (GraphNode node1 : seta)
			for (GraphNode node2 : setb)
				setc.add(new Edge("e"+edgecount++,node1,node2));
		
		ST.put(ref,setc);
	}

	public void makeSrcDest(EReference ref, List<EClass> list1, List<EClass> list2, String name, boolean bi){
		List<EClass> conlist1 = new ArrayList<EClass>();
		List<EClass> conlist2 = new ArrayList<EClass>();
		TreeSet<GraphNode> seta = new TreeSet<GraphNode>();
		TreeSet<GraphNode> setb = new TreeSet<GraphNode>();
		TreeSet<Edge> setc = new TreeSet<Edge>();
		
		//if (!ST.containsKey(ref))
		//	throw new FactoryException("error: the reference <"+ref.getName()+"> is missing.");
		
		for (int i=0;i<list1.size();i++){
			EClass clsa = list1.get(i);
			if (!BVG.containsKey(clsa))
				throw new FactoryException("error: expect <"+clsa.getName()+"> to be found ["+ref.getName()+"].");
			if (!clsa.isAbstract())
				conlist1.add(clsa);
		}

		for (int i=0;i<list2.size();i++){
			EClass clsb = list2.get(i);
			if (!BVG.containsKey(clsb))
				throw new FactoryException("error: expect "+clsb.getName()+" to be found ["+ref.getName()+"].");
			if (!clsb.isAbstract())
				conlist2.add(clsb);
		}
		
		/* at this stage, if the list is empty then this object is def abstract. */
		if (conlist1.size()==0 || conlist2.size()==0) 
			throw new FactoryException("error: cannot perform this operation on abstract objects ["+ref.getName()+"].");

		for (int i=0;i<conlist1.size();i++) seta.addAll(BVG.get(conlist1.get(i)));
		for (int i=0;i<conlist2.size();i++) setb.addAll(BVG.get(conlist2.get(i)));			
		
		for (GraphNode nodea : seta){
			for (GraphNode nodeb : setb){
				setc.add(new Edge("e"+edgecount++,name,nodea,nodeb,bi)); //add extra information about reference.
			}
		}
		
		ST.put(ref,setc);
	}

	public String[] getEnum(DataNode data){
		if (!ED.containsKey(data))
			throw new FactoryException("error: cannot find enum type for "+data);

		EList<EEnumLiteral> literals = ED.get(data).getELiterals();
		String[] Literals = new String[literals.size()];
		int i=0;
		
		for (EEnumLiteral l : literals)
			Literals[i++] = l.getLiteral();

		return Literals;
	}

	public List<GraphNode> getGraphNode(String typename){
		List<GraphNode> nodes = new ArrayList<GraphNode>();
		
		for (EClass cls : BVG.keySet())	{
			if (cls.getName().compareTo(typename)==0){
				nodes.addAll(BVG.get(cls));
				return nodes;
			}
		}
		
		return nodes;
	}

	public int getStringBound(DataNode data){
		if (!EA.containsKey(data))
			throw new FactoryException("error: cannot find string type for "+data);

		return bound.getBound(EA.get(data));
	}

	public boolean existsClass(String clsname){
		return VG.containsKey(clsname);
	}

	public DataNode makeDataNode(String name, NodeType type){
		return new DataNode(name, type,"");
	}
	
	public DataNode makeDataNode(String name, PrimitiveType type){
		return new DataNode(name, type,"");
	}

	public HashMap<EClass,TreeSet<GraphNode>> getBVG(){
		return BVG;
	}

	public HashMap<GraphNode, TreeSet<Edge>> getENA(){
		return ENA;
	}

	public LinkedHashMap<EReference, TreeSet<Edge>> getST(){
		return ST;
	}

	public TreeSet<Edge> getEdgesByName(String name){
		for (EReference ref : ST.keySet())
			if (ref.getName().compareTo(name)==0)
				return ST.get(ref);
		return null;
	}

	public EReference getRef(String name){
		for (EReference ref : ST.keySet())
			if (ref.getName().compareTo(name)==0)
				return ref;

		return null;
	}
	
	public TreeSet<Edge> getDataNodes(GraphNode node){
		if (node==null) throw new FactoryException("Error: graph node is null.");
		if (!ENA.containsKey(node)) throw new FactoryException("Error: cannot retrieve data nodes for "+node);

		return ENA.get(node);
	}

	public static String VG2String(){
		StringBuffer sb = new StringBuffer();
		for (EClass key : BVG.keySet()){
			TreeSet<GraphNode> s = BVG.get(key); //it has to be TreeSet<GraphNode> for safe purpose.
			sb.append("Type:"+key.getName()+"->{");
			Iterator<GraphNode> it= s.iterator();
			while (it.hasNext()){
				GraphNode n = it.next();
				sb.append(n.toString()+",");
			}
			sb.append("}\n");
		}
		return sb.toString();
	}

	public static String ENA2String(){
		StringBuffer sb = new StringBuffer();
		for (GraphNode key : ENA.keySet()){
			TreeSet<Edge> s = ENA.get(key); //it has to be TreeSet<Edge> for safe purpose.
			sb.append(key.toString()+"->{");
			Iterator<Edge> it = s.iterator();
			while (it.hasNext()){
				Edge e = it.next();
				sb.append(e.toString()+",");
			}
			sb.append("}\n");
		}
		
		return sb.toString();
	}

	public static String ST2String(){
		StringBuffer sb = new StringBuffer();
		for (EReference ref : ST.keySet()){
			sb.append(ref.getName()+"->{");
			TreeSet<Edge> s = ST.get(ref);
			for (Edge e : s)
				sb.append(e.toString()+",");
			sb.append("}\n");
		}
		return sb.toString();
	}
	
	public String toString(boolean detailed){
		StringBuffer sb = new StringBuffer();		
		if (detailed)
			sb.append(VG2String()+"\n"+ENA2String()+"\n"+ST2String()+"\n");
		sb.append("===============================================\n");
		sb.append("Total Class(es) translated:"+BVG.size()+"\n");
		sb.append("Total Attribute(s) translated:"+ENA.size()+"\n");
		sb.append("Total Reference(s) translated:"+ST.size()+"\n");
		sb.append("\n result in total \n\n");
		sb.append("Node:"+nodecount+"\n");
		sb.append("Data:"+datacount+"\n");
		sb.append("Edge:"+edgecount+"\n");
		
		return sb.toString();
	}

}
