/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  APR-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package atongmu.ast;

import java.io.PrintWriter;
import java.util.*;
import atongmu.err.MissingFormulaException;
import atongmu.err.EmptyFormulaException;
import atongmu.value.*;

/* 
 * Formula creation	
 * First-order formula generation assemblies your complicated formulas.
 * @TODO: To build a more compact form of AST.
 */
 
public final class FOFormula{
	private List<Expression> exprs;

	public FOFormula(){
		exprs = new ArrayList<Expression>();
	}

	public FOFormula(Expression...e){
		exprs=new ArrayList<Expression>();
		for (int i=0;i<e.length;i++)
			exprs.add(e[i]);
	}

	/*public FOFormula(Expression es[]){
		exprs = new ArrayList<Expression>();
		for (int i=0;i<es.length;i++)		
			exprs.add(es[i]);
	}*/
	
	public void addFormula(Expression...exprs){
		addExpression(exprs);
	}

	public void addFormula(Expression expr){
		addExpression(expr);
	}

	public void addFormula(List<Expression> list){
		addExpression(list);
	}

	public void addExpression(Expression...exprs){
		if (exprs==null) throw new FormulaException("Error: the list of formulas is null.");
		for (int i=0;i<exprs.length;i++){
			if (exprs[i]==null) throw new FormulaException("Error: cannot add a null formula.");
			addExpression(exprs[i]);
		}
		
	}

	public void addExpression(Expression expr) { //throws EmptyFormulaException
		if (exprs!=null)
			exprs.add(expr);
		//else
		//	throw new EmptyFormulaException(new PrintWriter(System.err,true));
	}

	public void addExpression(List<Expression> list){
		if (list!=null)
			for (int i=0;i<list.size();i++)
				exprs.add(list.get(i));
	}

	public void addExpression(FOFormula fof){
		if (fof==null)
			throw new FormulaException("can not add null formula.");

		addExpression(fof.getExpressions());
	}

	public boolean delExpression(Expression expr){
		return exprs.remove(expr);
	}

	public Expression[] toExpressions(){
		Expression e[] = new Expression[exprs.size()];
		for (int i=0;i<exprs.size();i++)
			e[i]=exprs.get(i);
		
		return e;
	}

	public FOFormula clone(){
		return new FOFormula(toExpressions());
	}
	
	public List<Expression> getExpressions(){
		return exprs;
	}

	public void reset(){
		exprs.clear();
	}
	

	/* works on a list of non-empty expressions */
	public static BinaryExpression join(Connective conn, List<Expression> list){
		Expression exprs[] = new Expression[list.size()];
		for (int i=0;i<list.size();i++)
			exprs[i] = list.get(i);
		
		return FOFormula.join(conn,exprs);
		
	}

	public static BinaryExpression _join(Connective conn, List<BinaryExpression> list){
		BinaryExpression exprs[] = new BinaryExpression[list.size()];
		for (int i=0;i<list.size();i++)
			exprs[i] = list.get(i);
		
		return FOFormula.join(conn,exprs);		
	}

	public static BinaryExpression join(Connective conn, Expression...expr) {  //throws MissingFormulaException
		if (expr.length < 2)
			throw new FormulaException("Error: need more expressions...");
		

		BinaryExpression b = new BinaryExpression(conn, expr[0],expr[1]);

		for (int i=2;i<expr.length;i++)
			b = new BinaryExpression(conn,b,expr[i]);
		return b;
	}

	/*public static BinaryExpression pushNOT(BinaryExpression expr){
	}*/

	public static ArithmeticExpression join(Arithmetic operator, List<Var> list){
		Var vars[] = new Var[list.size()];
		for (int i=0;i<list.size();i++)		
			vars[i] = list.get(i);
		
		return FOFormula.join(operator, vars);
	}

	public static ArithmeticExpression join(Arithmetic operator, Var...vars){
		if (vars.length<2)
			throw new FormulaException("Error: insufficient expressions...");
		
		ArithmeticExpression a = new ArithmeticExpression(operator,vars[0],vars[1]);
		for (int i=2;i<vars.length;i++)
			a = new ArithmeticExpression(operator,a,vars[i]);
		
		return a;
	}
	
	//perform a full check on each formula
	public void CheckFormula() throws EmptyFormulaException{
		for (int i=exprs.size()-1;i>=0;i--)
			if (exprs.get(i)==null)			
				throw new EmptyFormulaException(new PrintWriter(System.err,true));
	}

