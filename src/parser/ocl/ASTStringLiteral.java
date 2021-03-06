package parser.ocl;
import org.antlr.runtime.Token;
import parser.ocl.visitor.PrintVisitor;
import parser.ocl.visitor.ReturnVisitor;
import atongmu.ast.Expression;

public class ASTStringLiteral extends ASTExpression {
    private String fValue;

    public ASTStringLiteral(Token token) {
        String st = token.getText(); 
        // strip quotes
        fValue = st.substring(1, st.length() - 1);
        // Read Escape Characters
        convertString();
    }

    public String toString() {
        return "ASTStringLiteral: "+fValue;
    }
    
	public void accept(PrintVisitor v){
		v.visit(this);
	}

	public <E,LN,LE,V> E accept(ReturnVisitor<E,LN,LE,V> v){
		return v.visit(this);
	}

    /**
     * Converts EscapeCharcters in a String to the corresponding Java-Esc-Characters
     * See: http://forum.java.sun.com/thread.jspa?threadID=733734&messageID=4219038
     * @param str
     * @return
     */
    private void convertString() {
        char[] strArr = fValue.toCharArray();
        boolean escape = false;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < strArr.length; ++i) {
            if (escape) {
                if (strArr[i] == 'b') {
                    buf.append('\b');
                } else if (strArr[i] == 't') {
                    buf.append('\t');
                } else if (strArr[i] == 'n') {
                    buf.append('\n');
                } else if (strArr[i] == 'r') {
                    buf.append('\r');
                } else if (strArr[i] == 'f') {
                    buf.append('\f');
                } else if (strArr[i] == 'u') {    				
                    // Unicode escape
                    int utf = Integer.parseInt(fValue.substring(i + 1, i + 5), 16);
                    buf.append((char)utf);
                    i += 4;
                } else if (Character.isDigit(strArr[i])) {
                    // Octal escape
                    int j = 0;
                    for (j = 1; (j < 3) && (i + j < strArr.length); ++j) {
                        if (!Character.isDigit(strArr[i+j]))
                            break;
                    }
                    int octal = Integer.parseInt(fValue.substring(i, i + j), 8);
                    buf.append((char)octal);
                    i += j-1;
                } else {
                    buf.append(strArr[i]);
                }   			
                escape = false;
            } else if (strArr[i] == '\\') {
                escape = true;
            } else {
                buf.append(strArr[i]);
            }    		
        }
        fValue = buf.toString();
    }
}
