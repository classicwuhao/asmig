/* A Petersen Solver
 * 
 * 04-FEB-2013	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */

package examples.petersen;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import atongmu.smt.SMTSolver;
import atongmu.smt.Solver;
import atongmu.smt.SmtInvoker;
import atongmu.smt.Result;
import atongmu.util.SMT2Writer;
import atongmu.err.UnknownException;
import atongmu.err.UnsatException;
import atongmu.ast.FOFormula;

public final class PetersenSolver{
	private static Petersen pgraph;
	public static void main (String args[]){
		pgraph = new Petersen();
		Rule rule = new Rule(pgraph);
		Solve(rule.getFormula());
	}

	public static void Solve(FOFormula formulas){
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("petersen")),
			formulas);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			Result result = invoker.incSolve();
			PetersenInterpreter interpreter = new PetersenInterpreter(pgraph,"petersen_graph");
			invoker.releaseFiles();
		}
		catch (IOException e){ 
			//
		}
		catch (UnknownException e){
			e.printErrMessage(" formula cannot be decided.");
		}
		catch (UnsatException e){
			e.printErrMessage(" formula is not satisfiable.");
		}
	}

}

