/**
 *	Rules for coloring a petersen graph
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 */

package examples.petersen;

import atongmu.ast.FOFormula;
import atongmu.ast.Var;
import atongmu.util.Pair;

public final class Rule{
	private Petersen pgraph;
	private final int color=0x03;
	private FOFormula formula = new FOFormula();

	public Rule(Petersen graph){pgraph=graph;EncodeRule();}
	
	private void EncodeRule(){

		formula.addExpression(FOFormula.Range(1,color,pgraph.getOutter()));
		formula.addExpression(FOFormula.Range(1,color,pgraph.getInner()));

		for (Pair p : pgraph.getEdges())
			formula.addExpression(FOFormula.Unique((Var)p.first(),(Var)p.second()));
	}

	public FOFormula getFormula(){return formula;}

}
