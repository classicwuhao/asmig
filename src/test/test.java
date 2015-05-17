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
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.Type;
import atongmu.atg.Node;
import atongmu.atg.NodeType;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;
import atongmu.atg.Factory;
import atongmu.value.Value;
import atongmu.value.BoolValue;
import atongmu.value.IntValue;
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
import atongmu.ast.Vars;
import atongmu.err.DuplicatedDeclaration;
import atongmu.err.EmptyFormulaException;
import atongmu.err.UnsatException;
import atongmu.err.UnknownException;
import atongmu.err.VarExistsException;
import atongmu.ast.visitor.AbstractVisitor;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;

public class test{

	public static void main (String args[]){

		System.out.println("<Testing program for atongmu>");
		long time=System.currentTimeMillis();
		//testBinaryFormula();
		//testJoinOp();
		//testArithmeticExpression();
		//testBool1();
		//testBool2();
		//testIncSolve();
		testFormula();
		time=System.currentTimeMillis()-time;
		System.out.println("<End of testing "+ time+ " milliseconds >");
	}

	public static void testBinaryFormula(){
		System.out.println("#########Testing binary formula#########");
		System.out.println("(x1=1 or x1=0) and (x2=1)");

		Var var1 = new Var("x1", new IntType());
		ConstDecl c1 = new ConstDecl(var1);
		Var var2 = new Var("x2",new IntType());

		ConstDecl c2 = new ConstDecl(var2);
		System.out.println(c1);
		System.out.println(c2);
	
		NumLiteral n1 = new NumLiteral(1);
		NumLiteral n2 = new NumLiteral(0);
	
		BinaryExpression b1 = new BinaryExpression(Connective.EQUAL,var1,n1);
		BinaryExpression b2 = new BinaryExpression(Connective.EQUAL,var1,n2);
		BinaryExpression b3 = new BinaryExpression(Connective.EQUAL,var2,n1);
		BinaryExpression b4 = new BinaryExpression(Connective.OR,b1,b2);
		BinaryExpression b5 = new BinaryExpression(Connective.AND,b4,b3);
		System.out.println(b5);
	}

	public static void testJoinOp(){
		
		System.out.println("#########Testing join operation#########");
		System.out.println("not (x1=1 and x2=1 and x3=1 and x4=1)");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		SMT2Writer writer = new SMT2Writer(new PrintWriter(System.out,true),fof);
		
		Var var1 = new Var("x1", new IntType());
		Var var2 = new Var("x2", new IntType());
		Var var3 = new Var("x3", new IntType());
		Var var4 = new Var("x4", new IntType());
		
		ConstDecl c1 = new ConstDecl(var1);
		ConstDecl c2 = new ConstDecl(var2);
		ConstDecl c3 = new ConstDecl(var3);
		ConstDecl c4 = new ConstDecl(var4);
		//System.out.println(c1);
		//System.out.println(c2);
		//System.out.println(c3);
		//System.out.println(c4);
		NumLiteral n1 = new NumLiteral(-1);
		
		BinaryExpression b1 = new BinaryExpression(Connective.EQUAL,var1,n1);
		BinaryExpression b2 = new BinaryExpression(Connective.EQUAL,var2,n1);
		BinaryExpression b3 = new BinaryExpression(Connective.EQUAL,var3,n1);
		BinaryExpression b4 = new BinaryExpression(Connective.EQUAL,var4,n1);
		BinaryExpression b = FOFormula.join(Connective.AND,b1,b2,b3,b4);

		NegFun n = new NegFun(b);
		fof.addExpression(n);
		System.out.println(n);
		writer.Traverse();
	
		/*catch (EmptyFormulaException e){
			e.setFormula(fof);
			e.printErrMessage(fof.CheckEmpty2String());
		}*/
	//and[and[and[and[=[<x1:(Int)>,1],=[<x2:(Int)>,1]],=[<x2:(Int)>,1]],=[<x3:(Int)>,1]],=[<x4:(Int)>,1]]		
	}

	public static void testArithmeticExpression(){
		System.out.println("#########Testing Arithmetic Expression#########");
		System.out.println("(x1 > 20) and (x1 < 66) and (x2 >= 100) and (x2 =< 900)");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		SMT2Writer writer = new SMT2Writer(new PrintWriter(System.out,true),fof);

		Var x1 = new Var("x1", new IntType());
		Var x2 = new Var("x2", new IntType());
		ConstDecl c1 = new ConstDecl(x1);
		ConstDecl c2 = new ConstDecl(x2);
		NumLiteral n1 = new NumLiteral(20);
		NumLiteral n2 = new NumLiteral(66);
		NumLiteral n3 = new NumLiteral(100);
		NumLiteral n4 = new NumLiteral(900);

		
		ArithmeticExpression e1 = new ArithmeticExpression(Arithmetic.GREATER,x1,n1);
		ArithmeticExpression e2 = new ArithmeticExpression(Arithmetic.LESS,x1,n2);
		ArithmeticExpression e3 = new ArithmeticExpression(Arithmetic.GREATER_EQUAL,x2,n3);
		ArithmeticExpression e4 = new ArithmeticExpression(Arithmetic.LESS_EQUAL,x2,n4);

		BinaryExpression b = FOFormula.join(Connective.AND,e1,e2);
		BinaryExpression b1 = FOFormula.join(Connective.AND,e3,e4);
		fof.addExpression(b);
		fof.addExpression(b1);
		writer.Traverse();
		
		/*catch (EmptyFormulaException e){
			e.setFormula(fof);
			e.printErrMessage(fof.CheckEmpty2String());
		}*/
	}

