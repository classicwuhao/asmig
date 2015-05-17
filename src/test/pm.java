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

public class pm {


	public static void main (String args[]){
		System.out.println("#########Testing Function Replacement#########");	
		Var v1 = new Var("x1",new BoolType());
		Var v2 = new Var("x2",new BoolType());
		Var v3 = new Var("x3",new BoolType());
		
		ConstDecl c1 = new ConstDecl(v1);
		ConstDecl c2 = new ConstDecl(v2);
		ConstDecl c3 = new ConstDecl(v3);
		
		FOFormula fof= new FOFormula();

		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			BinaryExpression b1 = FOFormula.join(Connective.AND,v1,v2,v3);
			fof.addExpression(b1);
			
			//BinaryExpression b2 = new BinaryExpression(Connective.EQUAL,v2,new BoolLiteral(true));
			//fof.addExpression(b2);

			v2.setValue(new BoolValue(false));	//we know x2 has to be included in each solution.
			v2.setFix(true);

			writer.Traverse();
			writer.close(false);
			//invoker.setSolverPath("");
			//Z3
			//SMTINTERPOL
			/*SMT2Writer copywriter = new SMT2Writer(new PrintWriter(
			new FileWriter("tmp.smt2")),writer.getFormula().clone());*/

			int solution=0;
			while(invoker.incSolve()==Result.SAT){
				invoker.showVars();
				NegFun neg = FOFormula.NegVars();
				//System.out.println(neg);
				//System.out.println(Vars.print2String());
				fof.addExpression(neg);
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
