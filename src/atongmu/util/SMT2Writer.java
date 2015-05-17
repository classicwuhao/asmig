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
package atongmu.util;

import java.util.*;
import java.io.*;
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
import atongmu.ast.Vars;
import atongmu.ast.ArithmeticExpression;
import atongmu.ast.Expression;
import atongmu.ast.FOFormula;
import atongmu.ast.StrVar;
import atongmu.ast.visitor.AbstractVisitor;
import atongmu.value.Value;
import atongmu.value.BoolValue;
import atongmu.value.IntValue;

public final class SMT2Writer extends AbstractVisitor{
	private PrintWriter out;
	private FOFormula fof;	
	//private String file;
	//default constructor
	public SMT2Writer(){
		out=null;
		fof=null;	
	}
	
	public SMT2Writer(PrintWriter w, FOFormula f)
	{
		out = w;
		fof=f;
		if (!emptyFormula())
			System.err.println("Warning...did you just feed me with everything abstract?");
	}

	public void visit (Argument arg){
		//do nothing
	}

	public void visit (BinaryExpression b){
		out.print("(");
		out.print(b.getConnectiveString()+" ");
		b.getLeft().accept(this);
		out.print(" ");
		b.getRight().accept(this);
		out.print(")");
	}

	public void visit(ArithmeticExpression e){
		out.print("(");
		out.print(e.getOperator2String()+" ");
		e.getOperand1().accept(this);
		out.print(" ");
		e.getOperand2().accept(this);
		out.print(")");			
	}

	public void visit (Constant con){
		//do nothing			
	}

	public void visit (Declaration dec){
		//do nothing
	}

	public void visit (FunExpression fun){

	}

	public void visit (NegFun nfun){
		out.print("(");
		out.print(nfun.getConnectiveString()+" ");
		nfun.getExpression().accept(this);
		out.print(")");
	}

	public void visit (Var var){
		Value val = var.getValue();
		if (var.isFixed()){
			if (val.IsBool(val))
				new BoolLiteral((BoolValue)val).accept(this);
			if (val.IsInt(val))
				new NumLiteral((IntValue)val).accept(this);
		}
		else{
			out.print(var.getName());
		}
	}

	public void visit(StrVar v){}
	
	public void visit (NumLiteral num){
		int value = num.getLiteral();
		if (value>=0)
			out.print(num.getLiteral());
		else{
			out.print("(- ");
			out.print(~value+1);
			out.print(")");
		}
	}

	public void visit(BoolLiteral b){
		out.print(b.toString());
	}

	public void Traverse(){
		Traverse(false);
	}

	public void Traverse(boolean fun){
		if (fof==null){
			//throw an exception
			System.err.println("No formula found.");
			return;
		}

		if (out==null){
			//throw an exception
			System.err.println("file is not specified.");
			return;
		}

		//System.out.println("Traverse...");
		//take care of declarations for variables;
		//using declare-const statement

		for (Declaration d : Vars.getVars().keySet()){
				Var v = Vars.getVars().get(d);
				if (!v.isFixed()){
					if (!fun){
						out.print("(declare-const ");
						out.print(d.getName()+" "+d.getType().toString());
						out.println(")");
					}
					else{
						out.print("(declare-fun ");
						out.print(d.getName()+" () "+d.getType().toFun());
						out.println(")");
					}
				}
		}
				
		//take care of declarations for user-specified functions;
		
		//start to visit
		List<Expression> exprs = fof.getExpressions();

		for (int i=0;i<exprs.size();i++){
			Expression expr= exprs.get(i);
			//add assertion;
			out.print("(assert ");
			expr.accept(this);
			out.println(")");
		}
		//System.out.println("Finish.");
	}

	private void WriteString(String anything){
		out.println(anything);
	}

	public void Traverse(Expression expr){
		out.print("(assert ");
		expr.accept(this);
		out.println(")");
	}

	public void Traverse(Expression...exprs){
		for (int i=0;i<exprs.length;i++){
			out.print("(assert ");
			exprs[i].accept(this);
			out.println(")");
		}
	}

	private void WriteCache(){
		Traverse();
		fof.reset();
	}

	public void WriteFormula(){
		List<Expression> exprs = fof.getExpressions();
		for (int i=0;i<exprs.size();i++){
			Expression expr= exprs.get(i);
			//add assertion;
			out.print("(assert ");
			expr.accept(this);
			out.println(")");
		}
	}
	
	public void Check(){
		out.println("(check-sat)");
		out.print("(get-value ( ");
		for (Declaration d : Vars.getVars().keySet()){
			Var v = Vars.getVars().get(d);
			if (!v.isFixed()){
				out.print(d.getName()+" ");
			}
		}
		out.println("))");
	}
	
	public void close(boolean reset){
		out.flush();
		out.close();
		if (reset) fof.reset();
	}

	public void setProduceModel(boolean b){
		out.println("(set-option :produce-models "+b+")");
	}

	public void setPrintSuccess(boolean b){
		out.println("(set-option :print-success "+b+")");
	}

	public void setLogic(String logic){
		out.println("(set-logic "+logic+")");
	}

	/* do not use it anymore */
	private void resetFormula(){
		fof.reset();
	}

	public FOFormula getFormula(){
		return fof;
	}

	/* we can return a copy of all formula */
	public FOFormula getFormula(boolean copy){
		return (copy) ? fof.clone() : fof;
	}

	private boolean emptyFormula(){
		return fof.size() > 0;
	}

}
