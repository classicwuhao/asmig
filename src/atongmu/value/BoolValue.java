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
package atongmu.value;

public class BoolValue extends Value{
	public static final BoolValue TRUE = new BoolValue(true);
	public static final BoolValue FALSE = new BoolValue(false);
	private boolean value;
	public BoolValue(){}
	public BoolValue(boolean b){value=b;}
	public boolean getValue(){return value;}
	public void setValue(boolean n){value=n;}
	public String toString(){return (value==false) ? "false" : "true";}
}
