/**
 *	This file contains a collection of testing procedures
 *  for testing my engine: ATONGMU
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 *
 */

package test;
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


public class test1{

	public static void main (String args[]){
		//testOneOnly();
		testOneStarBI();
	}

	public static void testOneOnly(){
		System.out.println("#########Testing Selecting One Among Many#########");
		FOFormula fof= new FOFormula();	

		Var a = new Var("a",new IntType());
		//Var x0 = new Var("x0",new BoolType());	
		//Var x1 = new Var("x1",new BoolType());
		//Var x2 = new Var("x2",new BoolType());
	
		ConstDecl c1 = new ConstDecl(a);
		//ConstDecl c2 = new ConstDecl(x0);
		//ConstDecl c3 = new ConstDecl(x1);
		//ConstDecl c4 = new ConstDecl(x2);		

		ArithmeticExpression e1 = new ArithmeticExpression(Arithmetic.GREATER,a,new NumLiteral(0));
		ArithmeticExpression e2 = new ArithmeticExpression(Arithmetic.LESS,a,new NumLiteral(0));
		ArithmeticExpression e3 = new ArithmeticExpression(Arithmetic.EQUAL,a,new NumLiteral(0));

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);			
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			fof.addExpression(FOFormula.OneOnlyExprs(e1,e2,e3));
			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				invoker.showVars();
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (UnknownException e){
			e.printErrMessage(" formula cannot be decided.");
		}
		catch (UnsatException e){
			System.err.println("unsat exception.");
			e.printErrMessage(" formula is not satisfiable.");
		}
	}

	public static void testOneStarBI(){
		System.out.println("#########Testing Selecting One Row Only#########");
		long time = System.currentTimeMillis();
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

		FOFormula fof= new FOFormula();	
		//BinaryExpression b1 = new BinaryExpression(Connective.AND,fof.Some(v1,v2,v3),fof.None(v4,v5,v6));
		//BinaryExpression b2 = new BinaryExpression(Connective.AND,fof.Some(v4,v5,v6),fof.None(v1,v2,v3));
		BinaryExpression b1 = FOFormula.join(Connective.AND,fof.Some(v1,v2,v3),fof.None(v4,v5,v6),fof.None(v7,v8,v9));
		BinaryExpression b2 = FOFormula.join(Connective.AND,fof.None(v1,v2,v3),fof.Some(v4,v5,v6),fof.None(v7,v8,v9));
		BinaryExpression b3 = FOFormula.join(Connective.AND,fof.None(v1,v2,v3),fof.None(v4,v5,v6),fof.Some(v7,v8,v9));
		
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);			
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			fof.addExpression(FOFormula.join(Connective.OR,b1,b2,b3));
			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				invoker.showVars();
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
			System.out.println("Total time spent:"+ (System.currentTimeMillis()-time)+" ms.");
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (UnknownException e){
			e.printErrMessage(" formula cannot be decided.");
		}
		catch (UnsatException e){
			System.err.println("unsat exception.");
			e.printErrMessage(" formula is not satisfiable.");
		}
		
	}
	
}
