/**
 *	Coloring a graph
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 *
 */

package examples.graphcoloring;
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

public class GraphColoring{

	private static String testfile="../../test/GraphColoring.ecore";
	private static String foclfile="./gc.ocl";

	public static void TranslateOCL(String oclfile){
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		translator.translate();
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		o2f ocltranslator = new o2f(oclfile,translator.getFactory(),foftranslator);
		ocltranslator.visit();		
	}

	public static void Solve(String oclfile){
		FOFormula fof;
		ColorInterpreter interpreter;
		long time = System.currentTimeMillis();
		
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		System.out.println("loading..."+testfile);
		translator.translate();
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		System.out.println(translator.getFactory().toString(true));
		//System.out.println(translator.itree2String());
		long time1 = System.currentTimeMillis();
		o2f ocltranslator = new o2f(oclfile,translator.getFactory(),foftranslator);
		ocltranslator.visit();

		/* merge two fofs */
		fof = foftranslator.getFormula();
		fof.addExpression(ocltranslator.getFormula());
		System.out.println("OCL Translation Time:"+(System.currentTimeMillis()-time1));
		System.out.println("Total Translation Time:"+(System.currentTimeMillis()-time));
		
		/* solve model with OCL constraints */
		int solution=0;
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("graphcoloring")),
			fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			while (solution<=499){
				Result result = invoker.incSolve();
				if (result!=Result.SAT) break;
				interpreter = new ColorInterpreter("./dot/instance"+solution+".dot",foftranslator,"instance"+solution);
				interpreter.interpret();
				solution++;
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
			}
			invoker.releaseFiles();
			System.out.println(solution+" solution(s) enumerated." );		
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

		time = System.currentTimeMillis()-time;
		System.out.println("Toal time spend: "+time+" ms.");

	}

	public static void main (String args[]){
		TranslateOCL(foclfile);
	}
}

