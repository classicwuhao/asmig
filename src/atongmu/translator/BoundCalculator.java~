/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  NOV-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package atongmu.translator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.io.*;
import org.eclipse.emf.common.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import atongmu.util.SMT2Writer;
import atongmu.smt.SMTSolver;
import atongmu.smt.Solver;
import atongmu.smt.SmtInvoker;
import atongmu.smt.Result;
import atongmu.util.SMT2Writer;
import atongmu.err.UnknownException;
import atongmu.err.UnsatException;
import atongmu.ast.Argument;
import atongmu.ast.BinaryExpression;
import atongmu.ast.Constant;
import atongmu.ast.ConstDecl;
import atongmu.ast.Declaration;
import atongmu.ast.FunExpression;
import atongmu.ast.NegFun;
import atongmu.ast.Var;
import atongmu.ast.Vars;
import atongmu.ast.NumLiteral;
import atongmu.ast.BoolLiteral;
import atongmu.ast.ArithmeticExpression;
import atongmu.ast.Connective;
import atongmu.ast.Arithmetic;
import atongmu.ast.FOFormula;
import atongmu.ast.Expression;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.EnumType;
import atongmu.type.Type;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;
/**
 * This is the calculator for calculating bounds for your metamodel.
 * Ecore2ATG translator uses this calculator to figure out a bound for every class in your model.
 * Changing to a large bound will slow down the entire translation process.
 * The default bound is 3.
 */
public class BoundCalculator{
	private Bound bound;
	private HashMap<EClass,List<EClass>> itree = new HashMap<EClass,List<EClass>>();
	private HashMap<String,Var> vars = new HashMap<String,Var>();
	private HashMap<String, Integer> boundInfo = new HashMap<String, Integer>();
	private HashMap<String, Integer> mark = new HashMap<String, Integer>();
	private HashSet<EClass> set = new HashSet<EClass>();
	private FOFormula fof = new FOFormula();
	private String prefix="_$_";
	private final int max_upper_bound = 3; // this should be a least upper bound.

	public BoundCalculator(Bound b){
		bound = b;
		builditree();
	}

	public HashMap<String, Integer> calculate(){
		detectReferences();
		Strategy1();
		
		if (findBounds()==Result.UNSAT){
			System.err.println("Unanble to calculate the bounds, please try to allocate bound manually.");
			return null;
		}
		
		System.err.println("Bounds calculated:");

		for (String key : vars.keySet()){
			Var var = vars.get(key);			
			//System.out.println(var.getName()+"="+var.getValue());
			boundInfo.put(var.getName().substring(prefix.length()),new Integer(var.getValue().toString()));
		}

		return boundInfo;
		
	}

	public void reset(){
		Vars.resetContext();
	}

	private void builditree(){
		List<EClass> classes = bound.getMetaclasses();

		for (int i=0;i<classes.size();i++){
			EClass cls = classes.get(i);
			List<EClass> set;
			if (!cls.isAbstract()){
				Var v = new Var(prefix+cls.getName(),new IntType());
				ConstDecl c = new ConstDecl(v);
				vars.put(prefix+cls.getName(),v);
			}
			if (cls.getESuperTypes().size()>0){
				for (int j=0;j<cls.getESuperTypes().size();j++){
					EClass supercls = cls.getESuperTypes().get(j);				
					if (!itree.containsKey(supercls))
						set = new ArrayList<EClass>();
					else
						set = itree.get(supercls);
					set.add(cls);
					itree.put(supercls,set);
				}
			}
		}//end of for
	}//end of itree
	