	public static void testBool1(){
		System.out.println("#########Testing Bool Proof(1)#########");
		System.out.println(" p and q -> r");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		SMT2Writer writer = new SMT2Writer(new PrintWriter(System.out,true),fof);

		Var p = new Var("p", new BoolType());
		Var q = new Var("q", new BoolType());
		Var r = new Var("r", new BoolType());

		ConstDecl c1 = new ConstDecl(p);
		ConstDecl c2 = new ConstDecl(q);
		ConstDecl c3 = new ConstDecl(r);

		BinaryExpression b1 = new BinaryExpression(Connective.AND,p,q);
		BinaryExpression b2 = FOFormula.join(Connective.IMPLIES,b1,r);
		BinaryExpression b3 = new BinaryExpression(Connective.EQUAL,p,new BoolLiteral(BoolValue.TRUE));
		fof.addExpression(b2);
		fof.addExpression(b3);
		writer.Traverse();

	}

	public static void testBool2(){
		System.out.println("#########Testing Bool Proof(2)#########");
		System.out.println("(not p) and q -> p and (q or not r)");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		
		Var e1 = new Var("e1", new BoolType());
		Var e2 = new Var("e2", new BoolType());
		Var e3 = new Var("e3", new BoolType());
		Var e4 = new Var("e4", new BoolType());

		ConstDecl c1 = new ConstDecl(e1);
		ConstDecl c2 = new ConstDecl(e2);
		ConstDecl c3 = new ConstDecl(e3);
		ConstDecl c4 = new ConstDecl(e4);
		
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);

			BinaryExpression b1 = fof.join(Connective.XOR,e1,e2,e3,e4);
			//BinaryExpression b2 = fof.join(Connective.AND,e1,e2,e3);
			//NegFun n = new NegFun(b2);
			//fof.addExpression(new BinaryExpression(Connective.OR,b1,n));

			fof.addExpression(b1);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				invoker.showVars();
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);

		}
		/*catch(MissingFormulaException e){
			e.printErrMessage("< 2");
		}*/
		/*catch (EmptyFormulaException e){
			e.setFormula(fof);
			e.printErrMessage(fof.CheckEmpty2String());
		}*/
		catch (IOException e){
			e.printStackTrace();
		}
		catch (UnknownException e){
			e.printErrMessage(" formula cannot be decided.");
		}
		catch (UnsatException e){
			e.printErrMessage(" formula is not satisfiable.");
		}
	}

	public static void testIncSolve(){
		System.out.println("#########Testing inSolve(1)#########");
		System.out.println("(x1 > 10) and (x1 < 200)");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			Var b1 = new Var("b1", new BoolType());
			Var b2 = new Var("b2",new BoolType());
			Var x1 = new Var("x1",new IntType());
			ConstDecl c1 = new ConstDecl(b1);
			ConstDecl c2 = new ConstDecl(b2);
			ConstDecl c3 = new ConstDecl(x1);
			
			ArithmeticExpression e1 = new ArithmeticExpression(Arithmetic.GREATER,x1,new NumLiteral(10));
			ArithmeticExpression e2 = new ArithmeticExpression(Arithmetic.LESS,x1,new NumLiteral(20));
			BinaryExpression b = new BinaryExpression(Connective.AND,e1,e2);
						
			fof.addExpression (b);
			//fof.addExpression(new BinaryExpression(Connective.EQUAL,x1,new NumLiteral(30)));
			
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			//invoker.setSolverPath("");
			//Z3
			//SMTINTERPOL
			SMT2Writer copywriter = new SMT2Writer(new PrintWriter(
			new FileWriter("tmp.smt2")),writer.getFormula().clone());

			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				
				invoker.showVars();
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
						
			//invoker.setFilePath("tmp.smt2");
			//invoker.invoke();
			//invoker.incSolve();
			//invoker.showVars();
		}
		
		/*catch (EmptyFormulaException e){
			e.setFormula(fof);
			e.printErrMessage(fof.CheckEmpty2String());
		}*/
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

	private static void testFormula(){
		System.out.println("#########Testing Formula(1)#########");
		Vars.resetContext();
		FOFormula fof= new FOFormula();
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
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
			//fof.addExpression(arithB);

			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			SMT2Writer copywriter = new SMT2Writer(new PrintWriter(
			new FileWriter("test.smt2")),writer.getFormula().clone());

			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				invoker.showVars();	
				fof.addExpression(FOFormula.NegVars());
				solution++;
			}
			//invoker.releaseFiles();
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
	
}