	public static NegFun NegVars(){
		NegFun nullneg =null;
		Constant constant=null;
		Var v=null;
		int size=Vars.decVarSize();
		
		if (size<=0) return nullneg;
		
		if (size<2){
			for (int key : Vars.getContext().keySet())
				v=Vars.getVar(key);
			
			if (v.isIntType() && v.getValue()!=null)
				constant=new NumLiteral((IntValue)v.getValue());
			
			else if (v.isBoolType() && v.getValue()!=null)
				constant=new BoolLiteral((BoolValue)v.getValue());

			if (!v.isFixed())
				return new NegFun(new BinaryExpression(Connective.EQUAL,v,constant));
			else
				return nullneg;
		}
		
		/* make a binary expression for each variable in the context */	
		BinaryExpression b[]=new BinaryExpression[size];
		int i=0;
		BinaryExpression r=null;
		
		for (int key : Vars.getContext().keySet()){
			v=Vars.getVar(key);
			if (v==null)	
				throw new FormulaException("unexpected null value occurs for a variable.");
			
			if (v.isIntType() && v.getValue()!=null)
				constant=new NumLiteral((IntValue)v.getValue());
			else if (v.isBoolType() && v.getValue()!=null)
				constant=new BoolLiteral((BoolValue)v.getValue());

			if (constant!=null && !v.isFixed())
				b[i++]=new BinaryExpression(Connective.EQUAL,v,constant);
		}

		//try{
			 r = FOFormula.join(Connective.AND,b);

		//}
		//catch(MissingFormulaException e){
		//	e.printErrMessage("Negation on variables errors.");					
		//}

		return (r==null) ? nullneg : new NegFun(r);
	}

	/* works on a list of variables */
	public static BinaryExpression OneOnly(List<Var> list){
		if (list==null)
			throw new FormulaException("Variables must not be empty.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of variables.");

		Var[] vars = new Var[list.size()];
		for (int i=0;i<list.size();i++)
			vars[i] = list.get(i);
		
		return FOFormula.OneOnly(vars);
	}

	/* select only one variable among many others */
	public static BinaryExpression OneOnly(Var...vars){
		for (int i=0;i<vars.length;i++)	
			if (!vars[i].isBoolType())
				throw new FormulaException("variable's type "+ vars[i].getName() +" must be a boolean.");

		if (vars.length==1)
			return new BinaryExpression(Connective.EQUAL,vars[0],new BoolLiteral(true));

		if (vars.length==2)
			return new BinaryExpression(Connective.XOR,vars[0],vars[1]);

		NegFun negs[];
		BinaryExpression[] bs = new BinaryExpression[vars.length];
		
		for (int i=0;i<vars.length;i++){
			negs = new NegFun[vars.length-1];
			for (int j=0,k=0;j<vars.length;j++){
				if (j!=i)
					negs[k++] = new NegFun(vars[j]);
			}
			BinaryExpression b1 = FOFormula.join(Connective.AND,negs);
			bs[i]=new BinaryExpression(Connective.AND,vars[i],b1);
		}

		/* using OR operator to join each subformula */
		return FOFormula.join(Connective.OR,bs);
	}

	public static BinaryExpression OneOnlyExprs(List<Expression> list){
		if (list==null)
			throw new FormulaException("Cannot apply this operation on a null object.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of expressions.");	

		Expression expressions[] = new Expression[list.size()];

		for (int i=0;i<list.size();i++)
			expressions[i] = list.get(i);

		return FOFormula.OneOnlyExprs(expressions);
	}


	public static BinaryExpression OneOnlyExprs(Expression...exprs){

		if (exprs.length==1)
			return new BinaryExpression(Connective.EQUAL,exprs[0],new BoolLiteral(true));

		if (exprs.length==2)
			return new BinaryExpression(Connective.XOR,exprs[0],exprs[1]);
		
		NegFun negs[];
		BinaryExpression[] bs = new BinaryExpression[exprs.length];		

		for (int i=0;i<exprs.length;i++){
			negs = new NegFun[exprs.length-1];
			for (int j=0,k=0;j<exprs.length;j++){
				if (j!=i)
					negs[k++] = new NegFun(exprs[j]);
			}
			BinaryExpression b1 = FOFormula.join(Connective.AND,negs);
			bs[i]=new BinaryExpression(Connective.AND,exprs[i],b1);
		}

		return FOFormula.join(Connective.OR,bs);
	}

	
	public static Expression DifferentColor(List<Var> vars){
		if (vars==null) throw new FormulaException("Error: I cannot apply this operation on a null object.");

		Var[] _vars = new Var[vars.size()];
		for (int i=0;i<vars.size();i++)
			_vars[i]=vars.get(i);

		return FOFormula.DifferentColor(_vars);
	}

	public static Expression DifferentColor(Var...vars){
		if (vars==null) throw new FormulaException("Error: I cannot apply this operation on a null object.");
		if (vars.length==1) return vars[0];
		if (vars.length==2) return new NegFun(new BinaryExpression(Connective.EQUAL,vars[0],vars[1]));

		List<Expression> formulas = new ArrayList<Expression>();
		
		for (int i=0;i<vars.length-1;i++)
			formulas.add(new NegFun(new BinaryExpression(Connective.EQUAL,vars[i],vars[i+1])));
			
		return FOFormula.join(Connective.AND,formulas);
	}

