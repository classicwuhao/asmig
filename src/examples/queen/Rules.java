/* Rules for N-Queen
 * 
 * 11-FEB-2013	
 * Written by: Hao Wu
 * bugs report to: Haowu@cs.nuim.ie
 */

package examples.queen;
import java.util.List;
import java.util.ArrayList;
import atongmu.ast.Var;
import atongmu.ast.FOFormula;
import atongmu.ast.Connective;
import atongmu.ast.BinaryExpression;
import atongmu.ast.NegFun;
public final class Rules{
	private Board board;
	private FOFormula formulas = new FOFormula();

	public Rules(Board b){board=b ;}

	public FOFormula EncodeRules(){
		/* row, column and dia */
		for (int i=0;i<board.row();i++)
			formulas.addExpression(FOFormula.OneOnly(board.getRow(i)));

		for (int i=0;i<board.col();i++)
			formulas.addExpression(FOFormula.OneOnly(board.getCol(i)));

		for (int i=0;i<board.row();i++)
			for (int j=0;j<board.col();j++)
				formulas.addExpression(new BinaryExpression(Connective.IMPLIES,
				board.getCell(i,j),FOFormula.None(board.getDiagonal(i,j))));
			
		return formulas;
	}

}
