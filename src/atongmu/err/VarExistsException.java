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
public final class VarExistsException extends AbstractException{

	public VarExistsException(PrintWriter e){
		this.err=e;
		this.message="I can not make a new variable because it already exists";
		this.err_code=0x10;
	}

	public VarExistsException(PrintWriter e, int c){
		this.err = e;
		this.err_code=c;
	}

	public void printErrMessage(String varname){
		this.err.println("VarExistsException@"+"["+ varname+"]:"+this.message);
	}
}
