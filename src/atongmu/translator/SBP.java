/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  AUGUST-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package atongmu.translator;

import java.util.*;
import org.eclipse.emf.ecore.*;
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

public final class SBP{
	private a2f modelTranslator;
	
	public SBP(a2f translator){
		if (translator==null)
			throw new SBPException("model translator can not be null.");

		modelTranslator = translator;
	}

	public BinaryExpression generateOne2One(Edge edges[][]){
		if (edges==null) throw new SBPException("array cannot be null.");
		if (edges[0].length != edges.length) throw new SBPException("array has to be squre.");
		int k=0;
		int sub = edges[0].length * (edges.length-1);
		Expression[] sbps = new BinaryExpression[sub];
		Var var = modelTranslator.getLinks().get(edges[0][edges[0].length-1]);
		
		for (int j=edges.length-2;j>=0;j--,k++)
			sbps[k] = new BinaryExpression(Connective.IMPLIES,var,
				modelTranslator.getLinks().get(edges[0][j]));
			
		for (int i=1;i<edges.length;i++){
			var = modelTranslator.getLinks().get(edges[i][0]);
			for (int j=1;j<edges[i].length;j++,k++){
			sbps[k] = new BinaryExpression(Connective.IMPLIES,var,
				modelTranslator.getLinks().get(edges[i][j]));					
			}
		}
		
		return FOFormula.join(Connective.AND,sbps);
	}

	public void SBP4Some(List<Var> vars, int level){
		List<Expression> exprs = new ArrayList<Expression>();
		Var lexer = vars.get(0);		

		if (vars.size()<=1) return;

		for (int i=0;i<level;i++){
			Var l = vars.get(i);
			for (int j=i+1;j<vars.size();j++)
				exprs.add(new BinaryExpression(Connective.IMPLIES,l,vars.get(j)));
		}

		if (exprs.size()>1)
			modelTranslator.getFormula().addExpression(FOFormula.join(Connective.AND,exprs));
		else
			modelTranslator.getFormula().addExpression(exprs.get(0));	
	}

	public void genSBP4Some(List<Var> vars){
		if (vars.size()<=1) return;
		
		Var lexer = vars.get(0);
		List<Expression> sbps = new ArrayList<Expression>();
		
		for (int i=1;i<vars.size();i++)
			sbps.add(new BinaryExpression(Connective.IMPLIES,lexer,vars.get(i)));
		
		modelTranslator.getFormula().addExpression(FOFormula.join(Connective.AND,sbps));
	}

}

