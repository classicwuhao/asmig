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

public class testparser{

	private static String testfile="../../test/ocl_simple_example_1.ecore"; 

	public static void testPrintVisitor(String oclfile){
		PrintAST printer= new PrintAST(oclfile);
		System.out.println("Print Visitor..."+oclfile);
		printer.visit();
	}

	public static void testReturnVisitor(String oclfile){
	
		System.out.println("Translating...");
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		translator.translate();
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		System.out.println("Visiting OCL AST...");
		o2f ocltranslator = new o2f(oclfile,translator.getFactory(),foftranslator);
		ocltranslator.visit();
		
	}

	public static void testSolve(String oclfile){
		FOFormula fof;
		DotInterpreter interpreter;
		long time = System.currentTimeMillis();
		
		System.out.println("****************Testing Solving Simple OCL Constraints from a Metamodel****************");
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		System.out.println("loading..."+testfile);
		translator.translate();
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		System.out.println(translator.getFactory().toString(true));
		//System.out.println(translator.itree2String());
		System.out.println("Translating OCL Constraints...");
		long time1 = System.currentTimeMillis();
		o2f ocltranslator = new o2f(oclfile,translator.getFactory(),foftranslator);
		ocltranslator.visit();
		System.out.println("************Translating completed with no errors************");
		/* merge two fofs */
		fof = foftranslator.getFormula();
		fof.addExpression(ocltranslator.getFormula());
		System.out.println("OCL Translation Time:"+(System.currentTimeMillis()-time1));
		System.out.println("Total Translation Time:"+(System.currentTimeMillis()-time));
		/* Specify a partial model */
		//PModel pm = new PModel(foftranslator,Criteria.INCLUSION);
		//pm.setFixedNode("b3");
		//pm.setFixedNode("a1");
		
		/* solve model with OCL constraints */
		int solution=0;
		int s = 1;
		Scanner scan = new Scanner(System.in);
		System.out.print("Ready to solve...Press a number greater than 0:");
		s = scan.nextInt();
		if (s==0) return;
		try{
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("test.smt2")),
			fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer); //use Z3 smt-solver
			while (s>0){
				System.out.println("Solving...");
				Result result = invoker.incSolve();
				if (result!=Result.SAT) break;
				System.out.println("interpreting this solution...");
				interpreter = new DotInterpreter("./dot/instance"+solution+".dot",foftranslator,"instance"+solution);
				interpreter.interpret();
				System.out.println("diagram description generated.");
				solution++;
				System.out.println(solution +" One solution found...Do you want to continue finding ?");
				s = scan.nextInt();
				/* the following 2 lines should be made implicit to users */
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
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

	public static void main (String args[]){

		if (args.length>=2){
			testfile = args[0];
			//testPrintVisitor(args[1]);
			//testReturnVisitor(args[0]);
			testSolve(args[1]);
		}
	
		/*OCLParser parser;
		OCLLexer lexer;

		System.out.println("*****Test Program for OCL Parser*****");
		PrintWriter err=new PrintWriter(System.out,true);
		try {
			if (args.length>=1){
				ParseErrorHandler errh = new ParseErrorHandler(args[0],err);
				lexer = new OCLLexer(new ANTLRFileStream(args[0]));
				CommonTokenStream token = new CommonTokenStream(lexer);
				parser = new OCLParser(token);

				lexer.init(errh);
				//parser.init(errh);
				ASTOCLExpression astexpt = parser.oclexpression();
				List<ASTConstraintDefinition> list = astexpt.getConstraints();
				System.out.println("Constraints found:"+list.size());
				for (int i=0;i<list.size();i++){
					List<ASTInvariantClause> clauses = list.get(i).getInvs();
					List<Token> tokens = list.get(i).getVars();

					for (int j=0;j<clauses.size();j++){
						ASTInvariantClause inv = clauses.get(j);
						System.out.println(inv.getName()+":"+inv.toString());
					}

					/*for (int k=0;k<tokens.size();k++){
						Token t = tokens.get(k);
						System.out.println("token:"+t.getText());
					}*/

				/*}
				//ASTType asttype = parser.typeOnly();
				//System.out.println("ErrorCount:"+errh.errorCount());
			//}
		//}
		//catch (RecognitionException e) {
         //  	 err.println(args[0] +":" + 
         	             e.line + ":" +
           	             e.charPositionInLine + ": " + 
           	             e.getMessage());
		}
		catch (IOException e){
			e.printStackTrace();
		}*/
	}
}

