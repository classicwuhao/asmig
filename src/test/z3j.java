/**
 *	Using Z3 Java APIs to solve formulas.
 *  Written by: Hao Wu
 *	TIME STAMP: 15th-JULY-2013
 *  Bug(s) report to: haowu@cs.nuim.ie
 *	
 *  Note
 *  @TODO: upgrade SMTInokver to Z3 Java APIs.
 *  @TODO: upgrade Interpreter functionaility.
 */

package test;

import com.microsoft.z3.*;
import com.microsoft.z3.enumerations.*;
import java.util.*;
import java.io.*;
import java.util.*;
import atongmu.ast.*;
import atongmu.atg.*;
import atongmu.type.*;
import atongmu.util.*;
import atongmu.value.*;
import atongmu.err.*;
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;
import atongmu.translator.*;
import atongmu.interpreter.*;

public class z3j{
	private static String libfile="libz3java.so";

	/* load library explicitly... */	
	/*static{
		try{
			//System.setProperty("java.library.path","../../lib/");
			//System.loadLibrary(libfile);
			System.load("/home/haowu/phd/Research/code/ModelFinder0.2/lib/"+libfile);
			System.out.println(System.getProperty("java.library.path"));
		}
		catch(UnsatisfiedLinkError e){
			System.err.println("Library loading error:"+ e.getMessage());
			System.exit(1);
		}
		
	}*/

	private static com.microsoft.z3.Solver z3Solver;
	private static Context ctx;
	private static HashMap<String,String> cfg;

	public static void main (String args[]) {
		long time, total_time;		
		int share[]=new int[3]; int diff[]=new int[1];
		share[0]=1;share[1]=4;share[2]=3;diff[0]=2;

		System.out.println("Z3 Java APIs test...");
		Initialise();
		total_time=time= System.currentTimeMillis();

		//System.out.println("Running simple test case 1...");
		//solve(SimpleTest1());
		//System.out.println("Finish simple test case 1 within "+(System.currentTimeMillis()-time) +" ms.");

		//System.out.println("Running simple test case 2...");
		//time = System.currentTimeMillis();
		//solve(SimpleTest2());
		//System.out.println("Finish simple test case 2 within "+(System.currentTimeMillis()-time) +" ms.");

		//System.out.println("Running simple test case 3...");
		//time = System.currentTimeMillis();
		//solve(SimpleTest3());
		//System.out.println("Finish simple test case 3 within "+(System.currentTimeMillis()-time) +" ms.");

		time = System.currentTimeMillis();
		System.out.println("Running complex test case 1...");
		solve(ComplexTest1(share,diff));
		System.out.println("Finish complex case 1 within "+(System.currentTimeMillis()-time) +" ms.");

		//time = System.currentTimeMillis();
		//System.out.println("Running complex test case 2...");
		//solve(ComplexTest2(2,3));
		//System.out.println("Finish complex case 2 within "+(System.currentTimeMillis()-time) +" ms.");

		System.out.println("\n Total time spend: "+ (System.currentTimeMillis()-total_time)+" ms.");
	}


	private static void Initialise(){
		cfg  = new HashMap<String,String>();
		try{
			cfg.put("model","true");
			cfg.put("auto-config","false");
			//cfg.put("model_completion","true");
			ctx = new Context(cfg);
			if (ctx==null) System.err.println("Error: Cannot initialise the solver, context is null and testing is terminated");
		}
		catch (Z3Exception e){
			System.err.println("Error:"+e.getMessage());
		}
	}

