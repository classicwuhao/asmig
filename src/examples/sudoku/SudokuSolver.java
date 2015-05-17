/* A Sudoku Solver
 * 
 * 09-NOV-2012	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */
package examples.sudoku;
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


public class SudokuSolver{

	public static void main (String args[]){
		Sudoku sudoku = new Sudoku();
		Rules rules = new Rules(sudoku);
		rules.EncodeRules();
		
		/*rules.setValue(1,1,2);rules.setValue(1,4,4);rules.setValue(1,5,5);
		rules.setValue(1,6,8);rules.setValue(1,7,3);rules.setValue(2,6,1);
		rules.setValue(2,7,4);rules.setValue(2,8,5);rules.setValue(3,1,5);
		rules.setValue(3,6,7);rules.setValue(3,7,6);rules.setValue(4,4,6);
		rules.setValue(4,8,3);rules.setValue(5,2,9);rules.setValue(5,8,8);
		rules.setValue(6,2,8);rules.setValue(6,6,2);rules.setValue(7,3,9);
		rules.setValue(7,4,8);rules.setValue(7,9,4);rules.setValue(8,2,6);
		rules.setValue(8,3,2);rules.setValue(8,4,5);rules.setValue(9,3,1);
		rules.setValue(9,4,3);rules.setValue(9,5,2);rules.setValue(9,6,4);
		rules.setValue(9,9,9);*/

		/*rules.setValue(1,7,6);rules.setValue(1,8,8);rules.setValue(2,5,7);
		rules.setValue(2,6,3);rules.setValue(2,9,9);rules.setValue(3,1,3);
		rules.setValue(3,3,9);rules.setValue(3,8,4);rules.setValue(3,9,5);
		rules.setValue(4,1,4);rules.setValue(4,2,9);rules.setValue(5,1,8);
		rules.setValue(5,3,3);rules.setValue(5,5,5);rules.setValue(5,7,9);
		rules.setValue(5,9,2);rules.setValue(6,8,3);rules.setValue(6,9,6);
		rules.setValue(7,1,9);rules.setValue(7,2,6);rules.setValue(7,7,3);
		rules.setValue(7,9,8);rules.setValue(8,1,7);rules.setValue(8,4,6);
		rules.setValue(8,5,8);rules.setValue(9,2,2);rules.setValue(9,3,8);
		FindSolution(rules.getFormula());
		System.out.println(sudoku);

		rules.EncodeRules();
		rules.setValue(1,5,5);rules.setValue(2,1,5);rules.setValue(2,6,1);
		rules.setValue(2,7,9);rules.setValue(3,1,9);rules.setValue(3,6,7);
		rules.setValue(3,8,3);rules.setValue(4,3,6);rules.setValue(4,6,5);
		rules.setValue(5,3,7);rules.setValue(5,5,9);rules.setValue(5,7,2);
		rules.setValue(6,4,4);rules.setValue(6,7,8);rules.setValue(7,2,3);
		rules.setValue(7,4,9);rules.setValue(7,9,1);rules.setValue(8,3,4);
		rules.setValue(8,4,8);rules.setValue(8,8,6);rules.setValue(9,5,2);*/


		rules.setValue(1,3,9);rules.setValue(1,4,7);rules.setValue(1,5,4);rules.setValue(1,6,8);
		rules.setValue(2,1,7);
		rules.setValue(3,2,2);rules.setValue(3,4,1);rules.setValue(3,6,9);
		rules.setValue(4,3,7);rules.setValue(4,7,2);rules.setValue(4,8,4);
		rules.setValue(5,2,6);rules.setValue(5,3,4);rules.setValue(5,5,1);rules.setValue(5,7,5);rules.setValue(5,8,9);
		rules.setValue(6,2,9);rules.setValue(6,3,8);rules.setValue(6,7,3);
		rules.setValue(7,4,8);rules.setValue(7,6,3);rules.setValue(7,8,2);
		rules.setValue(8,9,6);
		rules.setValue(9,4,2);rules.setValue(9,5,7);rules.setValue(9,6,5);rules.setValue(9,7,9);

		/* solve sudoku...*/
		FindSolution(rules.getFormula());
		/* print out the solution */
		System.out.println(sudoku);
		
	}
			
	public static void FindSolution(FOFormula formulas){
		int solution=0;
		int s = 1;
		Scanner scan = new Scanner(System.in);
		System.out.println("Ready to solve this Sudoku...");
		s = scan.nextInt();
		if (s==0) return;
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("sudoku")),
			formulas);
			writer.Traverse();
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			System.out.println("Generating...");
			Result result = invoker.incSolve();
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