	public static Expression Same(List<Var> vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");
		Var[] _vars = new Var[vars.size()];
		for (int i=0;i<vars.size();i++)
			_vars[i] = vars.get(i);

		return FOFormula.Same(_vars);
	}

	public static Expression Same(Var...vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");
		//To be disabled for only one variable, this can change SAT of a formula.
		if (vars.length==1) return vars[0]; 
		if (vars.length==2) return new BinaryExpression(Connective.EQUAL,vars[0],vars[1]);

		List<Expression> formulas = new ArrayList<Expression>();

		for (int i=0;i<vars.length;i++)
			for (int j=i+1;j<vars.length;j++)
				formulas.add(new BinaryExpression(Connective.EQUAL,vars[i],vars[j]));

		return FOFormula.join(Connective.AND,formulas);
	}

	public static Expression Unique(List<Var> vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");
		Var _vars[] = new Var[vars.size()];
		for (int i=0;i<vars.size();i++)
			_vars[i]=vars.get(i);

		return FOFormula.Unique(_vars);
	}

	public static Expression Unique(Var...vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");

		List<Expression> formulas = new ArrayList<Expression>();
		if (vars.length==1)
			return vars[0];
		if (vars.length==2)
			return new NegFun(new BinaryExpression(Connective.EQUAL,vars[0],vars[1]));

		for (int i=0;i<vars.length;i++)
			for (int j=i+1;j<vars.length;j++)
				formulas.add(new NegFun(new BinaryExpression(Connective.EQUAL,vars[i],vars[j])));
			
		
		return FOFormula.join(Connective.AND,formulas);
		
	}

	/*public static Expression Same(Var...vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");
		
		List<Expression> formulas = new ArrayList<Expression>();

		if (vars.length==1)
			return vars[0];

		if (vars.length==2)
			return new BinaryExpression(Connective.EQUAL,vars[0],vars[1]);

		for (int i=0;i<vars.length-1;i++)
			formulas.add(new BinaryExpression(Connective.EQUAL,vars[i],vars[i+1]));

		return FOFormula.join(Connective.AND,formulas);
	}*/

	public static Expression Range(int from, int to, List<Var> vars){
		if (vars==null) if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");

		Var[] v= new Var[vars.size()];
		for (int i=0;i<vars.size();i++)
			v[i]=vars.get(i);

		return Range(from, to, v);
	}

	public static Expression Range(int from,int to, Var...vars){
		if (vars==null) throw new FormulaException("Cannot apply this operation on a null object.");
		if (vars.length==1) 
			return new BinaryExpression(Connective.AND,
						new ArithmeticExpression(Arithmetic.GREATER_EQUAL,vars[0],new NumLiteral(from)),
						new ArithmeticExpression(Arithmetic.LESS_EQUAL,vars[0],new NumLiteral(to)));

		List<Expression> formulas = new ArrayList<Expression>();
		for (int i=0;i<vars.length;i++)
			formulas.add(
						new BinaryExpression(Connective.AND,
						new ArithmeticExpression(Arithmetic.GREATER_EQUAL,vars[i],new NumLiteral(from)),
						new ArithmeticExpression(Arithmetic.LESS_EQUAL,vars[i],new NumLiteral(to)))
						);

		return FOFormula.join(Connective.AND,formulas);
	}

	public static Expression NegVars(Var...vars){
		if (vars==null) throw new FormulaException("Error: variables are null.");
		Constant constant = null;
		List<Expression> formulas = new ArrayList<Expression>();
		if (vars.length==1){
			if (vars[0].isIntType() && vars[0].getValue()!=null)
				constant = new NumLiteral((IntValue)vars[0].getValue());
			else if (vars[0].isBoolType() && vars[0].getValue()!=null)
				constant = new BoolLiteral((BoolValue)vars[0].getValue());

			return new NegFun(new BinaryExpression(Connective.EQUAL,vars[0],constant));
		}
			
		for (int i=0;i<vars.length;i++){
			Var v = vars[i];
				
			if (v.isIntType() && v.getValue()!=null)
				constant = new NumLiteral((IntValue)v.getValue());
			else if (v.isBoolType() && v.getValue()!=null)
				constant = new BoolLiteral((BoolValue)v.getValue());
			
			formulas.add(new BinaryExpression(Connective.EQUAL,v,constant));	
		}

		return new NegFun(FOFormula.join(Connective.AND,formulas));

	}

	public static Expression SetConstant(Var var, int value){
		if (var.isIntType())
			return new ArithmeticExpression(Arithmetic.EQUAL,var,new NumLiteral(value));
		else if (var.isBoolType())
			return new BinaryExpression(Connective.EQUAL,var,new BoolLiteral(value>0));
		else
			throw new FormulaException("Error: unsupported constant type.");
	}

