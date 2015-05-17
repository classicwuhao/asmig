package parser.ocl;

import org.antlr.runtime.Token;

public abstract class ASTType extends AST {
    // first token of type, useful for error reporting
    private Token fStartToken; 

    public void setStartToken(Token pos) {
        fStartToken = pos;
    }

    public Token getStartToken() {
        return fStartToken;
    }

}
