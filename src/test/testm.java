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

public class testm{


	public static void main (String args[]){
		Select2OutOf5();
	}

	public static void Select2OutOf5(){
		FOFormula fof= new FOFormula();
		Var t1 = new Var("t1",new IntType());
		Var t2 = new Var("t2",new IntType());
		Var t3 = new Var("t3",new IntType());
		Var t4 = new Var("t4",new IntType());
		Var t5 = new Var("t5",new IntType());

		ConstDecl c1 = new ConstDecl(t1);
		ConstDecl c2 = new ConstDecl(t2);
		ConstDecl c3 = new ConstDecl(t3);
		ConstDecl c4 = new ConstDecl(t4);
		ConstDecl c5 = new ConstDecl(t5);

		fof.addExpression(FOFormula.Range(0,1,t1,t2,t3,t4,t5));
		fof.addExpression(new ArithmeticExpression(Arithmetic.EQUAL,FOFormula.join(Arithmetic.PLUS,t1,t2,t3,t4,t5)
		,new NumLiteral(2)));

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){	
				fof.addExpression(FOFormula.NegVars());
				System.out.println("t1:"+t1+",t2:"+t2+",t3:"+t3+",t4:"+t4+",t5:"+t5);
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
		}
		catch (IOException e){e.printStackTrace();System.err.println("failed.");}
		catch (UnknownException e){e.printErrMessage("formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage("formula is not satisfiable.");}
		
	}

}
