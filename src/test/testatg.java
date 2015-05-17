/**
 *	This file contains a collection of testing procedures
 *  for testing my engine: ATONGMU::ModelTranslator
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

public class testatg{
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

		int share0[] = new int[3];
		int share1[] = new int[2];
		int share2[]= new int[2];
		int share3[]= new int[2];
		int share4[]= new int[2];
		int diff0[] = new int[2];
		
		share0[0]=5;share0[1]=2;share0[2]=3;
		//share1[0]=3;share1[1]=5;
		//share2[0]=1;share2[1]=3;
		//share3[0]=2;share3[1]=4;
		//share4[0]=3;share4[1]=4;

		diff0[0]=4;diff0[1]=1;
		
		FOFormula fof;
		//Bound bound = new Bound(testfile);
		//Factory f = new Factory(bound);
		long time = System.currentTimeMillis();
		System.out.println("Translating...");
		e2a translator = new e2a(testfile);	// create factory and bound within a single call.
		translator.translate();
		System.out.println(translator.getFactory().toString(true)); // print details about everything, for debugging purpose.
		//System.out.println(translator.getFactory().toString(false));
		a2f foftranslator = new a2f(translator);
		foftranslator.TranslateToFOF();
		//Var[][] vars = foftranslator.getReference("access");
		/*Acyclic acyclic = new Acyclic(foftranslator,
			foftranslator.getATGTranslator().getBound().getReferenceByName("parents"));
		acyclic.gen0();*/
		//System.out.println("generating chains...");
		Chain chain = new Chain(foftranslator);
		chain.genChain(2,"extends");
	
		System.out.println("Generating LCOM3...");
		/*Metrics metrics = new Metrics(foftranslator);
		metrics.DisjointShare("access",share0);
		//metrics.Share("access",share1);
		//metrics.Share("access",share2);
		//metrics.Share("access",share3);
		//metrics.Share("access",share4);
		metrics.NoShare("access",diff0);
		foftranslator.Conjoin(metrics.getFormulas());*/
		
		System.out.println("Translation time spend: "+(System.currentTimeMillis()-time)+" ms.");
		System.out.println("Formula successfully generated, ready to solve...?");
		DotInterpreter interpreter;
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
				interpreter = new DotInterpreter("./dot/instance"+solution+".dot",foftranslator,"instance"+solution);
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

	public static void testGraphNode(){
		System.out.println("#########Testing Graph Node(2)#########");
		FOFormula fof= new FOFormula();
		try{
			Var b0 = new Var("b0", new BoolType());
			Var d0 = new Var("d0",new IntType());
			Var d4 = new Var("d4",new IntType());
			
			ConstDecl c1 = new ConstDecl(b0);
			ConstDecl c2 = new ConstDecl(d0);
			ConstDecl c3 = new ConstDecl(d4);
			
			SMT2Writer writer = new SMT2Writer(new PrintWriter (new FileWriter("tmp.smt2")),fof);
			SmtInvoker invoker = new SmtInvoker(Solver.Z3, writer);
			
			int solution=0;
			int s = 1;
			Scanner scan = new Scanner(System.in);
			
			while(s!=0){
				if (invoker.incSolve()!=Result.SAT)
					break;
				invoker.showVars();
				s=scan.nextInt();
				NegFun neg = FOFormula.NegVars();
				fof.addExpression(neg);
				solution++;
			}
			//invoker.releaseFiles();
			System.out.println("Number of the solutions found:"+solution);
		}
		/*catch (EmptyFormulaException e){
			e.setFormula(fof);
			e.printErrMessage(fof.CheckEmpty2String());
		}*/
		catch (IOException e){
			e.printStackTrace();
		}
		catch (UnknownException e){
			e.printErrMessage(" formula cannot be decided.");
		}
		catch (UnsatException e){
			e.printErrMessage(" formula is not satisfiable.");
		}		

	}
	


}
