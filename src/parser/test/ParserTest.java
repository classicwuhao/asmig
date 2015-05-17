package parser.test;
import org.antlr.runtime.*;
import parser.ocl.*;
import java.io.*;
import java.util.*;
	
public class ParserTest{

	public static void main (String args[]){
		OCLParser parser;
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

					for (int k=0;k<tokens.size();k++){
						Token t = tokens.get(k);
						System.out.println("token:"+t.getText());
					}

				}
				//ASTType asttype = parser.typeOnly();
				System.out.println("ErrorCount:"+errh.errorCount());
			}
		}

		catch (RecognitionException e) {
           	 err.println(args[0] +":" + 
           	             e.line + ":" +
           	             e.charPositionInLine + ": " + 
           	             e.getMessage());
		}
		catch (IOException e){
			e.printStackTrace();
		}
		

	}

}
