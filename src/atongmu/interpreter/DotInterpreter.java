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
package atongmu.interpreter;

import java.io.*;
import java.util.*;
import org.eclipse.emf.ecore.*;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.ast.Var;
import atongmu.ast.StrVar;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.Type;
import atongmu.value.Value;
import atongmu.value.BoolValue;
import atongmu.value.IntValue;
import atongmu.translator.a2f;
import atongmu.translator.e2a;

public final class DotInterpreter extends Interpreter{
	private String dotfile;
	private a2f translator;
	private FileWriter filewriter;
	private PrintWriter out;
	private String graphlabel;	

	public DotInterpreter(String file, a2f t, String label){
		dotfile = file;
		translator = t;
		graphlabel=label;
		try{
			filewriter = new FileWriter(file);
			out = new PrintWriter(filewriter);	
		}
		catch (Exception e){
			System.err.println("error: failed to interpret this solution.");
		}
	}

	public void interpret(){
		head();
		interpretObjects();
		finish();
	}

	private void head(){
		out.write("digraph instance{\n");
		out.write("graph [autosize=false, size=\"(20,10)\",resolution=100];");
		out.write ("label=\""+graphlabel.toUpperCase()+"\""+" fontsize=15" + " labelloc=top labeljust=l;");
		out.write("//objects to be shown \n");
	}

	private void finish(){
		out.write("\n}//end of instance");
		out.flush();
		out.close();
	}

	private void interpretObjects(){
		HashMap<GraphNode, Var>	vars = translator.getObjects();
		HashMap<GraphNode, TreeSet<Edge>> attrs = translator.getAttributes();
		HashMap<DataNode,Var> data = translator.getData();
		HashMap<Edge, Var> links = translator.getLinks();
		List<GraphNode> nodes = new ArrayList<GraphNode>();
		HashMap<DataNode, StrVar> string = translator.getString();

		for (GraphNode node : vars.keySet()){
			Var var = vars.get(node);
			BoolValue value = (BoolValue)var.getValue(); //it has to be bool value...no other cases.
			if (value==null) continue;
			if (value.getValue()==true){
				nodes.add(node);
				drawObjects(var.getName(),node.getType().getName());
			}
		}

		for (int i=0;i<nodes.size();i++){
			GraphNode node = nodes.get(i);
			TreeSet<Edge> edges = attrs.get(node);
			if (edges==null) continue; /* get next edge */
			for (Edge e : edges){
				Var v = data.get(e.dest());
				if (v==null){
					/* try string */
					StrVar str = string.get(e.dest());
					if (str==null) continue; //throw new InterpreterException("error: cannot interprete attribute ["+e.dest()+"]");
					drawAttributes(str.getShortName(),
						str.toValue(),"String");
					drawAttrEdge(node.getName(),str.getShortName(),e.getName());
				}else{
					drawAttributes(v.getName(),
						v.getValue().toString(),v.getType().toString());
					drawAttrEdge(node.getName(),v.getName(),e.getName());
				}
			}
		}

		for (Edge e : links.keySet()){
			Var var = links.get(e);
			//System.out.print("Var:"+var.getName()+"->");
			BoolValue value = (BoolValue)var.getValue(); //each link is assigned with a boolean value.
			if (value==null) continue;
			if (value.getValue()==true){
				//System.out.println(e.source().getName()+","+e.dest().getName());
				if (!e.getF())
					drawUdLinks(e.source().getName(),e.dest().getName(),e.getInfo());
				else
					drawBdLinks(e.source().getName(),e.dest().getName(),e.getInfo());
			}
		}

	}

	private void drawObjects(String name, String type){
		out.write(name+ " [shape=box color="+Color.RED.toString()
				+" fillcolor="+Color.ORANGE.toString()+" style=filled fontsize=11"
				+" label=\""+name+":"+type+"\"];\n");
	}

	/* use a different color for drawing attributes */
	private void drawAttributes(String nodename, String name, String type){
		out.write(nodename+ " [shape=box color="+Color.BLACK
				+" fillcolor="+Color.GRAY75+" style=filled fontsize=11"
				+" label=\""+name+":"+type+"\"];\n");
	}
	
	private void drawAttrEdge(String source, String dest, String label){
		out.write(source + "->" + dest +
			"[arrowhead=vee arrowtail=vee style=filled fontsize=9 label="+"\" "+label+" \"];\n");
	}

	private void drawBdLinks(String source, String dest, String label){
		out.write( source + "->" + dest + "[dir=none color=blue fontcolor=blue fontsize=9 label=\" "+label+" \"];\n");
	}

	private void drawUdLinks(String source, String dest, String label){
		out.write( source + "->" + dest + "[arrowhead=vee arrowtail=vee style=filled"
			+ " color=darkgreen fontcolor=darkgreen fontsize=9 label=\" "+label+" \"];\n");
	}

}
