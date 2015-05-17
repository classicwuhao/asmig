/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  MAR-2013 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package atongmu.translator;

public class AssociationSymbol extends Symbol{
	private String name;
	private int hash;
	private int lower;
	private int upper;
	private boolean binary;

	public AssociationSymbol(String n, int h, int l, int u, boolean b){
		name = n;
		hash = h;
		lower = l;
		upper = u;		
		binary = b;
	}
	
	public int getLower(){return lower;}
	public int getUpper(){return upper;}
	@Override
	public int hashCode(){return hash;}
	public String getName(){return name;}
	@Override
	public String toString(){return "Association Symbol:"+name;}
	@Override
	public boolean isUnaryAssociation(){return !binary;}
	@Override
	public boolean isBinaryAssociation(){return binary;}
		
}
