package parser.ocl;

import org.antlr.runtime.Token;

public class ASTVariableDeclaration extends AST {
    private Token fName;
    private ASTType fType;

    public ASTVariableDeclaration(Token name, ASTType type) {
        fName = name;
        fType = type;
    }

    public Token name() {
        return fName;
    }
}

