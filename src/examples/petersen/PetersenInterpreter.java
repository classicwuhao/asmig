/* Petersen Graph Interpreter
 * 
 * 04-FEB-2013	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */

package examples.petersen;

import java.io.*;
import java.util.*;
import atongmu.interpreter.*;
import atongmu.ast.Var;
import atongmu.util.Pair;
import atongmu.value.IntValue;

public final class PetersenInterpreter extends Interpreter{
	private Petersen pgraph;
	private String dotfile;
	private FileWriter filewriter;
	private PrintWriter out;

	public PetersenInterpreter(Petersen graph,String file){
		pgraph=graph;dotfile=file;
		try{
			filewriter = new FileWriter(file);
			out = new PrintWriter(filewriter);	
		}
		catch (Exception e){
			System.err.println("error: failed to interpret this solution.");
		}

		out.write("digraph instance{\n");
		out.write ("label=\""+"Petersen"+"\""+" fontsize=15" + " labelloc=top labeljust=l;");
		out.write("//objects to be shown \n");
		interpret();
		out.write("\n}//end of graph");
		out.flush();
		out.close();
	}
	
	public void interpret(){
	
		for (Var v : pgraph.getOutter())
			drawObjects(v.getName(),v.getType().toString(),choose(v));

		for (Var v : pgraph.getInner())
			drawObjects(v.getName(),v.getType().toString(),choose(v));

		for (Pair p:pgraph.getEdges())
			drawBdLinks(((Var)p.first()).getName(),((Var)p.second()).getName(),"");
	}

	private Color choose(Var v){
		IntValue iv = (IntValue)v.getValue();
		int k = iv.getValue();
		switch (k){
			case 1: return Color.RED;
			case 2: return Color.BLUE;
			case 3: return Color.GREEN;
			default:;
		}
		return Color.BLACK;
	}

	private void drawObjects(String name, String type, Color color){
		out.write(name+ " [shape=circle color="+Color.BLACK.toString()
				+" fillcolor="+color.toString()+" style=filled fontsize=11"
				+" label=\""+""+"\"" + "];\n");

	}

	private void drawBdLinks(String source, String dest, String label){
		out.write( source + "->" + dest + "[dir=none color=black fontcolor=black fontsize=9 label=\" "+label+" \"];\n");
	}
	

}
