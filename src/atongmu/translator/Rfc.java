/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  JULY-2013 
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
import atongmu.ast.StrVar;
import atongmu.ast.Expression;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.EnumType;
import atongmu.type.Type;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;

public class Rfc{
	private a2f modelTranslator;
	private HashMap	<Integer,ArrayList<Expression>> chains = new HashMap<Integer,ArrayList<Expression>>();
	private	HashMap<Integer, Expression> subsets = new HashMap<Integer, Expression>();

	public Rfc(a2f translator){
		if (translator==null) throw new TranslatorException("Error: model translator cannot be null.");
		this.modelTranslator= translator;
	}

	/*
	 * Given a length, and reference defined over the metamodel,
     * Generate graph with this particular path length.
     * The graph is a DAG.
	 */

	public void genChain(int len, String ref, String ref1){
		Var m[][] = modelTranslator.getRefByName(ref);
		/*check if this array is square.*/
		if (m.length!=m[0].length) throw new TranslatorException("Error: this array [" + ref +"] has to be square.");
		if (len>m.length-1 || len<0) throw new TranslatorException("Error: the specified length is not valid.");
		List<Var> vars = new ArrayList<Var>();		
		List<Var> chain = new ArrayList<Var>();
		List<Expression> exprs = new ArrayList<Expression>();
		List<Expression> subexprs = new ArrayList<Expression>();
		
		//List<Expression> upper = new ArrayList<Expression>();
		int c=0;

		/*for (int i=0;i<m.length;i++){
			for (int j=0;j<m[i].length;j++)
				System.out.print(m[i][j]);
			System.out.println();
		}*/
		
		/* get elements, this is very tricky and even myself takes a while to follow. */
		/* Remember: this array is differnt from an adjacency matrix. */
		/* we extract all the elements from left hand side.*/		
		
		for (int i=0;i<m.length-1;i++)
			vars.add(m[i][m[0].length-i-1-1]);

		/* turn off the rest of edges */		
		for (int i=1;i<m.length;i++)
			for (int j=m[0].length-i;j<m[0].length;j++)
				modelTranslator.getFormula().addExpression(FOFormula.None(m[i][j]));
		for (int i=0;i<m.length;i++)
			modelTranslator.getFormula().addExpression(FOFormula.None(m[i][m[0].length-i-1]));
		
		/* work out possible chains...*/
		LOOP:
		for (int i=0;i<vars.size();i++){
			for (int j=0;j<len;j++){
				if (i+j>=vars.size()) break LOOP;
				chain.add(vars.get(i+j));
			}
			if (chain.size()==len) {
				chains.put(new Integer(c),new ArrayList(chain));
				List<Var> tmp = modelTranslator.containVar(ref,chain,ref1);
				List<Expression> tmpexpr = new ArrayList<Expression>();
				List<ArrayList<Expression>> subset = new ArrayList<ArrayList<Expression>>();

				/*System.out.println("tmp...");
				for (int a=0;a<tmp.size();a++)
					System.out.println(tmp.get(a));
				System.out.println();*/
				
				for (int k=0;k<tmp.size();k+=len+1){
					ArrayList<Expression> list = new ArrayList<Expression>();
					for (int l=0;l<len;l++){
						list.add(tmp.get(l+k));
					}
					subset.add(list);
					/* switch off rest of edges in the same row */
					//modelTranslator.SwitchOff(list,ref1);
				}
				
				for (int n=0;n<subset.size();n++)
					tmpexpr.add(FOFormula.join(Connective.AND,subset.get(n)));

				subset.clear();
				subsets.put(new Integer(c++),FOFormula.join(Connective.OR,tmpexpr));
				tmpexpr.clear();
			}
			chain.clear();
		}
		
		for (Integer key : chains.keySet()){
			ArrayList<Expression> list = chains.get(key);
			if (list.size()>1){
				exprs.add(FOFormula.join(Connective.AND,list));
				System.out.println(FOFormula.join(Connective.AND,list));
				modelTranslator.getFormula().addExpression(
					new BinaryExpression(Connective.IMPLIES,FOFormula.join(Connective.AND,list),subsets.get(key)));
			}
			else{
				exprs.addAll(list);
				/*modelTranslator.getFormula().addExpression(
					new BinaryExpression(Connective.IMPLIES,list.get(0),subsets.get(key)));*/
			}
		}

		if (len==1){
			ArrayList[] rows = new ArrayList[m.length-1];
			for (int i=0;i<m.length-1;i++){
				rows[i] = new ArrayList<Var>();
				for (int j=0;j<m[0].length-i-1;j++)
					rows[i].add(m[i][j]);
			}
			
			/*for (int i=0;i<rows.length;i++){
				System.out.println();
				for (int j=0;j<rows[i].size();j++)
					System.out.print(rows[i].get(j)+"");
			}*/
				
			for (int i=0;i<rows.length-1;i++){
				Expression exprs0=FOFormula.Some(rows[i]);
				ArrayList<Var> exprs1 = new ArrayList<Var>();
				for (int j=i+1;j<rows.length;j++)
					exprs1.addAll(rows[j]);

				if (exprs1.size()>0)
					modelTranslator.getFormula().addExpression(
						(new BinaryExpression(Connective.IMPLIES,exprs0,FOFormula.None(exprs1))));
			}
		}

		modelTranslator.getFormula().addExpression(FOFormula.OneOnlyExprs(exprs));
	}
}