	private void detectReferences(){
		List<EReference> refs = bound.getMetareferences();
		List<EReference> uni_refs = new ArrayList<EReference>();
		List<EReference> dup_refs = new ArrayList<EReference>();

		for (int i=0;i<refs.size();i++){
			EReference ref = refs.get(i);
			if (ref.getEOpposite()==null){
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1){
					//System.out.println("1..1:"+ref.getName());
					fof.addExpression(calcUN_One2One(ref));
				}

				if (ref.getLowerBound()==0 && ref.getUpperBound()==1){
					//System.out.println("0..1:"+ref.getName());
					fof.addExpression(calcUN_One2ZeroOrOne(ref));
				}

				if (ref.getLowerBound()==1 && ref.getUpperBound()==-1){
					//System.out.println("1..*:"+ref.getName());
					fof.addExpression(calcUN_One2OneStar(ref));
				}
		
				if (ref.getLowerBound()==0 && ref.getUpperBound()==-1){
					//System.out.println("*:"+ref.getName());
					fof.addExpression(calcUN_One2Star(ref));
				}
			}
			else {
				EReference oref = ref.getEOpposite();
				refs.remove(oref);
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("1<->1:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2One(ref,oref));
				}		
				if (ref.getLowerBound()==1 && ref.getUpperBound()==-1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("1<->1..*:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2OneOrStar(oref,ref));
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==-1){
					//System.out.println("1<->1..*:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2OneOrStar(ref,oref));
				}
				if (ref.getLowerBound()==0 && ref.getUpperBound()==-1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("1<->*:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2Star(oref,ref));
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==0 && oref.getUpperBound()==-1){
					//System.out.println("1<->*:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2Star(ref,oref));
				}
				if (ref.getLowerBound()==0 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("1<->0..1:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2ZeroOrOne(oref,ref));
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==0 && oref.getUpperBound()==1){
					//System.out.println("1<->0..1:"+ref.getName()+"<->"+oref.getName());
					fof.addExpression(calcBI_One2ZeroOrOne(ref,oref));
				}
			}
		}

	}
	
	private Expression calcUN_One2One(EReference ref){
		EClass cls = (EClass)ref.getEType();
		EClass container = ref.getEContainingClass();
		
		return new BinaryExpression(Connective.AND,buildeq(cls,1),buildeq(container,1));
	}

	private Expression calcUN_One2ZeroOrOne(EReference ref){
		EClass cls = (EClass)ref.getEType();		
		return buildeq(cls,0);
	}

	private Expression calcUN_One2OneStar(EReference ref){
		EClass cls = (EClass)ref.getEType();			
		EClass container = ref.getEContainingClass();

		return new BinaryExpression(Connective.AND,buildeq(cls,max_upper_bound),buildeq(container,1));
		//buildeq(cls,1)
	}

	private Expression calcUN_One2Star(EReference ref){
		EClass cls = (EClass)ref.getEType();		
		return buildeq(cls,max_upper_bound);
		//buildeq(cls,0);
	}

	private Expression calcBI_One2One(EReference ref1, EReference ref2){
		EClass cls1 = (EClass)ref1.getEType();
		EClass cls2 = (EClass)ref2.getEType();
		
		BinaryExpression b = new BinaryExpression (Connective.AND,buildeq(cls1,1),buildeq(cls2,1));
		ArithmeticExpression a = new ArithmeticExpression(Arithmetic.EQUAL,Sum(cls1),Sum(cls2));
		
		return new BinaryExpression(Connective.AND,b,a);
		
	}

	private Expression calcBI_One2ZeroOrOne(EReference ref1, EReference ref2){
		EClass cls1 = (EClass)ref1.getEType();
		EClass cls2 = (EClass)ref2.getEType();
		
		return new BinaryExpression(Connective.AND,buildeq(cls1,1),buildeq(cls2,0));
	}
	
	private Expression calcBI_One2OneOrStar(EReference ref1, EReference ref2){
		EClass cls1 = (EClass)ref1.getEType();
		EClass cls2 = (EClass)ref2.getEType();
		
		//BinaryExpression b = new BinaryExpression (Connective.AND,buildeq(cls1,0),buildeq(cls2,0));
		ArithmeticExpression a = new ArithmeticExpression(Arithmetic.GREATER_EQUAL,Sum(cls2),Sum(cls1));

		fof.addExpression(new ArithmeticExpression(Arithmetic.GREATER_EQUAL,Sum(cls2),new NumLiteral(max_upper_bound)));
		return new BinaryExpression(Connective.AND,buildeq(cls1,1),a);
		
	}		
	
	private Expression calcBI_One2Star(EReference ref1, EReference ref2){
		EClass cls1 = (EClass)ref1.getEType();
		EClass cls2 = (EClass)ref2.getEType();
		
		fof.addExpression(new ArithmeticExpression(Arithmetic.GREATER_EQUAL,Sum(cls2),new NumLiteral(max_upper_bound)));
		return new BinaryExpression(Connective.AND,buildeq(cls1,1),buildeq(cls2,0));
	}

	private Expression Sum(EClass cls){
		HashSet<EClass> children = new HashSet<EClass>();
		List<Var> bounds = new ArrayList<Var>();		

		if (!cls.isAbstract())
			children.add(cls);
		getDescendants(cls,children);

		if (children.size()==0)
			throw new BoundException("Error: Reference type ["+ cls + "]" +
				"cannot be instantiate through its children. ");

		for (EClass child : children)
			bounds.add(vars.get(prefix+child.getName()));

		if (bounds.size()<=1)
			return bounds.get(0);
		else
			return FOFormula.join(Arithmetic.PLUS,bounds);
	}

	private ArithmeticExpression buildeq(EClass cls, int num){
		HashSet<EClass> children = new HashSet<EClass>();
		List<Var> bounds = new ArrayList<Var>();		

		if (cls==null) throw new BoundException("Error: this class is null, please check your type.");

		if (!cls.isAbstract())
			children.add(cls);
		getDescendants(cls,children);

		if (children.size()==0)
			throw new BoundException("Error: Reference type ["+ cls + "]" +
				"cannot be instantiate through its children. ");

		for (EClass child : children){
			
			bounds.add(vars.get(prefix+child.getName()));
		}

		if (bounds.size()>=2)
			return new ArithmeticExpression(Arithmetic.GREATER_EQUAL,
												FOFormula.join(Arithmetic.PLUS, bounds),
												new NumLiteral(num));
		else
			return new ArithmeticExpression(Arithmetic.GREATER_EQUAL,bounds.get(0),new NumLiteral(num));
		//else
		//	return new ArithmeticExpression(Arithmetic.EQUAL,new NumLiteral(1), new NumLiteral(1));
		
	}

	private void getDescendants(EClass cls, HashSet children){
		if (itree.containsKey(cls)){
			List<EClass> classes = itree.get(cls);
			for (int i=0;i<classes.size();i++){
				getDescendants(classes.get(i),children);
				if (!classes.get(i).isAbstract())
					children.add(classes.get(i));
			}
		}
	}

	public String itree2String(){
		StringBuffer sb = new StringBuffer();
		for (EClass cls : itree.keySet()){
			sb.append(cls.getName()+"-> {");
			for (EClass child : itree.get(cls))
				sb.append(child.getName()+",");
			sb.append("}\n");
		}
		
		return sb.toString();
	}

	private void Strategy1(){
		List<Expression> exprs = new ArrayList<Expression>();
		for (String key : vars.keySet()){
			Var var = vars.get(key);			
			exprs.add(new ArithmeticExpression(Arithmetic.GREATER,var,new NumLiteral(0)));
		}

		if (exprs.size()<=0) return;
		
		if (exprs.size()>=2)
			fof.addExpression(FOFormula.join(Connective.AND,exprs));
		else
			fof.addExpression(exprs.get(0));	
	}

	private Result findBounds(){

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("bound.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			//invoker.releaseFiles();
			return invoker.incSolve();
		}
		catch (IOException e){ 
			System.err.println(e.getMessage());
			return Result.UNSAT;
		}
		catch (UnknownException e){
			e.printErrMessage("UNKNOWN: formula cannot be decided.");
			return Result.UNSAT;
		}
		catch (UnsatException e){
			e.printErrMessage("UNSAT: formula is not satisfiable.");
			return Result.UNSAT;
		}
		finally{
			return Result.SAT;
		}
	}
	
}
