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
import atongmu.translator.*;
import atongmu.interpreter.*;

public class testc{
	
	public static void main (String args[]){
		//buildModel();
		test1();
	}


	public static void test1(){
		FOFormula fof= new FOFormula();
		Var b1 = new Var("b1",new IntType());
		Var b2 = new Var("b2",new IntType());
		Var b3 = new Var("b3",new IntType());
		Var t1 = new Var("t1",new IntType());
		Var t2 = new Var("t2",new IntType());
		Var t3 = new Var("t3",new IntType());

		ConstDecl c1 = new ConstDecl(b1);
		ConstDecl c2 = new ConstDecl(b2);
		ConstDecl c3 = new ConstDecl(b3);
		ConstDecl c4 = new ConstDecl(t1);
		ConstDecl c5 = new ConstDecl(t2);
		ConstDecl c6 = new ConstDecl(t3);
		
		fof.addExpression(FOFormula.Range(1,3,b1,b2,b3,t1,t2,t3));
		fof.addExpression(FOFormula.Same(b1,b2,b3));
		fof.addExpression(FOFormula.Same(t1,t2,t3));
		//fof.addExpression(FOFormula.Unique(b1,b2,b3));
		//fof.addExpression(FOFormula.Unique(t1,t2,t3));

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				//NegFun neg = FOFormula.NegVars();
				fof.addExpression(FOFormula.NegVars(b1,b2,b3,t1,t2,t3));
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
		}
		catch (IOException e){e.printStackTrace();System.err.println("failed.");}
		catch (UnknownException e){e.printErrMessage("formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage("formula is not satisfiable.");}

		
	}


	public static void buildModel(){
		FOFormula fof= new FOFormula();

		Var f1 = new Var("f1",new IntType());
		Var f2 = new Var("f2",new IntType());
		//Var f3 = new Var("f3",new IntType());
		Var a1 = new Var("a1",new BoolType());
		Var a2 = new Var("a2",new BoolType());
		//Var a3 = new Var("a3",new BoolType());
		Var b1 = new Var("b1",new IntType());
		Var b2 = new Var("b2",new IntType());

		ConstDecl c1 = new ConstDecl(f1);
		ConstDecl c2 = new ConstDecl(f2);
		ConstDecl c3 = new ConstDecl(a1);
		ConstDecl c4 = new ConstDecl(a2);
		ConstDecl c5 = new ConstDecl(b1);
		ConstDecl c6 = new ConstDecl(b2);
		//ConstDecl c7 = new ConstDecl(f3);
		//ConstDecl c8 = new ConstDecl(a3);
		
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,new NegFun(a1), 
				new ArithmeticExpression(Arithmetic.EQUAL,f1,new NumLiteral(-1))));

		fof.addExpression(new BinaryExpression(Connective.IMPLIES,new NegFun(a2), 
				new ArithmeticExpression(Arithmetic.EQUAL,f2,new NumLiteral(-1))));

		fof.addExpression(FOFormula.Range(1,3,b1,b2));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b1,new NumLiteral(1)),
			new ArithmeticExpression(Arithmetic.LESS,f1,new NumLiteral(0))));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b1,new NumLiteral(2)),
			new ArithmeticExpression(Arithmetic.EQUAL,f1,new NumLiteral(0))));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b1,new NumLiteral(3)),
		
			new ArithmeticExpression(Arithmetic.GREATER,f2,new NumLiteral(0))));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b2,new NumLiteral(1)),
			new ArithmeticExpression(Arithmetic.LESS,f2,new NumLiteral(0))));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b2,new NumLiteral(2)),
			new ArithmeticExpression(Arithmetic.EQUAL,f2,new NumLiteral(0))));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,b2,new NumLiteral(3)),
			new ArithmeticExpression(Arithmetic.GREATER,f2,new NumLiteral(0))));

		fof.addExpression(new ArithmeticExpression(Arithmetic.EQUAL,b1,b2));
		fof.addExpression(new BinaryExpression(Connective.AND,a1,a2));
		fof.addExpression(FOFormula.Unique(f1,f2));
		
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				//NegFun neg = FOFormula.NegVars();
				fof.addExpression(FOFormula.NegVars(b1,b2));
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
		}
		catch (IOException e){e.printStackTrace();System.err.println("failed.");}
		catch (UnknownException e){e.printErrMessage("Case 1 [failed] formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage("Case 1 [failed] formula is not satisfiable.");}
		
	}

}
