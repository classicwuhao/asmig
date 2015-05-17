package parser.ocl;

import org.antlr.runtime.Token;

public class ASTPrePostClause extends AST {
	Token fToken;     // pre or post
	Token fName;      // optional
    ASTExpression fExpr;

    public ASTPrePostClause(Token tok, Token name, ASTExpression e) {
        fToken = tok;
        fName = name;
        fExpr = e;
    }

}
