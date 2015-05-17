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
import atongmu.atg.Node;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;

/**
 *	Model Translator
 *	Class, Attrbutes, Generalisations and Associations (uni/bi directional).
 */
public final class a2f{
	private e2a translator;
	private FOFormula fof;
	private FOFormula fofattr; //additional constraints for attributes.
	private HashMap<EClass,TreeSet<GraphNode>> bvg;
	private HashMap<GraphNode, TreeSet<Edge>> ena;
	private LinkedHashMap<EReference, TreeSet<Edge>> st;
	private HashMap<GraphNode, Var> vars;
	private HashMap<Integer, GraphNode> nodes;
	private HashMap<DataNode, Var> data;
	private HashMap<DataNode, StrVar> string;
	private HashMap<Edge,Var[]> sequence;
	private HashMap<Edge, Var> attrs;
	private HashMap<Integer, Expression> nodeattrs;
	private HashMap<Edge, Var> edges;
	private List<BinaryExpression> tmpExprs = new ArrayList<BinaryExpression>();
	private SBP sbp = new SBP(this); /* symmetry breaking predicates generator */
	private FormulaCache fcache = new FormulaCache();	

	public a2f (e2a t){
		translator = t;
		fof = new FOFormula();
		bvg = translator.getFactory().getBVG();
		ena = translator.getFactory().getENA();
		st = translator.getFactory().getST();
		vars = new HashMap<GraphNode, Var>();
		nodes = new HashMap<Integer, GraphNode>();
		data = new HashMap<DataNode,Var>();
		attrs = new HashMap<Edge,Var>();
		nodeattrs = new HashMap<Integer, Expression>();
		edges = new HashMap<Edge,Var>();
		sequence = new HashMap<Edge, Var[]>();
		string = new HashMap<DataNode,StrVar>();
		fofattr = new FOFormula();
	}
	
	public void TranslateToFOF(){
		TranslateObjects();
		TranslateAttributes();
		TranslateLinks();
	}

	private void TranslateObjects(){
		for (EClass cls : bvg.keySet()){
			for (GraphNode n : bvg.get(cls)){ 
				Var v = new Var(n.getName(),new BoolType());
				ConstDecl c = new ConstDecl(v);
				vars.put(n,v);
				nodes.put(n.getName().hashCode(),n);
			}
		}
		
		for (GraphNode n : ena.keySet()){
			for (Edge e : ena.get(n)){
				Var v;
				DataNode d = (DataNode)e.dest();
				PrimitiveType t = d.getPType();
				if (t==PrimitiveType.INT)
					v = new Var(d.getName(),new IntType());
				else if (t==PrimitiveType.BOOL)
					v = new Var(d.getName(),new BoolType());
				else if (t==PrimitiveType.ENUM){
					String[] literals = translator.getFactory().getEnum(d);
					v = new Var(d.getName(),new EnumType(literals));
					/* additional constraints */
					fofattr.addExpression(
						new BinaryExpression(Connective.AND,
							new ArithmeticExpression(Arithmetic.GREATER_EQUAL,v,new NumLiteral(0)),
							new ArithmeticExpression(Arithmetic.LESS,v,new NumLiteral(literals.length))
						)
					);
				}
				else if (t==PrimitiveType.STRING){
					/*StrVar vs =new StrVar(translator.getFactory().getStringBound(d)
						,n.getName()+"_"+d.getName()+"_");
					vs.setShortName(d.getName());
					fofattr.addExpression(vs.formula());
					string.put(d,vs);
					sequence.put(e,vs.getVars());*/
					continue;
				}
				else{
					throw new TranslatorException("error: unsupported type found "+d);
				}
				ConstDecl c = new ConstDecl(v);
				data.put(d,v);
				attrs.put(e,v);
			}
		}

		for (EReference ref : st.keySet()){
			for (Edge e : st.get(ref)){ 
				Var v = new Var(e.getName(),new BoolType());
				ConstDecl c = new ConstDecl(v);
				edges.put(e,v);
			}
		}
	}