	public static void solve(FOFormula formula){
		Expr expr; BoolExpr b;
		Model model; FuncDecl constants[];
		int solutions=0;long time;
		List<BoolExpr> exprs = new ArrayList<BoolExpr>();
	
		try{
			cfg.put("model","true");
			cfg.put("auto-config","true");
			ctx = new Context(cfg);
			SMT2Writer writer = new SMT2Writer(new PrintWriter 
				(new FileWriter("formula.smt2")),formula);
			writer.Traverse(false);writer.close(true);
			time = System.currentTimeMillis();
			BoolExpr bexpr = ctx.parseSMTLIB2File("formula.smt2", null, null, null, null);
			time = System.currentTimeMillis()-time;
			System.out.println("Formula parsing completed in "+time+ " ms.");
			System.out.println("Begin to solve...");
			z3Solver = ctx.mkSolver();
			z3Solver.add(bexpr);
			
			//time = System.currentTimeMillis();
			if (z3Solver.check()==Status.SATISFIABLE){
				model = z3Solver.getModel();
				constants = model.getDecls();
				solutions++;
				System.out.println("Num of functions:"+model.getNumConsts());
				System.out.println("************Solution - "+solutions+"************");
				for (int i=0;i<constants.length;i++){
					//expr = model.getConstInterp(constants[i]);
					expr = model.eval(constants[i].apply(),true);
					Sort s = constants[i].getRange();
					com.microsoft.z3.Symbol sym = constants[i].getName();
					if (s.getSortKind()==Z3_sort_kind.Z3_INT_SORT)
						b = ctx.mkEq(ctx.mkIntConst(sym),expr);
					else if (s.getSortKind()==Z3_sort_kind.Z3_BOOL_SORT)
						b = ctx.mkEq(ctx.mkBoolConst(sym),expr);
					else
						throw new TranslatorException("Error: unsupported sort ( " + s +" )");

					//BoolExpr b = ctx.mkEq(ctx.mkBoolConst(sym),expr);
					exprs.add(b);
				}
				z3Solver.add(Negate(exprs,true));
				//System.out.println("\n expr:"+Negate(exprs));
				//System.out.println("\n time spent on this solution: "+ (System.currentTimeMillis() - time) +" ms.\n\n");
			}
			else{
				System.out.println("unsat: no solution is found.");
				return;
			}

			/* enumerate all possible solution(s) */
			while (z3Solver.check()==Status.SATISFIABLE){
				time = System.currentTimeMillis();
				model = z3Solver.getModel();
				//constants = model.getConstDecls();
				solutions++;
				System.out.println("************Solution - "+solutions+"************");
				exprs.clear();
				for (int i=0;i<constants.length;i++){
					//expr = model.getConstInterp(constants[i]);
					expr = model.eval(constants[i].apply(),true);
					Sort s = constants[i].getRange();
					com.microsoft.z3.Symbol sym = constants[i].getName();
					if (s.getSortKind()==Z3_sort_kind.Z3_INT_SORT)
						b = ctx.mkEq(ctx.mkIntConst(sym),expr);	
					else if (s.getSortKind()==Z3_sort_kind.Z3_BOOL_SORT)
						b = ctx.mkEq(ctx.mkBoolConst(sym),expr);
					else
						throw new TranslatorException("Error: unsupported sort ( " + s +" )");
					exprs.add(b);
				}
				z3Solver.add(Negate(exprs,true));
				//time = System.currentTimeMillis()-time;
				//System.out.println("\n time spent on this solution: "+time+" ms.\n\n");
			}

			//System.out.println("End of solving.\n");
			//System.out.println("Total time spent:"+(System.currentTimeMillis()-total_time)+" ms.");
			/* reset the variables */
			Vars.resetContext();
			
		}
		catch (Z3Exception e){
			System.err.println("Error:"+e.getMessage());
		}
		catch (IOException e){
			e.printStackTrace();System.err.println("SMT2 file dumped failed.");
		}
		catch (java.lang.Exception e){
			System.err.println("Error:"+e.getMessage());
		}	
		
		
	}

	public static FOFormula SimpleTest0(){
		List<Var> vars = new ArrayList<Var>();
		System.out.println("Testing formula...");
		FOFormula formula = new FOFormula();
		for (int i=0;i<6;i++){
			Var v = new Var("v"+i,new BoolType());
			ConstDecl c = new ConstDecl(v);
			vars.add(v);
		}
		
		formula.addExpression(FOFormula.Some(vars));
		return formula;
	}
	

