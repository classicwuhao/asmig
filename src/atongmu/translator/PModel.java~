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
package atongmu.translator;

import java.util.*;
import atongmu.ast.Argument;
import atongmu.ast.BinaryExpression;
import atongmu.ast.Constant;
import atongmu.ast.ConstDecl;
import atongmu.ast.Declaration;
import atongmu.ast.FunExpression;
import atongmu.ast.NegFun;
import atongmu.ast.Var;
import atongmu.ast.NumLiteral;
import atongmu.ast.BoolLiteral;
import atongmu.ast.ArithmeticExpression;
import atongmu.ast.Connective;
import atongmu.ast.Arithmetic;
import atongmu.ast.FOFormula;
import atongmu.ast.Expression;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.Type;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;
import atongmu.value.BoolValue;

public class PModel{
	Criteria criteria;
	a2f modelTranslator;

	public PModel(a2f translator, Criteria cri){
		modelTranslator = translator;		
		criteria = cri;
	}

	public void setFixedNode(List<String> nodes){
		if (nodes==null) throw new TranslatorException("Cannot apply to a null object.");
		for (int i=0;i<nodes.size();i++)
			setFixedNode(nodes.get(i));
	}
	
	public void setFixedNode(String nodename){
		Var v = modelTranslator.getVar(nodename);
		setVar(v);
		removeENA(modelTranslator.getNode(nodename));
	}

	public void setFixedNode1(List<GraphNode> nodes){
		if (nodes==null) throw new TranslatorException("Cannot apply to a null object.");
	
		for (int i=0;i<nodes.size();i++)
			setFixedNode(nodes.get(i));
	}	

	private void setFixedNode(GraphNode node){
		if (node==null) throw new TranslatorException("");
	
		Var v = modelTranslator.getVar(node);
		setVar(v);
		
	}

	private void removeENA(String nodename){
		GraphNode n = modelTranslator.getNode(nodename);
		removeENA(n);
	}

	private void removeENA(GraphNode node){
		HashMap<GraphNode, TreeSet<Edge>> ena = modelTranslator.getAttributes();
		
		if (!ena.containsKey(node)){
			/* we may disable this warning message */
			System.err.println("Warning: node "+node+" does not have any attributes.");
			return;
		}

		for (Edge e : ena.get(node)){
			Expression expr = modelTranslator.getENA_Expr(e);
			modelTranslator.removeFormula(expr);
		}
	}


	private void setVar(Var v){
		/* check this variable, has to be boolean type */
		if (!v.isBoolType())
			throw new TranslatorException("Sorry, I need a boolean type.");
		
		if (criteria==Criteria.INCLUSION)
			v.setValue(new BoolValue(true));
		else
			v.setValue(new BoolValue(false));	//we can set the bound to zero.
		
		v.setFix(true);
		
		/* remove formula for attributes */
	}
	
	
	private Var getVar(GraphNode node){
		return modelTranslator.getVar(node);
	}

}

