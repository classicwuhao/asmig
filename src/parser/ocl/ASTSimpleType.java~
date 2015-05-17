package parser.ocl;

import org.antlr.runtime.Token;

public class ASTSimpleType extends ASTType {
    private Token fName;

    public ASTSimpleType(Token name) {
        fName = name;
    }

	public String type(){
		return fName.getText();
	}

    public String toString() {
        return "ASTSimpleType: "+fName.toString();
    }
}
