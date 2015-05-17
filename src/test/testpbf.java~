/**
 *	This file contains a collection of testing procedures
 *  for testing my engine: ATONGMU::PBF
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 *  
 *  Method testModel1() can be used as a driven program to load any metamodel in ecore format, 
 *  and enumerate as many as pssoible solutions based on 
 *  an awkward keyboard input.
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
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;
import atongmu.translator.*;
import atongmu.interpreter.*;

public class testpbf{
	private static String testfile="../../test/coverage_test1.ecore";
	private static String oclfile="";	

	public static void main(String[] args){
		System.out.println("Testing Program for Partition-Based Formula");
		if (args.length>=1){
			testfile=args[0];
			if (args.length==2) oclfile=args[1];
			test();
		}
		else{
			System.out.println("You did not sepcify a model, the default model is applied.");
			test();
		}
	}

	public static void test(){
		System.out.println("#########Test Case (1): Loading -> Translating -> Solving (partition-based formula) -> Interpreting #########");
		FOFormula fof;
		o2f ocltranslator;
		long time = System.currentTimeMillis();
		System.out.println("Translating...");
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		//translator.calculateBound();
		System.err.println("Bound calculation is done.");
		translator.translate();
		//System.out.println(translator.getFactory().toString(true)); // print details about everything, for debugging purpose.

		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();

		/* partition-based formula */
		PBF pbf = new PBF(foftranslator,Criteria.CA);
		pbf.genAttributeCriteria();
		pbf.genAssociationCriteria();		

		fof = foftranslator.getFormula();
		fof.addExpression(pbf.getFormula());
		
		if (oclfile!=""){
			ocltranslator = new o2f(oclfile,translator.getFactory(),foftranslator);
			ocltranslator.visit();
			fof.addExpression(ocltranslator.getFormula());
		}
		
		System.out.println("Translation time spend: "+(System.currentTimeMillis()-time)+" ms.");
		System.out.println("Formula successfully generated, ready to solve...?");

		DotInterpreter interpreter;
		int solution=0;
		int s = 1;
		Scanner scan = new Scanner(System.in);
		s = scan.nextInt();
		if (s==0) return;
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("testpbf.smt2")),
			fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			while (true){
				System.out.println("Solving...");
				Result result = invoker.incSolve();
				if (result!=Result.SAT) break;
				System.out.println("interpreting this solution...");
				interpreter = new DotInterpreter("./dot/instance"+solution+".dot",foftranslator,"instance"+solution);
				interpreter.interpret();
				//System.out.println("diagram description generated.");
				solution++;
				System.out.println(solution +" One solution found...Do you want to continue finding ?");
				//s = scan.nextInt();
				//NegFun neg = FOFormula.NegVars();
				fof.addExpression(FOFormula.NegVars(pbf.getPBV()));
			}
			//invoker.releaseFiles();
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


}
