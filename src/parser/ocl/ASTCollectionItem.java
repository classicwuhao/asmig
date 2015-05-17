package parser.ocl;

public class ASTCollectionItem extends AST {
    ASTExpression fFirst;
    ASTExpression fSecond; // may be null

    public void setFirst(ASTExpression expr) {
        fFirst = expr;
    }

    public void setSecond(ASTExpression expr) {
        fSecond = expr;
    }

}
