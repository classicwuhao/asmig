/* A Sudoku Solver
 * 
 * 09-NOV-2012	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */
package examples.sudoku;
import java.util.List;
import java.util.ArrayList;
import atongmu.ast.Var;
import atongmu.ast.FOFormula;
import atongmu.value.IntValue;

public class Rules{
	private Sudoku sudoku;
	private FOFormula formulas = new FOFormula();
			
	public Rules(Sudoku s){
		sudoku=s;
	}

	public void EncodeRules(){
		if (sudoku==null) throw new SudokuException("Error: A sudoku is null.");
		
		List<Var> vars = sudoku.getAllCells();
		Var[] cells = new Var[vars.size()];
		
		/* set up a range for each cell */
		for (int i=0;i<vars.size();i++) cells[i]=vars.get(i);
		formulas.addExpression(FOFormula.Range(1,9,cells));

		/* rules for each row */
		for (int i=1;i<=sudoku.getRow();i++)
			formulas.addExpression(FOFormula.Unique(sudoku.getGridRowCells(i)));
		
		/* rules for each column */
		for (int i=1;i<=sudoku.getColumn();i++)
			formulas.addExpression(FOFormula.Unique(sudoku.getGridColumnCells(i)));
	
		/* rules for each block */
		for (int i=1;i<=sudoku.getBlockLength();i++)
			formulas.addExpression(FOFormula.Unique(sudoku.getBlock(i)));

	}
	
	public void setValue(int i, int j, int value){
		Var v = sudoku.getCell(i,j);
		formulas.addExpression(FOFormula.SetConstant(v,value));
	}

	public FOFormula getFormula(){return formulas;}

}
