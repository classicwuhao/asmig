package parser.ocl;

import org.antlr.runtime.Token;

public class ASTTupleItem extends AST {
    private Token fName;
    private ASTExpression fExpr;

    public ASTTupleItem(Token name, ASTExpression expr) {
        fName = name;
        fExpr = expr;
    }

    public Token name() {
        return fName;
    }

    public ASTExpression expression() {
        return fExpr;
    }
}