	public static FOFormula SimpleTest1(){
		Var x1 = new Var("x1",new IntType());
		ConstDecl c3 = new ConstDecl(x1);
			
		ArithmeticExpression e1 = new ArithmeticExpression(Arithmetic.GREATER,x1,new NumLiteral(10));
		ArithmeticExpression e2 = new ArithmeticExpression(Arithmetic.LESS,x1,new NumLiteral(2000));
		BinaryExpression b = new BinaryExpression(Connective.AND,e1,e2);
		
		return new FOFormula(b);
	}

	public static FOFormula SimpleTest2(){
			FOFormula fof = new FOFormula();
			Var e1 = new Var("e1", new BoolType());
			Var e2 = new Var("e2",new BoolType());
			Var e3 = new Var("e3",new BoolType());
			Var e4 = new Var("e4",new BoolType());
			Var e5 = new Var("e5",new BoolType());
			Var e6 = new Var("e6",new BoolType());
			Var e7 = new Var("e7",new BoolType());
			Var e8 = new Var("e8",new BoolType());

			ConstDecl c1 = new ConstDecl(e1);
			ConstDecl c2 = new ConstDecl(e2);
			ConstDecl c3 = new ConstDecl(e3);
			ConstDecl c4 = new ConstDecl(e4);
			ConstDecl c5 = new ConstDecl(e5);
			ConstDecl c6 = new ConstDecl(e6);
			ConstDecl c7 = new ConstDecl(e7);
			ConstDecl c8 = new ConstDecl(e8);

			Var t1 = new Var("t1",new IntType());
			Var t2 = new Var("t2",new IntType());
			Var t3 = new Var("t3",new IntType());
			Var t4 = new Var("t4",new IntType());
			Var t5 = new Var("t5",new IntType());
			Var t6 = new Var("t6",new IntType());
			Var t7 = new Var("t7",new IntType());
			Var t8 = new Var("t8",new IntType());

			ConstDecl c10 = new ConstDecl(t1);
			ConstDecl c20 = new ConstDecl(t2);
			ConstDecl c30 = new ConstDecl(t3);
			ConstDecl c40 = new ConstDecl(t4);
			ConstDecl c50 = new ConstDecl(t5);
			ConstDecl c60 = new ConstDecl(t6);
			ConstDecl c70 = new ConstDecl(t7);
			ConstDecl c80 = new ConstDecl(t8);

			fof.addExpression(FOFormula.Range(0,1,t1));
			fof.addExpression(FOFormula.Range(0,1,t2));
			fof.addExpression(FOFormula.Range(0,1,t3));
			fof.addExpression(FOFormula.Range(0,1,t4));
			fof.addExpression(FOFormula.Range(0,1,t5));
			fof.addExpression(FOFormula.Range(0,1,t6));
			fof.addExpression(FOFormula.Range(0,1,t7));
			fof.addExpression(FOFormula.Range(0,1,t8));

			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t1,new NumLiteral(1)),
						e1));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t2,new NumLiteral(1)),
						e2));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t3,new NumLiteral(1)),
						e3));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t4,new NumLiteral(1)),
						e4));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t5,new NumLiteral(1)),
						e5));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t6,new NumLiteral(1)),
						e6));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t7,new NumLiteral(1)),
						e7));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t8,new NumLiteral(1)),
						e8));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t1,new NumLiteral(0)),
						new NegFun(e1)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t2,new NumLiteral(0)),
						new NegFun(e2)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t3,new NumLiteral(0)),
						new NegFun(e3)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t4,new NumLiteral(0)),
						new NegFun(e4)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t5,new NumLiteral(0)),
						new NegFun(e5)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t6,new NumLiteral(0)),
						new NegFun(e6)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t7,new NumLiteral(0)),
						new NegFun(e7)));
			fof.addExpression(new BinaryExpression(Connective.IMPLIES,
						new ArithmeticExpression(Arithmetic.EQUAL,t8,new NumLiteral(0)),
						new NegFun(e8)));

			fof.addExpression(FOFormula.Some(e1,e2,e3,e4));
			fof.addExpression(FOFormula.Some(e5,e6,e7,e8));

			fof.addExpression(FOFormula.join(Connective.OR,FOFormula.None(e1,e5),FOFormula.OneOnly(e1,e5)));
			fof.addExpression(FOFormula.join(Connective.OR,FOFormula.None(e2,e6),FOFormula.OneOnly(e2,e6)));
			fof.addExpression(FOFormula.join(Connective.OR,FOFormula.None(e3,e7),FOFormula.OneOnly(e3,e7)));
			fof.addExpression(FOFormula.join(Connective.OR,FOFormula.None(e4,e8),FOFormula.OneOnly(e4,e8)));

			ArithmeticExpression arith0 = FOFormula.join(Arithmetic.PLUS,t1,t2,t3,t4);
			ArithmeticExpression arith1 = FOFormula.join(Arithmetic.PLUS,t5,t6,t7,t8);

			ArithmeticExpression arithA = new ArithmeticExpression(Arithmetic.EQUAL,arith0,new NumLiteral(1));
			ArithmeticExpression arithB = new ArithmeticExpression(Arithmetic.EQUAL,arith1,new NumLiteral(1));
			
			ArithmeticExpression arith2 = FOFormula.join(Arithmetic.PLUS,t1,t5);
			ArithmeticExpression arith3 = FOFormula.join(Arithmetic.PLUS,t2,t6);
			ArithmeticExpression arith4 = FOFormula.join(Arithmetic.PLUS,t3,t7);
			ArithmeticExpression arith5 = FOFormula.join(Arithmetic.PLUS,t4,t8);

			ArithmeticExpression arith6 = new ArithmeticExpression(Arithmetic.EQUAL,arith2,new NumLiteral(1));
			ArithmeticExpression arith7 = new ArithmeticExpression(Arithmetic.EQUAL,arith3,new NumLiteral(1));
			ArithmeticExpression arith8 = new ArithmeticExpression(Arithmetic.EQUAL,arith4,new NumLiteral(1));
			ArithmeticExpression arith9 = new ArithmeticExpression(Arithmetic.EQUAL,arith5,new NumLiteral(1));
			
			fof.addExpression(FOFormula.join(Connective.OR,arith6,arith7,arith8,arith9));
			fof.addExpression(new BinaryExpression(Connective.XOR,arithB,arithA));

			return fof;
	}

	public static FOFormula SimpleTest3(){
		/* row 1 */
		Var v1 = new Var("e1",new BoolType());
		Var v2 = new Var("e2",new BoolType());
		Var v3 = new Var("e3",new BoolType());
		/* row 2 */
		Var v4 = new Var("e4",new BoolType());
		Var v5 = new Var("e5",new BoolType());
		Var v6 = new Var("e6",new BoolType());
		/* row 3 */		
		Var v7 = new Var("e7",new BoolType());
		Var v8 = new Var("e8",new BoolType());
		Var v9 = new Var("e9",new BoolType());		

		ConstDecl c1 = new ConstDecl(v1);
		ConstDecl c2 = new ConstDecl(v2);
		ConstDecl c3 = new ConstDecl(v3);
		ConstDecl c4 = new ConstDecl(v4);	
		ConstDecl c5 = new ConstDecl(v5);
		ConstDecl c6 = new ConstDecl(v6);
		ConstDecl c7 = new ConstDecl(v7);	
		ConstDecl c8 = new ConstDecl(v8);
		ConstDecl c9 = new ConstDecl(v9);

		//BinaryExpression b1 = new BinaryExpression(Connective.AND,FOFormula.Some(v1,v2,v3),FOFormula.None(v4,v5,v6));
		//BinaryExpression b2 = new BinaryExpression(Connective.AND,FOFormula.Some(v4,v5,v6),FOFormula.None(v1,v2,v3));
		
		BinaryExpression b1 = FOFormula.join(Connective.AND,FOFormula.Some(v1,v2,v3),FOFormula.None(v4,v5,v6),FOFormula.None(v7,v8,v9));
		BinaryExpression b2 = FOFormula.join(Connective.AND,FOFormula.None(v1,v2,v3),FOFormula.Some(v4,v5,v6),FOFormula.None(v7,v8,v9));
		BinaryExpression b3 = FOFormula.join(Connective.AND,FOFormula.None(v1,v2,v3),FOFormula.None(v4,v5,v6),FOFormula.Some(v7,v8,v9));

		return new FOFormula(FOFormula.join(Connective.OR,b1,b2,b3));
	}

	/*@TODO: update each constant by using Z3-native APIs*/
	private static BoolExpr Negate(List<BoolExpr> exprs, boolean debug) throws Z3Exception{
		BoolExpr e[] = new BoolExpr[exprs.size()];
		for (int i=0;i<exprs.size();i++) e[i] = exprs.get(i);
		//exprs.toArray(e);
		
		if (debug){
			for (int i=0;i<e.length;i++)
				System.out.print(e[i]+",");
			System.out.println();
		}
		return ctx.mkNot(ctx.mkAnd(e));
	}
	
	public static FOFormula ComplexTest1(int share[],int diff[]){
		FOFormula formulas= new FOFormula();
		int id = 1;
		int r=4,c=4;
		Var edges[][] = new Var[r][c];
		List <Var> f1 = new ArrayList<Var>();
		List <Expression> f2 = new ArrayList<Expression>();
		List <Expression> f3 = new ArrayList<Expression>();	

		for (int i=0;i<r;i++){
			for (int j=0;j<c;j++){
				Var e = new Var("e"+id++,new BoolType());
				edges[i][j]=e;
				ConstDecl dec = new ConstDecl(e);
			}
		}

		for (int i=0;i<r;i++){
			for (int j=0;j<c;j++){	
				f1.add(edges[i][j]);
				for (int k=0;k<share.length;k++)
					if (j==share[k]-1) f2.add(edges[i][j]);
			}
			
			for (int l=0;l<f2.size();l++)
				f1.remove(f2.get(l));

			Expression f = (f2.size()>1) ? FOFormula.join(Connective.AND,f2) : f2.get(0);
			f3.add(f);
			if (f1.size()>0)
				formulas.addExpression(new BinaryExpression(Connective.IMPLIES,f,FOFormula.None(f1)));
			f1.clear();f2.clear();
		}

		formulas.addExpression(FOFormula.SomeExprs(f3));
		

		f1.clear();f2.clear();
		for (int k=0;k<diff.length;k++){
			for (int i=0;i<r;i++){
				for (int j=0;j<c;j++){
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
			for (int j=0;j<r;j++){
				f2.add(edges[j][diff[i]-1]);
			}
			formulas.addExpression(FOFormula.SomeExprs(f2));
			f2.clear();
		}
			
		//System.out.println(formulas);
		return formulas;
	}


	public static FOFormula ComplexTest2(int...diff){
		FOFormula formulas= new FOFormula();
		int id = 1;
		int r=3,c=4;
		Var edges[][] = new Var[r][c];		
		List <Var> f1 = new ArrayList<Var>();
		List <Var> f2 = new ArrayList<Var>();

		for (int i=0;i<r;i++){
			for (int j=0;j<c;j++){
				Var e = new Var("e"+id++,new BoolType());
				edges[i][j]=e;
				ConstDecl dec = new ConstDecl(e);
			}
		}

		for (int k=0;k<diff.length;k++){
			for (int i=0;i<r;i++){
				for (int j=0;j<c;j++){
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
			for (int j=0;j<r;j++){
				f2.add(edges[j][diff[i]-1]);
			}
			formulas.addExpression(FOFormula.Some(f2));
			f2.clear();
		}
		
		//solve(formulas);
		//System.out.println("Solving is finished.");
		return formulas;
	}

}
