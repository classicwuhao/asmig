/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  JAN-2013 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

/**
 *	This file contains a collection of testing procedures
 *  for testing OCL Translation to Boolean logic
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 *
 */

package test;
import java.io.*;
import java.util.*;
import atongmu.ast.*;
import atongmu.atg.*;
import atongmu.type.*;
import atongmu.util.*;
import atongmu.value.*;
import atongmu.err.*;
import atongmu.translator.*;
import atongmu.interpreter.*;
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;
import parser.ocl.*;
import org.antlr.runtime.*;

public final class teststr{

	public static void main (String args[]){
		FOFormula fof = new FOFormula();
		StrVar string1 = new StrVar(5,"string1");
		StrVar string2 = new StrVar(3,"string2");
		//BinaryExpression b1 = new BinaryExpression(Connective.EQUAL,var1,new NumLiteral(-1));
		int count=0;
		fof.addExpression(string1.formula());
		fof.addExpression(string2.formula());
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("string.smt2")),
			fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			while (count<10){
				invoker.incSolve();
				fof.addExpression(FOFormula.NegVars());
				count++;
			}
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
