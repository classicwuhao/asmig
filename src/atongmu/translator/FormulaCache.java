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

import java.util.HashMap;
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

public final class FormulaCache{
	private HashMap<Symbol,ArrayList<FormulaRecord>> cache = new HashMap<Symbol,ArrayList<FormulaRecord>>();
	
	public FormulaCache(){}
			
	public void cache(Symbol symbol, Expression formula){
		ArrayList<FormulaRecord> records;
		if (formula==null) return; /* if it is really null, we just skip caching process. */
		
		records = (!cache.containsKey(symbol)) ? new ArrayList<FormulaRecord>() : cache.get(symbol);
		records.add(new SingleFormulaRecord(formula));
		cache.put(symbol,records);
	}

	public void cache(Symbol symbol, Expression[][] formulas){
		if (formulas==null) throw new CacheException("Error: formula is null, please check your formula.");
		ArrayList<FormulaRecord> records;

		if (cache.containsKey(symbol)){
			System.err.println("Warning: formula is already cached.");
			return;
		}
		
		records = new ArrayList<FormulaRecord>();
		records.add(new MultiFormulaRecord(formulas));
		cache.put(symbol,records);

	}
	
	public ArrayList<FormulaRecord> lookup(Symbol symbol){
		return (cache.containsKey(symbol)) ? cache.get(symbol) : null;
	}

	public List<Symbol> getSymbols(){
		List<Symbol> symbols = new ArrayList<Symbol>();
		for (Symbol sym : cache.keySet()) symbols.add(sym);		
		return symbols;
	}
	
}
