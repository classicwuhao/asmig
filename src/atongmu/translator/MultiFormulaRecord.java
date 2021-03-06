/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  MAR-2013 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package atongmu.translator;

import java.util.List;
import java.util.ArrayList;

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
import atongmu.ast.StrVar;
import atongmu.ast.Expression;

public class MultiFormulaRecord extends FormulaRecord{
	private SingleFormulaRecord records[][];
	private int m,n;
	public MultiFormulaRecord(Expression formulas[][]){
		if (formulas==null)	throw new CacheException("Error: formulas must not be empty.");
		records = new SingleFormulaRecord[formulas.length][formulas[0].length];
		m = formulas.length;
		n = formulas[0].length;
		
		for (int i=0;i<formulas.length;i++) 
			for (int j=0;j<formulas[0].length;j++)
				records[i][j] = new SingleFormulaRecord(formulas[i][j]);		
	}
	
	public SingleFormulaRecord[][] getFormulas(){return records;}
	
	public Expression getRecord(){
		/* we should merge them into one gate. */
		return records[0][0].getRecord();
	}

	public int getM(){return m;} public int getN(){return n;}
	public boolean isMultiFormulaRecord() {return true;}
		
}
