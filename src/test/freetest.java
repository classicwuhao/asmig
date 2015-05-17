/**
 *	Arbitary testing for your formulas.
 *	Hao Wu
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

public class freetest{

	public static void main (String args[]){
		System.out.println("Solving test case 0...");
		int share[]=new int[3]; int diff[]=new int[1];
		share[0]=1;share[1]=4;share[2]=3;diff[0]=2;//diff[1]=3;
		
		long time = System.currentTimeMillis();
		solve(test1(share,diff));
		System.out.println("Total time spent:"+ (System.currentTimeMillis()-time)+" ms.");
		//solve(test0(2,3));
		
	}

	public static FOFormula test1(int share[],int diff[]){
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

	public static FOFormula test0(int...diff){
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

	public static void solve(FOFormula formulas){
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("test.smt2")),formulas);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				invoker.showVars();	
				formulas.addExpression(FOFormula.NegVars());
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
			System.out.println("Solving is [successful]");
		}
		catch (IOException e){e.printStackTrace();System.err.println("Solving is [failed]");}
		catch (UnknownException e){e.printErrMessage(" [failed] formula cannot be decided.");}
		catch (UnsatException e){e.printErrMessage(" [failed] formula is not satisfiable.");}
	}

	

}

