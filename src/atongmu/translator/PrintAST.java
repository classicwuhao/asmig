package atongmu.translator;

import java.util.*;
import java.io.*;

import parser.ocl.OCLLexer;
import parser.ocl.OCLParser;
import parser.ocl.ASTExpression;
import parser.ocl.ASTAllInstancesExpression;
import parser.ocl.ASTBinaryExpression;
import parser.ocl.ASTBooleanLiteral;
import parser.ocl.ASTCollectionLiteral;
import parser.ocl.ASTEmptyCollectionLiteral;
import parser.ocl.ASTEnumLiteral;
import parser.ocl.ASTIfExpression;
import parser.ocl.ASTIntegerLiteral;
import parser.ocl.ASTLetExpression;
import parser.ocl.ASTOperationExpression;
import parser.ocl.ASTQueryExpression;
import parser.ocl.ASTRealLiteral;
import parser.ocl.ASTStringLiteral;
import parser.ocl.ASTTupleLiteral;
import parser.ocl.ASTTypeArgExpression;
import parser.ocl.ASTUnaryExpression;
import parser.ocl.ASTIterateExpression;
import parser.ocl.ASTConstraintDefinition;
import parser.ocl.ASTInvariantClause;
import parser.ocl.ASTUndefinedLiteral;
import parser.ocl.ASTOCLExpression;
import parser.ocl.Context;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.ParseErrorHandler;
import org.antlr.runtime.*;

import atongmu.ast.*;
import atongmu.atg.*;
import atongmu.type.*;
import atongmu.util.*;
import atongmu.value.*;
import atongmu.err.*;
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;

public final class PrintAST extends PrintVisitor{
	private String oclFile;
	private OCLParser parser;
	private OCLLexer lexer;
	private Context currentContext = new Context("");

	public PrintAST(){}

	public PrintAST(String file){
		oclFile = file;
	}

	public void visit(ASTAllInstancesExpression e){
		System.out.println("visiting allInstanceExpression...");
		System.out.println(e.token());
	}

	public void visit(ASTBinaryExpression e){
		System.out.println("visiting ASTBinaryExpression...");
		System.out.println("\n visiting...left");
		System.out.println(e.left());
		e.left().accept(this);	
		System.out.println("Token:"+e.getToken().getText());
		System.out.println("\n visiting...right");
		System.out.println(e.right());
		e.right().accept(this);
	}

 	public void visit(ASTBooleanLiteral e){
		System.out.println("visiting ASTBooleanLiteral...");
	}

	public void visit(ASTCollectionLiteral e){
		System.out.println("visiting ASTCollectionLiteral...");
	}

	public void visit(ASTEmptyCollectionLiteral e){
		System.out.println("visiting EmptyCollectionLiteral...");
	}

	public void visit(ASTEnumLiteral e){
		System.out.println("visiting ASTEnumLiteral...");
	}
	
	public void visit(ASTIfExpression e){
		System.out.println("visiting ASTIfExpression...");
	}

	public void visit(ASTIntegerLiteral e){
		System.out.println(e.value());
	}

	public void visit(ASTLetExpression e){
		System.out.println("visiting ASTLetExpression...");
	}

	public void visit(ASTOperationExpression e){
		System.out.println("visiting ASTOperationExpression...");
		if (e.IsAttrOperation()){
			if (e.source()!=null){
				System.out.println("visiting source...");
				e.source().accept(this);
				System.out.println("operator:"+e.operator().getText());
			}
			else if (e.IsSelfOp()){
				System.out.println("look up context for self keyword:"+currentContext.getContext());
			}
			else{
				System.out.println("a classifier:"+e.operator().getText());
			}
		}
		else{
			e.source().accept(this);
		}
	}

	public void visit(ASTQueryExpression e){
		System.out.println("visiting ASTQueryExpression...");
		System.out.println("variables:"+e.vars());
		List<Token> list = e.vars().idlist();

		for (int i=0;i<list.size();i++)
			System.out.println("id:"+list.get(i)+e.vars().type());
		
		System.out.println("operator:"+e.operator());
		System.out.println("range");
		e.range().accept(this);
		System.out.println("$$$range$$$");
		System.out.println("expression:");
		e.expr().accept(this);
		System.out.println("$$$expression$$$");
	}

	public void visit(ASTRealLiteral e){
		System.out.println("visiting ASTRealLiteral...");
	}

	public void visit(ASTStringLiteral e){
		System.out.println("visiting ASTStringLiteral...");
	}

	public void visit(ASTTupleLiteral e){
		System.out.println("visiting ASTTupleLiteral...");
	}

	public void visit(ASTTypeArgExpression e){
		System.out.println("visiting ASTTypeArgExpression...");
	}

	public void visit(ASTIterateExpression e){
		System.out.println("visiting ASTIterateExpression...");
	}

	public void visit(ASTUndefinedLiteral e){
		System.out.println("visiting ASTUndefinedLiteral...");
	}

	public void visit(ASTUnaryExpression e){
		System.out.println("visiting ASTUnaryExpression...");
	}

	public void visit(ASTConstraintDefinition e){
		for (int i=0;i<e.getInvs().size();i++){
			e.getInvs().get(i).accept(this);
		}
	}

	public void visit(ASTInvariantClause e){
		if (e.getName()!=null)
			System.out.print("Invariant Name:"+e.getName()+":");
		else
			System.out.print("Inv:");
		e.getExpression().accept(this);
		System.out.println("================================");
	}
	
	public void visit(ASTOCLExpression e){
		int i=0;
		System.out.println("==========================OCL PrintVisitor==========================");
		for (i=0;i<e.getConstraints().size();i++){
			System.out.println("Constraint "+ (i+1) +":");
			ASTConstraintDefinition consd = e.getConstraints().get(i);
			if (consd.getVars().size()!=0)
				currentContext.setContext(consd.getVars().get(0).getText());
			consd.accept(this);
		}
		System.out.println("Total Constraints found:"+i);
	}

	public void visit(){
		PrintWriter err=new PrintWriter(System.out,true);
		try{
			System.out.println("visiting...");
			ParseErrorHandler errh = new ParseErrorHandler(oclFile,err);
			lexer = new OCLLexer(new ANTLRFileStream(oclFile));
			CommonTokenStream token = new CommonTokenStream(lexer);
			parser = new OCLParser(token);
			lexer.init(errh);
			ASTOCLExpression astexpt = parser.oclexpression();
			if (errh.errorCount()==0)
				astexpt.accept(this);
			else
				System.err.println(" File <"+oclFile+"> has parsing errors, translating aborted.");
			
		}					
		catch (RecognitionException e) {
           	 err.println(oclFile +":" + 
           	             e.line + ":" +
           	             e.charPositionInLine + ": " + 
           	             e.getMessage());
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
