package parser.ocl;

import org.antlr.runtime.Token;

public class ASTVariableInitialization extends AST {
    private Token fName;
    private ASTType fType;
    private ASTExpression fExpr;

    public ASTVariableInitialization(Token name, ASTType type, 
                                     ASTExpression expr) {
        fName = name;
        fType = type;
        fExpr = expr;
    }

    public Token nameToken() {
        return fName;
    }

    public String toString() {
        return "ASTVaribleInitialization: (" + fName + " " + fType + " " + fExpr + ")";
    }
}
