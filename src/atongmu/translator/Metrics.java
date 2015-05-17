/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  APR-2013 
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

public class Metrics{
	private FOFormula formulas;
	private a2f modelTranslator;

	public Metrics(a2f translator){
		formulas = new FOFormula();
		modelTranslator = translator;
	}

	public void DisjointShare(String ref, int[] share){
		Var[][] edges = modelTranslator.getReference(ref);
		List <Var> f1 = new ArrayList<Var>();
		List <Expression> f2 = new ArrayList<Expression>();
		List <Expression> f3 = new ArrayList<Expression>();
		
		for (int i=0;i<share.length;i++)
			if (share[i]<=0 || share[i]>edges[0].length)
				throw new TranslatorException("Error: index ["+ share[i] +"] is invalid.");
		
		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[0].length;j++){
				f1.add(edges[i][j]);
				for (int k=0;k<share.length;k++)
					if (j==share[k]-1) f2.add(edges[i][j]);
			}
			
			for (int l=0;l<f2.size();l++)
				f1.remove(f2.get(l));

			Expression f0 = (f2.size()>1) ? FOFormula.join(Connective.OR,f2) : f2.get(0);
			Expression f01 = (f2.size()>1) ? FOFormula.join(Connective.AND,f2) : f2.get(0);
			f3.add(f01);
			if (f1.size()>0)
				formulas.addExpression(new BinaryExpression(Connective.IMPLIES,f0,FOFormula.None(f1)));
			f1.clear();f2.clear();
		}
		
		formulas.addExpression(FOFormula.SomeExprs(f3));
		
	}
	
	public void Share(String ref, int share[]){
		Var[][] edges = modelTranslator.getReference(ref);
		List <Var> f1 = new ArrayList<Var>();
		List <Expression> f2 = new ArrayList<Expression>();		
		List <Expression> f3 = new ArrayList<Expression>();
		
		for (int i=0;i<share.length;i++)
			if (share[i]<=0 || share[i]>edges[0].length)
				throw new TranslatorException("Error: index ["+ share[i] +"] is invalid.");
		
		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[0].length;j++){
				f1.add(edges[i][j]);
				for (int k=0;k<share.length;k++)
					if (j==share[k]-1) f2.add(edges[i][j]);
			}
			
			for (int l=0;l<f2.size();l++)
				f1.remove(f2.get(l));

			Expression f = (f2.size()>1) ? FOFormula.join(Connective.AND,f2) : f2.get(0);
			f3.add(f);
			/*if (f1.size()>0)
				formulas.addExpression(new BinaryExpression(Connective.IMPLIES,f,FOFormula.None(f1)));*/
			f1.clear();f2.clear();
		}

		formulas.addExpression(FOFormula.SomeExprs(f3));		

	}

	public void NoShare(String ref, int[] diff){
		Var[][] edges = modelTranslator.getReference(ref);
		List <Var> f1 = new ArrayList<Var>();
		List <Expression> f2 = new ArrayList<Expression>();
			
		for (int i=0;i<diff.length;i++)
			if (diff[i]<=0 || diff[i]>edges[0].length)
				throw new TranslatorException("Error: index ["+ diff[i] +"] is invalid.");

		for (int k=0;k<diff.length;k++){
			for (int i=0;i<edges.length;i++){
				for (int j=0;j<edges[0].length;j++){
					if (j!=diff[k]-1) f1.add(edges[i][j]);
				}
				formulas.addExpression(new BinaryExpression(
										Connective.IMPLIES,
										edges[i][diff[k]-1],
										FOFormula.None(f1)));
				f1.clear();
			}
		}
		
		for (int i=0;i<diff.length;i++){
			for (int j=0;j<edges.length;j++){
				f2.add(edges[j][diff[i]-1]);
			}
			formulas.addExpression(FOFormula.SomeExprs(f2));
			f2.clear();
		}
		
	}

	public void NoWeakShare(String ref, int[] diff){
		Var[][] edges = modelTranslator.getReference(ref);
		List <Var> f1 = new ArrayList<Var>();
		//List <Expression> f2 = new ArrayList<Expression>();
			
		for (int i=0;i<diff.length;i++)
			if (diff[i]<=0 || diff[i]>edges[0].length)
				throw new TranslatorException("Error: index ["+ diff[i] +"] is invalid.");


		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[0].length;j++)
				System.out.print(edges[i][j]+",");
			System.out.println();
		}

		System.out.println("Formula:");
		for (int i=0;i<edges.length;i++){
			for (int k=0;k<diff.length;k++){
				f1.add(edges[i][diff[k]-1]);
			}
			formulas.addExpression(FOFormula.OneOnly(f1));			
			f1.clear();			
		}
		
		/*for (int i=0;i<diff.length;i++){
			for (int j=0;j<edges.length;j++){
				f2.add(edges[j][diff[i]-1]);
			}
			formulas.addExpression(FOFormula.SomeExprs(f2));
			f2.clear();
		}*/
		
	}

	public FOFormula getFormulas(){return formulas;}
	
	public void ExactNumber(String ref, int k, boolean direction){
		if (k<0) throw new TranslatorException("Error: a negative number does not make sense to me.");

		/* NOTE: we allow only those associations that are reflexive. */
		Var[][] edges = modelTranslator.getRefByName(ref);
		Var[][] noc = new Var[edges.length][edges[0].length];
		List<Expression> exprs = new ArrayList<Expression>();
		List<Var> tmp = new ArrayList<Var>();
		List<Expression> subexprs = new ArrayList<Expression>();
		int counter = 1;

		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[i].length;j++)
				System.out.print(edges[i][j]+" ");
			System.out.println();
		}


		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[i].length;j++){
				Var v = new Var("_noc"+counter++,new IntType());
				ConstDecl decl = new ConstDecl(v);
				noc[i][j] = v;
				modelTranslator.getFormula().addExpression(FOFormula.Range(0,1,noc[i][j]));
				modelTranslator.getFormula().addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,noc[i][j],new NumLiteral(1))
											,edges[i][j]));
				modelTranslator.getFormula().addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,noc[i][j],new NumLiteral(0))
											,new NegFun(edges[i][j])));
			}
		}
					
		for (int i=0;i<edges.length;i++)
			exprs.add(new ArithmeticExpression(Arithmetic.EQUAL,noc[i][noc[0].length-i-1],new NumLiteral(0)));
			//System.out.println(edges[i][edges[0].length-i-1]);
		
		/* we always check our formula size */
		if (exprs.size()>1)
			formulas.addExpression(FOFormula.join(Connective.AND,exprs));
		else
			formulas.addExpression(exprs.get(0));
	
		/* now we can work out formulas by directions. */		
		tmp.clear();subexprs.clear();
		if (direction){
			for (int i=0;i<noc.length;i++){
				for (int j=0;j<noc[i].length;j++)
					tmp.add(noc[i][j]);
				subexprs.add(new ArithmeticExpression(Arithmetic.EQUAL,FOFormula.join(Arithmetic.PLUS,tmp),new NumLiteral(k)));
				tmp.clear();
			}
		}
		else{
			for (int i=0;i<noc[0].length;i++){
				for (int j=0;j<noc.length;j++)
					tmp.add(noc[j][i]);
				subexprs.add(new ArithmeticExpression(Arithmetic.EQUAL,FOFormula.join(Arithmetic.PLUS,tmp),new NumLiteral(k)));
				tmp.clear();
			}
		}
			
		formulas.addExpression(FOFormula.OneOnlyExprs(subexprs));
	}


	public void ExactNumberNonReflexive(String ref, int k){
		if (k<0) throw new TranslatorException("Error: a negative number does not make sense to me.");
		Var[][] edges = modelTranslator.getReference(ref);
		Var[][] num = new Var[edges.length][edges[0].length];
		List<Expression> exprs = new ArrayList<Expression>();
		List<Var> tmp = new ArrayList<Var>();
		List<Expression> subexprs = new ArrayList<Expression>();
		int counter = 1;
		
		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[i].length;j++)
				System.out.print(edges[i][j]+" ");
			System.out.println();
		}

		for (int i=0;i<edges.length;i++){
			for (int j=0;j<edges[i].length;j++){
				Var v = new Var("_num"+counter++,new IntType());
				ConstDecl decl = new ConstDecl(v);
				num[i][j] = v;
				modelTranslator.getFormula().addExpression(FOFormula.Range(0,1,num[i][j]));
				modelTranslator.getFormula().addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,num[i][j],new NumLiteral(1))
											,edges[i][j]));
				modelTranslator.getFormula().addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,num[i][j],new NumLiteral(0))
											,new NegFun(edges[i][j])));
			}
		}
		
		for (int i=0;i<edges[i].length;i++){
			for (int j=0;j<edges.length;j++){
				tmp.add(num[j][i]);
			}
			exprs.add(new ArithmeticExpression(Arithmetic.EQUAL, FOFormula.join(Arithmetic.PLUS,tmp), new NumLiteral(k)));
			tmp.clear();
		}

		if (exprs.size()>1)
			formulas.addExpression(FOFormula.join(Connective.AND,exprs));
		else
			formulas.addExpression(exprs.get(0));
	}

	
}
