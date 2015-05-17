/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  APR-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package atongmu.err;
import java.io.*;
import atongmu.ast.FOFormula;

public final class EmptyFormulaException extends AbstractException{
	private FOFormula fof;
	
	public EmptyFormulaException(PrintWriter e){
		this.err=e;
		this.message="I cannot find any formula.";
		this.err_code=0x15;
	}

	public EmptyFormulaException(PrintWriter e, int c){
		this.err = e;
		this.err_code=c;
	}

	public void printErrMessage(String info){
		if (fof!=null)
			this.err.println("Empty Formula Exception"+"["+info+"]"+":"+this.message);
		else
			this.err.println("Empty Formula Exception"+"[null formula]"+":"+this.message);
	}
	
	public void setFormula(FOFormula f){
		fof=f;
	}
}
