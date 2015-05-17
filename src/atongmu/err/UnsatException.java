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

public final class UnsatException extends AbstractException{
	
	public UnsatException(PrintWriter e){
		this.err=e;
		this.message="Couldn't find any models due to the unsatisfiability of the model.";
		this.err_code=0x19;
	}

	public UnsatException(PrintWriter e, int c){
		this.err = e;
		this.err_code=c;
	}

	public void printErrMessage(String info){
		this.err.println("unsat model exception"+"["+info+"]"+":"+this.message);
	}
	
}
