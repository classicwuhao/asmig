/* A N-Queen Solver
 * 
 * 11-FEB-2013	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */

package examples.queen;
import java.util.Scanner;
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

public final class Queen{

	private static Board Nqueen = new Board(8,8);

	public static void main (String args[]){

		Rules rules = new Rules(Nqueen);
		FindSolution(rules.EncodeRules());
	}

	public static void FindSolution(FOFormula formulas){
		int solution=0;
		int s = 1;
		Scanner scan = new Scanner(System.in);
		System.out.println("Ready to solve this N-Queen problem...?");
		s = scan.nextInt();
		if (s==0) return;
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("sudoku")),
			formulas);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			Result result = invoker.incSolve();
			if (result==Result.SAT)
				System.out.println(Nqueen);
			else
				System.out.println("No Solution.");
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
