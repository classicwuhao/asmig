/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  JAN-2013 
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

public final class Acyclic{
	private a2f modelTranslator;
	private EReference reference;
		
	public Acyclic(a2f translator, EReference ref){
		modelTranslator = translator;
		reference = ref;
	}

	/**
     *	Approach 1: We only take either lower or upper triangular part from the matrix.
	 *  @TODO: An empty graph is also allowed in this case.
     */
	public void gen0(){
		if (modelTranslator==null) throw new TranslatorException("Error: translator cannot be null.");
		if (reference==null) throw new TranslatorException("Error: reference is null.");
		
		/* get all the edges */
		Edge m[][] = getEdges(reference);
		List<Expression> upper = new ArrayList<Expression>();
		List<Expression> lower = new ArrayList<Expression>();
		List<Expression> dia = new ArrayList<Expression>();

		/*for (int i=0;i<m.length;i++){
			for (int j=0;j<m[0].length;j++)
				System.out.print(m[i][j]+",");
			System.out.println();
		}*/

		for (int i=0;i<m.length;i++)
			for (int j=0;j<m[0].length-i-1;j++)
				upper.add(modelTranslator.getLinks().get(m[i][j]));
			
		for (int i=1;i<m.length;i++)
			for (int j=m[0].length-i;j<m[0].length;j++)
				lower.add(modelTranslator.getLinks().get(m[i][j]));
		for (int i=0;i<m.length;i++)
			dia.add(modelTranslator.getLinks().get(m[i][m[0].length-i-1]));

		/*for (int i=0;i<dia.size();i++)
			System.out.println(dia.get(i));*/
		
		if (upper.size()==0 || lower.size()==0)
			throw new TranslatorException("Error: No instance(s) will be found in the current bounds.");
		
		modelTranslator.getFormula().addExpression(
			new NegFun(FOFormula.join(Connective.OR,dia)));
	
		if (upper.size()==1 && lower.size()==1){
			if (reference.getUpperBound()==-1){
				modelTranslator.getFormula().addExpression(
					new BinaryExpression(Connective.OR,
						new NegFun(new BinaryExpression(Connective.OR,upper.get(0),lower.get(0))),
						new BinaryExpression(Connective.XOR,upper.get(0),lower.get(0))
					));
				return;
			}	
			modelTranslator.getFormula().addExpression(
				new BinaryExpression(Connective.XOR,upper.get(0),lower.get(0)));
			return;
		}

		Expression f1 = FOFormula.join(Connective.OR,upper);
		Expression f2 = FOFormula.join(Connective.OR,lower);

		if (reference.getUpperBound()==-1){
			Expression e1 = new BinaryExpression(Connective.XOR,f1,f2);
			Expression e2 = new NegFun(new BinaryExpression(Connective.OR,f1,f2));
			modelTranslator.getFormula().addExpression(FOFormula.join(Connective.OR,e1,e2));
			return;
		}
			
		modelTranslator.getFormula().addExpression(FOFormula.join(Connective.XOR,f1,f2));
	}

	private Edge[][] getEdges(EReference ref){
		int i=0,j=0;
		EClass cls;
		HashSet <EClass> children = new HashSet<EClass>();		
		Bound bound = modelTranslator.getATGTranslator().getBound();
		
		if (ref.getEOpposite()!=null) throw new TranslatorException("Error: it must be a directed graph.");

		if (ref.getEType() instanceof EClass)
			cls = (EClass) ref.getEType();
		else
			throw new TranslatorException("unsupported data type:"+ref.getEType().getName());

		if (!cls.isAbstract())
			i = bound.getBound(cls);
		
		modelTranslator.getATGTranslator().getAllChildren(cls,children);
		
		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				i+= bound.getBound(child);
		}
		
		int r = i;
		Edge m[][] = new Edge[i][i];i=0;
		for (Edge e : modelTranslator.getReferences().get(ref)){
			m[i][j++] = e;
			if (j>=r){i++;j=0;}
		}		

		return m;
	}
	
}