	/* a seperate translation. */
	private void TranslateAttributes(){
		for (Edge e : attrs.keySet()){
			Var v1 = vars.get(e.source());
			Var v2 = data.get(e.dest());
			
			BinaryExpression b;
			if ( v2.getType() instanceof IntType) /* ??? using instanceof ??? */
				b = new BinaryExpression(Connective.EQUAL,v2,new NumLiteral(-1));
			else
				b = new BinaryExpression(Connective.EQUAL,v2,new BoolLiteral(false));

			BinaryExpression f = new BinaryExpression(Connective.IMPLIES,
				new BinaryExpression(Connective.EQUAL,v1,new BoolLiteral(false)),b);
		
			nodeattrs.put(e.hashCode(),f);
			fof.addExpression(f);
		}

		for (Edge e : sequence.keySet()){
			Var v1 = vars.get(e.source());
			StrVar strs = string.get(e.dest());
			
			for (int i=0;i<strs.length();i++)
				fof.addExpression(new BinaryExpression(Connective.IMPLIES,
					new NegFun(v1),new BinaryExpression(Connective.EQUAL,strs.getVar(i), new NumLiteral(0))
				));
		}
	}

	private void TranslateLinks(){		
		for (Edge e : edges.keySet()){
			// by this stage, everything should be in the right position.
			Var v1 = vars.get(e.source());
			Var v2 = vars.get(e.dest());
			Var v3 = edges.get(e);
			BinaryExpression b1 = new BinaryExpression(Connective.AND,
				new BinaryExpression(Connective.EQUAL,v1,new BoolLiteral(true))
				,new BinaryExpression(Connective.EQUAL,v2,new BoolLiteral(true)));
			BinaryExpression b2 = new BinaryExpression(Connective.EQUAL,v3,new BoolLiteral(true));
			BinaryExpression f = new BinaryExpression(Connective.IMPLIES,b2,b1);
			fof.addExpression(f);
		}

		
		for (EReference ref : st.keySet()){
			if (ref.getEOpposite()==null){
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1){
					TranslateOne2OneUN(ref);
				}// end of if

				if (ref.getLowerBound()==0 && ref.getUpperBound()==1){
					//System.out.println(ref.getName());
					TranslateZero2OneUN(ref);
				}

				if (ref.getLowerBound()==1 && ref.getUpperBound()==-1){
					TranslateSomeUN(ref);
				}
		
				if (ref.getLowerBound()==0 && ref.getUpperBound()==-1){
					TranslateZeroORSomeUN(ref);
				}
			}
			else{
				EReference oref = ref.getEOpposite();
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("one2one...");
					TranslateOne2OneBI(ref,oref);
				}		
				if (ref.getLowerBound()==1 && ref.getUpperBound()==-1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("order A: one2some...");
					TranslateOne2SomeBI(ref,oref,false);
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==-1){
					//System.out.println("order B: one2some...");
					TranslateOne2SomeBI(ref,oref,true);
				}
				if (ref.getLowerBound()==0 && ref.getUpperBound()==-1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("order A: zerorone...");
					TranslateZeroORSomeBI(ref,oref,false);	
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==0 && oref.getUpperBound()==-1){
					//System.out.println("order B: zerorone...");
					TranslateZeroORSomeBI(ref,oref,true);
				}
				if (ref.getLowerBound()==0 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==1 && oref.getUpperBound()==1){
					//System.out.println("order A: zerorsome...");				
					TranslateZero2OneBI(ref,oref,false);
				}
				if (ref.getLowerBound()==1 && ref.getUpperBound()==1 
					&& oref.getLowerBound()==0 && oref.getUpperBound()==1){
					//System.out.println("order B: zerorsome...");
					TranslateZero2OneBI(ref,oref,true);
				}
				
			}

		}// end of for

		fof.addExpression(fofattr);
	}

   /* translate corresponding links according to their multiplicities unidriectional 
    * [A] 1 -> 1 [B]
	*/
	private void TranslateOne2OneUN(EReference ref){
		EClass cls;
		int b = 0;
		ArrayList <Var> vs = new ArrayList<Var>();
		HashSet <EClass> children = new HashSet<EClass>();
		Bound bound = translator.getBound();

		if (ref.getEType() instanceof EClass)
			cls = (EClass)ref.getEType();
		else
			throw new TranslatorException("unsupported data type.");

		vs.clear();
		children.clear();
		
		if (!cls.isAbstract())
			b = bound.getBound(cls);
		translator.getAllChildren(cls,children);
		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				b += bound.getBound(child);
		}
	
		if (b==0) 
			throw new 
				TranslatorException("error: I cannot compute for reference ["
					+ref.getName()+"] in class ["+ref.getEContainingClass().getName()
					+"] because either it doesn't have any concrete children or bound is set to zero.");
		
		int c = 0;
		for (Edge e : st.get(ref)){
				vs.add(edges.get(e));
				c++;
			if (c>=b){
				fof.addExpression(FOFormula.OneOnly(vs));
				vs.clear();
				c = 0;
			}// end of if
		}// end of for
	}
	
   /** 
    * [A] 1 -> 0..1 [B]
    */
	private void TranslateZero2OneUN(EReference ref){
		EClass cls;
		int b = 0;
		int c = 0;
		ArrayList <Var> vs = new ArrayList<Var>();
		//ArrayList <Var> vs2 = new ArrayList<Var>();
		//ArrayList <BinaryExpression> bs = new ArrayList<BinaryExpression>();
		HashSet <EClass> children = new HashSet<EClass>();
		Symbol sym = new AssociationSymbol(ref.getName(),ref.hashCode(),
						ref.getLowerBound(),ref.getUpperBound(),false);
		
		Bound bound = translator.getBound();

		if (ref.getEType() instanceof EClass)
			cls = (EClass)ref.getEType();
		else
			throw new TranslatorException("unsupported data type.");

		vs.clear();

		if (!cls.isAbstract())
			b = bound.getBound(cls);

		translator.getAllChildren(cls,children);
		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				b += bound.getBound(child);
		}

		//System.out.println(ref.getName()+":"+ref.getEContainingClass().getName()+"->"+cls.getName()+"->"+b);
		if (b==0) 
			throw new 
				TranslatorException("error: I cannot compute for reference ["
					+ref.getName()+"] in class ["+ref.getEContainingClass().getName()
					+"] because either it doesn't have any concrete children or bound is set to zero.");

		for (Edge e : st.get(ref)){
			vs.add(edges.get(e));
			c++;
			if (c>=b){
				Expression expr = new BinaryExpression(Connective.OR,FOFormula.None(vs),FOFormula.OneOnly(vs));
				fcache.cache(sym, expr); /* cache the formula. */
				fof.addExpression(expr);
				vs.clear();
				c = 0;
			}// end of if
		}

		//if (bs.size()==0) return;
		/* be aware of using join operation */
		//if (bs.size()>1)
			//fof.addExpression(new BinaryExpression(Connective.OR,FOFormula.None(vs1),FOFormula.join(Connective.AND,bs)));
		//else
			//fof.addExpression(new BinaryExpression(Connective.OR,FOFormula.None(vs1),bs.get(0)));
	}

   /* 
    * [A] 1 -> 1..* [B]
    */
	private void TranslateSomeUN(EReference ref){
		ArrayList<Var> vars = new ArrayList<Var>();
		ArrayList<Var> vars0 =  new ArrayList<Var>();
		HashSet <EClass> children = new HashSet<EClass>();
		Bound bound = translator.getBound();
		EClass cls;
		int b = 0,c = 0,l = 0,t = 0;
		Symbol sym = new AssociationSymbol(ref.getName(),ref.hashCode(),
						ref.getLowerBound(),ref.getUpperBound(),false);

		if (ref.getEType() instanceof EClass)
			cls = (EClass)ref.getEType();
		else
			throw new TranslatorException("unsupported data type.");
		
		if (!cls.isAbstract())
			b = bound.getBound(cls);

		translator.getAllChildren(cls,children);

		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				b += bound.getBound(child);
		}

		if (b==0) 
			throw new 
				TranslatorException("error: I cannot compute for reference ["
					+ref.getName()+"] in class ["+ref.getEContainingClass().getName()
					+"] because either it doesn't have any concrete children or bound is set to zero.");
		
		for (Edge e : st.get(ref)){
			vars.add(edges.get(e));
			vars0.add(edges.get(e));
			c++;
			if (c>=b){
				Expression expr = FOFormula.Some(vars);
				fof.addExpression(expr);
				fcache.cache(sym,expr);
				l+=vars.size();
				vars.clear();
				c = 0;
				t++;
			}
		}
		//System.out.println("level:"+(l/t-1));
		//sbp.SBP4Some(vars0,l/t-1);
		//sbp.genSBP4Some(vars0);
	}

	/*
     * [A] 1 -> * [B]
     */
	private void TranslateZeroORSomeUN(EReference ref){
		ArrayList<Var> vars = new ArrayList<Var>();
		HashSet <EClass> children = new HashSet<EClass>();
		Bound bound = translator.getBound();
		EClass cls;
		int b = 0;
		int c = 0;		
		Symbol sym = new AssociationSymbol(ref.getName(),ref.hashCode(),
						ref.getLowerBound(),ref.getUpperBound(),false);		


		if (ref.getEType() instanceof EClass)
			cls = (EClass)ref.getEType();
		else
			throw new TranslatorException("unsupported data type.");
		
		if (!cls.isAbstract())
			b = bound.getBound(cls);

		translator.getAllChildren(cls,children);

		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				b += bound.getBound(child);
		}
		
		if (b==0) 
			throw new 
				TranslatorException("error: I cannot compute for reference ["
					+ref.getName()+"] in class ["+ref.getEContainingClass().getName()+"] because either it doesn't have any concrete children or bound is set to zero.");

		for (Edge e : st.get(ref)){
			vars.add(edges.get(e));
			//System.out.print(e+" , ");
			c++;
			if (c>=b){
				BinaryExpression expr = new BinaryExpression(Connective.OR,
					FOFormula.None(vars),FOFormula.Some(vars));
				fof.addExpression(expr);
				fcache.cache(sym,expr);
				vars.clear();
				//System.out.println();
				c = 0;
			}
		}// end of for
		
	}

	/* In [A] 1..1 [B] bidirectional case: the array has to be square. 
     * Each object assigned by users must have at most one corresponding object.
     * m >= n or n >= m are not allowed. Only the case m = n is permitted.
     * Thus, every single instance found under this encoding is symmetry.
	 */
	private void TranslateOne2OneBI(EReference ref1, EReference ref2){
		Edge m[][] = toArray(ref1,ref2);
		List<BinaryExpression> list1 = new ArrayList<BinaryExpression>();		
		List<BinaryExpression> list2 = new ArrayList<BinaryExpression>();

		List<Var> vars = new ArrayList<Var>();

		for (int i=0;i<m.length;i++,vars.clear()){
			for (int j=0;j<m[i].length;j++){
				vars.add(edges.get(m[i][j]));
			}
			if (vars.size()>1)
				list1.add(FOFormula.OneOnly(vars));
			else
				list1.add(new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true)));
			//list1.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.OneOnly(vars)));
		}
		
		for (int i=0;i<m[0].length;i++,vars.clear()){
			for (int j=0;j<m.length;j++){
				vars.add(edges.get(m[j][i]));
			}
			if (vars.size()>1)
				list2.add(FOFormula.OneOnly(vars));
			else
				list2.add(new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true)));
		}
		
		ToFormula(list1, list2);

		// if (m.length>1 && m[0].length>1)
		//	fof.addExpression(sbp.generateOne2One(m));
	}

	/**
     * In [A] 1 <-> 1..* [B] bidirectional case: the array has to be square. 
     * Each A object assigned by users must have at least one corresponding B object(s), and 
	 * each B object has at most one A object. Thus, |B| >= |A|. When |B| = |A| => 1..1
     */
	private void TranslateOne2SomeBI(EReference ref1, EReference ref2, boolean t){
		Expression exprs[][];
		Symbol sym = new AssociationSymbol(ref1.getName()+":"+ref2.getName(),
						ref1.hashCode(),1,-1,true);
		Edge e[][] = toArray(ref1,ref2);
		Edge m[][] = e;
		if (t) m = transpose(e);

		List<BinaryExpression> list1 = new ArrayList<BinaryExpression>();		
		List<BinaryExpression> list2 = new ArrayList<BinaryExpression>();
				
		List<Var> vars = new ArrayList<Var>();
		exprs = new Expression[m.length][m[0].length];

		for (int i=0;i<m.length;i++,vars.clear()){
			for (int j=0;j<m[i].length;j++){
				exprs[i][j] = edges.get(m[i][j]); // record this array.
				vars.add(edges.get(m[i][j]));
			}
			list1.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.OneOnly(vars)));
			//list1.add(FOFormula.OneOnly(vars));
		}

		for (int i=0;i<m[0].length;i++,vars.clear()){
			for (int j=0;j<m.length;j++){
				vars.add(edges.get(m[j][i]));
			}
			//if (vars.size()>1)
			list2.add(FOFormula.Some(vars));
			//else
				//list2.add(new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true)));
		}

		
		fcache.cache(sym,exprs);
		ToFormula(list1, list2);
	
	}

	/**
     * [A} 1 <-> * [B]
     */
	private void TranslateZeroORSomeBI(EReference ref1, EReference ref2, boolean t){
		Expression exprs[][];
		Edge e[][] = toArray(ref1,ref2);
		Edge m[][] = e;
		Symbol sym = new AssociationSymbol(ref1.getName()+":"+ref2.getName(),
						ref1.hashCode(),0,-1,true);

		if (t) m = transpose(e);

		List<BinaryExpression> list1 = new ArrayList<BinaryExpression>();		
		List<BinaryExpression> list2 = new ArrayList<BinaryExpression>();				

		List<Var> vars = new ArrayList<Var>();
		exprs = new Expression[m.length][m[0].length];	

		for (int i=0;i<m.length;i++,vars.clear()){
			for (int j=0;j<m[0].length;j++){
				if (edges.get(m[i][j])!=null){
					exprs[i][j] = edges.get(m[i][j]);
					vars.add(edges.get(m[i][j]));
				}
			}
			
			if (vars.size()>1)
				list1.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.OneOnly(vars)));
			else if (vars.size()!=0)
				list1.add( new BinaryExpression(Connective.OR,
					new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true)),
					new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(false))));
			//list1.add(FOFormula.OneOnly(vars));
		}
		
		for (int i=0;i<m[0].length;i++,vars.clear()){
			for (int j=0;j<m.length;j++){
				vars.add(edges.get(m[j][i]));
			}
			if (vars.size()>1)
				list2.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.Some(vars)));
			else
				list2.add(new BinaryExpression(Connective.OR,
					new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true)),
					new BinaryExpression(Connective.EQUAL,vars.get(0),new BoolLiteral(true))));
		}

		fcache.cache(sym,exprs);
		ToFormula(list1,list2);
	}

   /**
  	*	[A] 1 <-> 0..1 [B]
 	*/
	private void TranslateZero2OneBI(EReference ref1, EReference ref2, boolean t){
		Expression exprs[][];
		Symbol sym = new AssociationSymbol(ref1.getName()+":"+ref2.getName(),
						ref1.hashCode(),0,1,true);
		Edge e[][] = toArray(ref1,ref2);
		Edge m[][] = e;
		if (t) m = transpose(e);

		List<BinaryExpression> list1 = new ArrayList<BinaryExpression>();		
		List<BinaryExpression> list2 = new ArrayList<BinaryExpression>();				

		List<Var> vars = new ArrayList<Var>();		
		exprs = new Expression[m.length][m[0].length];
	
		for (int i=0;i<m.length;i++,vars.clear()){
			for (int j=0;j<m[i].length;j++){
				if (edges.get(m[i][j])!=null){
					exprs[i][j]=edges.get(m[i][j]); //record this array.
					vars.add(edges.get(m[i][j]));
				}
			}			
			list1.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.OneOnly(vars)));
		}
		
		for (int i=0;i<m[0].length;i++,vars.clear()){
			for (int j=0;j<m.length;j++){
				if (edges.get(m[j][i])!=null)
					vars.add(edges.get(m[j][i]));
			}
			if (vars.size()>0)			
				list2.add(new BinaryExpression(Connective.OR,FOFormula.None(vars),FOFormula.OneOnly(vars)));
		}
		
		fcache.cache(sym,exprs);
		ToFormula(list1,list2);
	
	}

	/*private void ToFormula1(List<BinaryExpression> list1, List<BinaryExpression> list2){
		
		if (list1.size()==1 && list2.size()==1){
			fof.addExpression(new BinaryExpression(Connective.OR,list1.get(0),list2.get(0)));
			return;
		}
		
		if (list1.size()==1 && list2.size()!=1){
			fof.addExpression(new BinaryExpression(Connective.OR,
								list1.get(0),FOFormula.join(Connective.AND,list2)));
			return;
		}

		if (list1.size()!=1 && list2.size()==1){
			fof.addExpression(new BinaryExpression(Connective.OR,
								list2.get(0),FOFormula.join(Connective.AND,list1)));
			return;
		}

		fof.addExpression(new BinaryExpression(Connective.AND,
							FOFormula.join(Connective.AND,list1),
							FOFormula.join(Connective.AND,list2)
							));
	}*/

	/* write everything formed so far into a formula */
	private void ToFormula(List<BinaryExpression> list1, List<BinaryExpression> list2){
		
		if (list1.size()==1 && list2.size()==1){
			fof.addExpression(new BinaryExpression(Connective.AND,list1.get(0),list2.get(0)));
			return;
		}
		
		if (list1.size()==1 && list2.size()!=1){
			fof.addExpression(new BinaryExpression(Connective.AND,
								list1.get(0),FOFormula._join(Connective.AND,list2)));
			return;
		}

		if (list1.size()!=1 && list2.size()==1){
			fof.addExpression(new BinaryExpression(Connective.AND,
								list2.get(0),FOFormula._join(Connective.AND,list1)));
			return;
		}

		fof.addExpression(new BinaryExpression(Connective.AND,
							FOFormula._join(Connective.AND,list1),
							FOFormula._join(Connective.AND,list2)
							));
	}

	private Edge[][] toArray(EReference ref1, EReference ref2){
		EClass cls1, cls2;
		Edge medges[][];
		int row = 0;
		int col = 0;
		int i = 0;
		int j = 0;
		HashSet <EClass> children1 = new HashSet<EClass>();
		HashSet <EClass> children2 = new HashSet<EClass>();
		Bound bound = translator.getBound();
	
		if (ref1.getEType() instanceof EClass)		
			cls1 = (EClass) ref1.getEType();
		else
			throw new TranslatorException("unsupported data type:"+ref1.getEType().getName());

		if (ref2.getEType() instanceof EClass)
			cls2 = (EClass) ref2.getEType();
		else
			throw new TranslatorException("unsupported data type:"+ref2.getEType().getName());

		if (!cls1.isAbstract())
			row = bound.getBound(cls1);

		if (!cls2.isAbstract())
			col = bound.getBound(cls2);

		translator.getAllChildren(cls1,children1);
		translator.getAllChildren(cls2,children2);

		for (Iterator<EClass> it = children1.iterator();it.hasNext();){
			EClass child1 = it.next();
			if (!child1.isAbstract())
				row += bound.getBound(child1);
		}

		for (Iterator<EClass> it = children2.iterator();it.hasNext();){
			EClass child2 = it.next();
			if (!child2.isAbstract())
				col += bound.getBound(child2);
		}
		
		if (row==0 || col==0) 
			throw new 
				TranslatorException("error: I cannot compute for references ["+ref1.getName()+","+ref2.getName()+"]");

		medges = new Edge[row][col];
		for (Edge e : st.get(ref1)){
			medges[i][j++] = e;
			if (j>=col){i++;j=0;}
		}
		
		/*System.out.println("toarray:->");
		for (int x=0;x<row;x++){
			for (int y=0;y<col;y++)
				System.out.print(medges[x][y]+" ");
			System.out.println();
		}*/
			
		return medges;
	}

	private Edge[][] transpose(Edge[][] m){
		if (m==null) throw new TranslatorException("Cannot transpose this array.");

		Edge medges[][] = new Edge[m[0].length][m.length];
		
		for (int i=0;i<m.length;i++){
			for (int j=0;j<m[i].length;j++){
				medges[j][i] = m[i][j];
			}
		}
		
		return medges;
	}
	
	public List<GraphNode> getAllChildren(String clsname){
		List<EClass> classes = translator.getBound().getMetaclasses();
		EClass t=null;
		HashSet<EClass>	children = new HashSet<EClass>();
		List<GraphNode> gnodes=null;
		HashMap<EClass,TreeSet<GraphNode>> bvg = translator.getFactory().getBVG();
		
		for (int i=0;i<classes.size();i++){
			if (classes.get(i).getName().compareTo(clsname)==0){
				t = classes.get(i);
				break;
			}
		}
		
		if (t==null) throw new TranslatorException("can not locate metaclass <"+clsname+">, program aborted.");

		translator.getAllChildren(t,children);
		gnodes = new ArrayList<GraphNode>();
		if (!t.isAbstract()){
			/* add itself into the list */
			for (GraphNode n : bvg.get(t))
				gnodes.add(n);
		}
		
		/* now children's trun */
		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			TreeSet<GraphNode> nodes = bvg.get(it.next());
			if (nodes!=null){
				for (GraphNode n : nodes)
					gnodes.add(n);
			}
		}

		return gnodes;
	}

	
	public Var getVar(String node){
		if (node==null)  throw new TranslatorException("Node is null.");
		if (node.length()==0) throw new TranslatorException("Node is not specified.");
		
		if (!nodes.containsKey(node.hashCode())) 
			throw new TranslatorException("Failed to find node <"+node+">");
			
		return vars.get(nodes.get(node.hashCode()));
		
	}

	public Var getVar(GraphNode node){
		if (node==null) throw new TranslatorException("Node is null.");
		if (!vars.containsKey(node)) throw new TranslatorException("Failed to find node <"+node.toString()+">");
		
		return vars.get(node);
	}

	public Var getVar(Edge e){
		if (e==null) throw new TranslatorException("Edge is null.");
		if (!edges.containsKey(e)) throw new TranslatorException("Failed to find edge <"+e.toString()+">");
		
		return edges.get(e);
	}

	public String getRefType(String name){
		EReference ref = translator.getFactory().getRef(name);
		if (ref==null) throw new TranslatorException("Error: this reference cannot be found.");
		
		return ref.getEType().getName();
	}

	public GraphNode getNode(String node){
		if (node==null)  throw new TranslatorException("Node is null.");
		if (node.length()==0) throw new TranslatorException("Node is not specified.");
		
		if (!nodes.containsKey(node.hashCode())) 
			throw new TranslatorException("Failed to find node <"+node+">");

		return nodes.get(node.hashCode());
	}

	public Expression getENA_Expr(Edge e){
		if (e==null) throw new TranslatorException("Edge is null.");
		if (!nodeattrs.containsKey(e.hashCode())) 
			throw new TranslatorException("Failed to find ena edge <"+e.getName()+">");

		return nodeattrs.get(e.hashCode());
	}

	public void removeFormula(Expression b){
		fof.removeExpression(b);
	}

	public int getEnumLiteralIndex(String type, String literal){
		EEnum eenum = translator.getEnumbyName(type);
		int i = -1;
		for (EEnumLiteral e : eenum.getELiterals()){
			i++;
			if (e.getLiteral().compareTo(literal)==0)
				return i;
		}
		return -1;
	}

	private int getBound(EClass eclass){
		HashSet <EClass> children = new HashSet<EClass>();
		Bound bound = translator.getBound();
		EClass cls=eclass;
		int b = 0;
		
		if (!cls.isAbstract())
			b = bound.getBound(cls);

		translator.getAllChildren(cls,children);

		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				b += bound.getBound(child);
		}
		
		if (b==0) 
			throw new 
				TranslatorException("error: I cannot compute for class ["
					+cls.getName()+"]" +
				"] because either it doesn't have any concrete children or bound is set to zero.");	

		return b;

	}

	public Var[][] getReference(String ref){
		if (ref==null) throw new TranslatorException("Error: reference name cannot be null.");
		EReference key=null;
		Edge m[][];
		Var vars[][];
		int b0=0,b1=0;
		
		for (EReference r : st.keySet())
			if (r.getName().compareTo(ref)==0) key=r;
		
		if (key==null) throw new TranslatorException("Falied to find reference "+ref);

		if (key.getEType() instanceof EClass)
			b0=getBound((EClass)key.getEType());
		else
			throw new TranslatorException("Expecting a EClass type object.");
		
		b1=getBound(key.getEContainingClass());
		vars = new Var[b0][b1];
		m = new Edge[b1][b0];
		int i=0,j=0;

		for (Edge e : st.get(key)){
			m[i][j++] = e;
			if (j>=b0){i++;j=0;}
		}// end of for


		Edge m1[][]=transpose(m);
		vars = new Var[m1.length][m1[0].length];

		for (i=0;i<m1.length;i++)
			for (j=0;j<m1[0].length;j++)
				vars[i][j]=edges.get(m1[i][j]);
		
		return vars;
	}

	public Var[][] getRefByName(String reference){
		if (reference==null) throw new TranslatorException("Error: reference name cannot be null.");
		
		EReference ref=null;
		int i=0,j=0;
		EClass cls;
		HashSet <EClass> children = new HashSet<EClass>();		
		Bound bound = translator.getBound();
		Var vars[][];		

		for (EReference r : st.keySet())
			if (r.getName().compareTo(reference)==0) ref=r;
		
		if (ref==null) throw new TranslatorException("Falied to find reference "+reference);

		if (ref.getEOpposite()!=null) throw new TranslatorException("Error: it must be a directed graph.");

		if (ref.getEType() instanceof EClass)
			cls = (EClass) ref.getEType();
		else
			throw new TranslatorException("unsupported data type:"+ref.getEType().getName());

		if (!cls.isAbstract())
			i = bound.getBound(cls);
		
		translator.getAllChildren(cls,children);
		
		for (Iterator<EClass> it = children.iterator();it.hasNext();){
			EClass child = it.next();
			if (!child.isAbstract())
				i+= bound.getBound(child);
		}
		
		int r = i;
		Edge m[][] = new Edge[i][i];
		vars = new Var[i][i];i=0;
		for (Edge e : st.get(ref)){
			vars[i][j++]=edges.get(e);
			if (j>=r){i++;j=0;}
		}

		return vars;
	}

	public List<Var> containVar(String ref1, List<Var> v, String ref2){
		if (v.size()<=0) throw new TranslatorException("No need to perform such calculations.");
		EReference ref=null;
		List<Var> c = new ArrayList<Var>();
		Var m[][] = getRefByName(ref1);
		HashSet<Node> items = new HashSet<Node>();
		
		for (EReference r : st.keySet())
			if (r.getName().compareTo(ref1)==0) ref=r;
		
		if (ref==null) throw new TranslatorException("Falied to find reference "+ref1);
		
		for (int i=0;i<v.size();i++){
			for (Edge e : st.get(ref)){
				if (v.get(i)==edges.get(e)){
					items.add(e.source());	
					items.add(e.dest());
				}
			}
		}

		for (EReference r : st.keySet())
			if (r.getName().compareTo(ref2)==0) ref=r;
		
		if (ref==null) throw new TranslatorException("Falied to find reference "+ref2);

		for (Edge e : st.get(ref)){
			for (Iterator<Node> iterator = items.iterator();iterator.hasNext();){
				Node n = iterator.next();
				if (e.source()==n || e.dest()==n)
					c.add(edges.get(e));
			}
		}

		/*System.out.println("relevant variables:");		
		for (int i=0;i<c.size();i++)
			System.out.println(c.get(i));
		System.out.println("finished");*/
		
		return c;
	}

	public void SwitchOff(GraphNode...nodes){
		if (nodes==null) throw new TranslatorException ("Error: nodes are not specified. ");
		if (nodes.length==0) return;
				
		for (int i=0;i<nodes.length;i++){
			Var v = vars.get(nodes[i]);
			if (v==null) throw new TranslatorException("Error: this node is not part of my translation. "+nodes[i]);
			fof.addExpression(new NegFun(v));
		}

	}
	
	public TreeSet<Edge> getEdges(String name){
		EReference ref = null;
		for (EReference r : st.keySet())
			if (r.getName().compareTo(name)==0) ref=r;
				
		if (ref==null) throw new TranslatorException("Error: no such reference: "+ref);
		
		return st.get(ref);
	}


	public e2a getATGTranslator(){return translator;}	
	public FOFormula getFormula(){return fof;}
	public void Conjoin(FOFormula formulas){fof.addExpression(formulas);}
	public HashMap<GraphNode, Var> getObjects(){ return vars;}
	public HashMap<GraphNode, TreeSet<Edge>> getAttributes(){return ena;}
	public HashMap<Edge, Var> getAttributes1(){return attrs;}
	public LinkedHashMap<EReference,TreeSet<Edge>> getReferences(){return st;}
	public HashMap<DataNode, Var> getData() {return data;}
	public HashMap<DataNode, StrVar>getString(){return string;}
	public HashMap<Edge, Var> getLinks(){return edges;}
	public FormulaCache getCache(){return fcache;}
	
}

