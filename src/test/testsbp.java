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

public class testsbp{


	public static void main (String args[]){
		System.out.println("***********Testing SBP***********");
		testsbp2();
	}
	
	public static void testsbp1(){
		System.out.println("Running case1...");
		FOFormula fof= new FOFormula();		
		Var a1 = new Var("a1",new BoolType());
		Var a2 = new Var("a2",new BoolType());
		Var a3 = new Var("a3",new BoolType());
		Var b1 = new Var("b1",new BoolType());
		Var b2 = new Var("b2",new BoolType());
		Var b3 = new Var("b3", new BoolType());

		Var e1 = new Var("e1", new BoolType());
		Var e2 = new Var("e2", new BoolType());
		Var e3 = new Var("e3", new BoolType());
		Var e4 = new Var("e4", new BoolType());
		Var e5 = new Var("e5", new BoolType());
		Var e6 = new Var("e6", new BoolType());
		Var e7 = new Var("e7", new BoolType());
		Var e8 = new Var("e8", new BoolType());
		Var e9 = new Var("e9", new BoolType());

		ConstDecl c1 = new ConstDecl(e1);
		ConstDecl c2 = new ConstDecl(e2);
		ConstDecl c3 = new ConstDecl(e3);
		ConstDecl c8 = new ConstDecl(e4);
		ConstDecl c9 = new ConstDecl(e5);
		ConstDecl c10 = new ConstDecl(e6);
		ConstDecl c11 = new ConstDecl(e7);
		ConstDecl c12 = new ConstDecl(e8);
		ConstDecl c13 = new ConstDecl(e9);
		ConstDecl c4 = new ConstDecl(a1);
		ConstDecl c7 = new ConstDecl(a2);
		ConstDecl c14 = new ConstDecl(a3);
		ConstDecl c5 = new ConstDecl(b1);
		ConstDecl c6 = new ConstDecl(b2);
		ConstDecl c15 = new ConstDecl(b3);
		
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e1,
						new BinaryExpression(Connective.AND,a1,b1)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e2,
						new BinaryExpression(Connective.AND,a1,b2)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e3,
						new BinaryExpression(Connective.AND,a1,b3)));

		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e4,
						new BinaryExpression(Connective.AND,a2,b1)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e5,
						new BinaryExpression(Connective.AND,a2,b2)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e6,
						new BinaryExpression(Connective.AND,a2,b3)));

		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e7,
						new BinaryExpression(Connective.AND,a3,b1)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e8,
						new BinaryExpression(Connective.AND,a3,b2)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e9,
						new BinaryExpression(Connective.AND,a3,b3)));

		fof.addExpression(FOFormula.join(Connective.AND,
				FOFormula.join(Connective.OR,e1,e2,e3),
				FOFormula.join(Connective.OR,e4,e5,e6),
				FOFormula.join(Connective.OR,e7,e8,e9)));
		
		List<Var> o = new ArrayList<Var>();
		List<Var> p = new ArrayList<Var>();
		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);o.add(e5);o.add(e6);o.add(e7);o.add(e8);o.add(e9);
		p.add(e1);p.add(e2);p.add(e3);p.add(e7);p.add(e8);p.add(e9);p.add(e4);p.add(e5);p.add(e6);
		fof.addExpression(buildeq(o,p));

		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);o.add(e5);o.add(e6);o.add(e7);o.add(e8);o.add(e9);
		p.add(e1);p.add(e3);p.add(e2);p.add(e4);p.add(e6);p.add(e5);p.add(e7);p.add(e9);p.add(e8);
		fof.addExpression(buildeq(o,p));

		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);o.add(e5);o.add(e6);o.add(e7);o.add(e8);o.add(e9);
		p.add(e2);p.add(e1);p.add(e3);p.add(e5);p.add(e4);p.add(e6);p.add(e8);p.add(e7);p.add(e9);
		fof.addExpression(buildeq(o,p));

		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);o.add(e5);o.add(e6);o.add(e7);o.add(e8);o.add(e9);
		p.add(e4);p.add(e5);p.add(e6);p.add(e1);p.add(e2);p.add(e3);p.add(e7);p.add(e8);p.add(e9);
		fof.addExpression(buildeq(o,p));
		
		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);o.add(e5);o.add(e6);o.add(e7);o.add(e8);o.add(e9);
		p.add(e1);p.add(e4);p.add(e7);p.add(e2);p.add(e5);p.add(e8);p.add(e3);p.add(e6);p.add(e9);
		fof.addExpression(buildeq(o,p));

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				//System.out.println(e1+", "+e2+", "+e3+", "+a1+", "+b1+", "+b2+", "+b3);
				//System.out.println(a1+", "+b1+", "+b2+", "+b3);
				System.out.println(a1+", "+a2+", "+b1+", "+b2+", "+e1+", "+e2+", "+e3+", "+e4);
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
			System.out.println("Case 1 [success]");
		}
		catch (IOException e){e.printStackTrace();System.err.println("Case 1 [failed]");}
		catch (UnknownException e){e.printErrMessage("Case 1 [failed] formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage("Case 1 [failed] formula is not satisfiable.");}
		
	}

	public static void testsbp2(){
		System.out.println("Running case 2...");
		FOFormula fof= new FOFormula();		
		Var a1 = new Var("a1", new BoolType());
		Var a2 = new Var("a2", new BoolType());
		Var b1 = new Var("b1", new BoolType());
		Var b2 = new Var("b2", new BoolType());

		Var e1 = new Var("e1", new BoolType());
		Var e2 = new Var("e2", new BoolType());
		Var e3 = new Var("e3", new BoolType());
		Var e4 = new Var("e4", new BoolType());

		ConstDecl c1 = new ConstDecl(a1);
		ConstDecl c2 = new ConstDecl(a2);
		ConstDecl c3 = new ConstDecl(b1);
		ConstDecl c4 = new ConstDecl(b2);
		ConstDecl c5 = new ConstDecl(e1);
		ConstDecl c6 = new ConstDecl(e2);
		ConstDecl c7 = new ConstDecl(e3);
		ConstDecl c8 = new ConstDecl(e4);
		
		List<Var> o = new ArrayList<Var>();	
		List<Var> p = new ArrayList<Var>();

		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e1,
						new BinaryExpression(Connective.AND,a1,b1)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e2,
						new BinaryExpression(Connective.AND,a1,b2)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e3,
						new BinaryExpression(Connective.AND,a2,b1)));
		fof.addExpression(new BinaryExpression(Connective.IMPLIES,e4,
						new BinaryExpression(Connective.AND,a2,b2)));

		fof.addExpression(FOFormula.join(Connective.AND,
				FOFormula.join(Connective.OR,e1,e2),
				FOFormula.join(Connective.OR,e3,e4)));

		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);
		p.add(e3);p.add(e4);p.add(e1);p.add(e2);
		fof.addExpression(buildeq(o,p));

		o.clear();p.clear();
		o.add(e1);o.add(e2);o.add(e3);o.add(e4);
		p.add(e2);p.add(e1);p.add(e4);p.add(e3);
		fof.addExpression(buildeq(o,p));

		/*o.clear();p.clear();
		o.add(a1);o.add(a2);o.add(b1);o.add(b2);
		p.add(b2);p.add(b2);p.add(a1);p.add(a2);
		fof.addExpression(buildeq(o,p));*/

		/*o.clear();p.clear();
		o.add(a1);o.add(a2);o.add(b1);o.add(b2);
		p.add(b2);p.add(b2);p.add(a1);p.add(a2);
		fof.addExpression(buildeq(o,p));*/
		

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				System.out.println(a1+", "+a2+", "+b1+", "+b2+", "+e1+", "+e2+", "+e3+", "+e4);
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			System.out.println("Number of the solutions found:"+solution);
			System.out.println("Case 2 [success]");
		}
		catch (IOException e){e.printStackTrace();System.err.println("Case 2 [failed]");}
		catch (UnknownException e){e.printErrMessage("Case 2 [failed] formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage("Case 2 [failed] formula is not satisfiable.");}
		
	}

	public static Expression buildeq(List<Var> origin, List<Var> perm){
		Expression prev = new BinaryExpression(Connective.IMPLIES,origin.get(0),perm.get(0));
		Expression p = new BinaryExpression(Connective.EQUAL,new BoolLiteral(true),new BoolLiteral(true));
		
		List<Expression> sbp = new ArrayList<Expression>();
		sbp.add(new BinaryExpression(Connective.AND,prev,p));

		for (int i=1;i<origin.size();i++){
			p = new BinaryExpression(Connective.AND,p,
					new BinaryExpression(Connective.EQUAL,origin.get(i-1),perm.get(i-1)));
			Expression curr = new BinaryExpression(Connective.IMPLIES,origin.get(i),perm.get(i));
			sbp.add(new BinaryExpression(Connective.IMPLIES,p,curr));
		}

		for (int i=0;i<sbp.size();i++) System.out.println((i+1)+": "+sbp.get(i));
		return FOFormula.join(Connective.AND,sbp);
	}

}
