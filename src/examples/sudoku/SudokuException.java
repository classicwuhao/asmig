/* A Sudoku Solver
 * 
 * 09-NOV-2012	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */
package examples.sudoku;
import java.lang.RuntimeException;

public final class SudokuException extends RuntimeException{
	private String message;
	
	public SudokuException(String m){
		message = m;
	}

	public String getMessage(){
		return message;
	}
	
}
