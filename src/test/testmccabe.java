/**
 *	This file contains a collection of testing procedures
 *  for testing my engine: ATONGMU::ModelTranslator
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
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;
import atongmu.translator.*;
import atongmu.interpreter.*;

public class testmccabe{
	private static String testfile="../../test/student.ecore"; 

	public static void main (String args[]){
		System.out.println("Testing Program for ASMIG");
		if (args.length>=1){
			testfile=args[0];
			testModel1();
		}
		else{
			System.out.println("You did not sepcify a model, the default model is applied.");
			testModel1();
		}
		//testModel2();
	}

	public static void testModel1(){
		System.out.println("#########Test Case (1): Loading -> Translating -> Solving -> Interpreting #########");
		FOFormula fof;
		long time = System.currentTimeMillis();
		System.out.println("Translating...");
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		translator.translate();
		System.out.println(translator.getFactory().toString(true)); // print details about everything, for debugging purpose.
		//System.out.println(translator.getFactory().toString(false));
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		
		QuanNodesEdges cab = new QuanNodesEdges(foftranslator);
		List<GraphNode> nodes = translator.getFactory().getGraphNode("Class");

		/*for (int i=0;i<nodes.size();i++)
			System.out.println(nodes.get(i));*/

		//GraphNode n1 = nodes.get(0);
		//GraphNode n2 = nodes.get(3);
		
		//foftranslator.SwitchOff(n1,n2);
		//nodes.remove(n1);
		//nodes.remove(n2);
		
		foftranslator.getFormula().addExpression(cab.generate("methods",2,nodes));
		
		System.out.println("Translation time spend: "+(System.currentTimeMillis()-time)+" ms.");
		System.out.println("Formula successfully generated, ready to solve...?");
		GraphInterpreter interpreter;
		int solution=0;
		int s = 1;
		Scanner scan = new Scanner(System.in);
		s = scan.nextInt();
		if (s==0) return;
		try{
			fof = foftranslator.getFormula();
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("test.smt2")),
			fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			while (invoker.incSolve()==Result.SAT){
				System.out.println("Solving...");
				System.out.println("interpreting this solution...");
				interpreter = new GraphInterpreter("./dot/instance"+solution+".dot",foftranslator,"instance"+solution);
				interpreter.interpret();
				solution++;
				System.out.println(solution +" One solution found...Do you want to continue finding ?");
				//s = scan.nextInt();
				fof.addExpression(FOFormula.NegVars());
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