	/* works on a list of vairables: NONE */
	public static NegFun None(List<Var> list){
		if (list==null)
			throw new FormulaException("Cannot apply this operation on a null object.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of variables.");

		Var vars[] = new Var[list.size()];
		for (int i=0;i<list.size();i++)		
			vars[i] = list.get(i);

		return FOFormula.None(vars);
	}

	/* works on a list of vairables: Some */
	public static BinaryExpression Some(List<Var> list){
		if (list==null)
			throw new FormulaException("Cannot apply this operation on a null object.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of variables.");

		Var vars[] = new Var[list.size()];

		for (int i=0;i<list.size();i++)
			vars[i] = list.get(i);			
	
		return FOFormula.Some(vars);
	}

	/* select nothing among many variables */
	public static NegFun None(Var...vars){
		//BinaryExpression b1 = FOFormula.join(Connective.OR,vars);
		return new NegFun(Some(vars));
	}


	/* select something among many variables*/
	public static BinaryExpression Some(Var...vars){
		if (vars==null)	throw new FormulaException("Variables cannot be empty.");
		/* explicitly make this variable true */
		if (vars.length==1) return new BinaryExpression(Connective.EQUAL,vars[0],new BoolLiteral(true));

		return FOFormula.join(Connective.OR,vars);
	}

	/* select some expressions from a list of expressions */
	public static BinaryExpression SomeExprs(List<Expression> list){
		if (list==null)
			throw new FormulaException("Cannot apply this operation on a null object.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of expressions.");

		Expression expressions[] = new Expression[list.size()];

		for (int i=0;i<list.size();i++)
			expressions[i] = list.get(i);			
	
		return FOFormula.SomeExprs(expressions);
	}	

	
	public static BinaryExpression SomeExprs(Expression...expressions){
		if (expressions==null)	throw new FormulaException("Expressions cannot be null.");
		if (expressions.length==0) throw new FormulaException("Expressions cannot be empty.");
		if (expressions.length==1) return new BinaryExpression(Connective.EQUAL,expressions[0],new BoolLiteral(true));
		return FOFormula.join(Connective.OR,expressions);

	}

	public static BinaryExpression AllExprs(List<Expression> list){
		if (list==null)
			throw new FormulaException("Cannot apply this operation on a null object.");

		if (list.size()==0)
			throw new FormulaException("Cannot apply this operation on an empty list of expressions.");

		Expression expressions[] = new Expression[list.size()];
		for (int i=0;i<list.size();i++)
			expressions[i]=list.get(i);
		
		return FOFormula.AllExprs(expressions);
	}

	public static BinaryExpression AllExprs(Expression...expressions){
		if (expressions==null) throw new FormulaException("Expressions cannot be null.");
		if (expressions.length==0) throw new FormulaException("Expressions cannot be empty.");
		if (expressions.length==1) return new BinaryExpression(Connective.EQUAL,expressions[0],new BoolLiteral(true));
		return FOFormula.join(Connective.AND,expressions);
	}

	public static NegFun[] Negation(List<Expression> list){
		if (list==null) throw new FormulaException("Expressions cannot be null.");
		if (list.size()==0) throw new FormulaException("Expressions cannot be empty.");		
		
		Expression expressions[] = new Expression[list.size()];

		for (int i=0;i<list.size();i++)
			expressions[i] = list.get(i);

		return Negation(expressions);
	}

	public static NegFun[] Negation(Expression...expressions){
		if (expressions==null) throw new FormulaException("Expressions cannot be null.");
		if (expressions.length==0) throw new FormulaException("Expressions cannot be empty.");
		NegFun negs[] = new NegFun[expressions.length];

		for (int i=0;i<expressions.length;i++)
			negs[i] = new NegFun(expressions[i]);

		return negs;
	}

	public static Expression MapVars(Var var0, Var var1){
		return new BinaryExpression(Connective.IMPLIES,var0,var1);
	}

	public boolean NegAllFormula(){
		for (int i=0;i<exprs.size();i++){
			if (exprs.get(i)==null) return false;
			NegFun nexpr = new NegFun(exprs.get(i));
			exprs.set(i,nexpr);
		}
		
		return true;
	}

	public void removeExpression(Expression expr){
		if (expr==null)	throw new FormulaException("Formula is about to remove is null.");
		if (!exprs.remove(expr))
			System.err.println("Warning: failed to remove formula <"+expr+">");
	}

	public String CheckEmpty2String(){
		if (exprs==null)
			return "formula is empty.";
		else 
			return toString();
	}

	public int size(){
		return exprs.size();
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<exprs.size();i++){
			sb.append("Formula(");
			sb.append(i+1);
			sb.append("):");
			sb.append(exprs.get(i).toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
