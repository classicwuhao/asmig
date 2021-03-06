package parser.ocl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.runtime.Token;

public class ASTElemVarsDeclaration extends AST {
    private List fIdList;   // (MyToken)
    private ASTType fType;  // optional: may be null

    public ASTElemVarsDeclaration(List idList, ASTType type) {
        fIdList = idList;
        fType = type;
    }

    public ASTElemVarsDeclaration() {
        fIdList = new ArrayList();
        fType = null;
    }

	public List<Token> idlist(){
		return fIdList;
	}

	public ASTType type(){
		return fType;
	}

    /**
     * Returns <tt>true</tt> if this list contains no declarations.
     */
    public boolean isEmpty() {
        return fIdList.isEmpty();
    }

}
