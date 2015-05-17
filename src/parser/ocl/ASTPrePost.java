package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.Token;

public class ASTPrePost extends AST {
    private Token fClassName;
    private Token fOpName;
    private List fParamList;    // (ASTVariableDeclaration)
    private ASTType fResultType; // optional
    private List fPrePostClauses;

    public ASTPrePost(Token classname, Token opname, 
                      List paramList, ASTType resultType) {
        fClassName = classname;
        fOpName = opname;
        fParamList = paramList;
        fResultType = resultType;
        fPrePostClauses = new ArrayList();
    }

    public void addPrePostClause(ASTPrePostClause ppc) {
        fPrePostClauses.add(ppc);
    }

}
