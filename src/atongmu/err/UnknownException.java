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

public final class UnknownException extends AbstractException{
	
	public UnknownException(PrintWriter e){
		this.err=e;
		this.message="cannot decide the model instances";
		this.err_code=0x20;
	}

	public UnknownException(PrintWriter e, int c){
		this.err = e;
		this.err_code=c;
	}

	public void printErrMessage(String info){
		this.err.println("unknown model exception"+"["+info+"]"+":"+this.message);
	}
	
}
