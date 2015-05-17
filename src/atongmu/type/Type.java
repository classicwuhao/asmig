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
package atongmu.type;

public abstract class Type{
	public abstract String toString();
	/* for solvers those who don't support constant declaration */
	public abstract String toFun(); 

	public boolean isBoolType(){
		return (this instanceof BoolType);
	}

	public boolean isIntType(){
		return (this instanceof IntType);
	}

	public boolean isEnumType(){
		return (this instanceof EnumType);
	}
	
}
