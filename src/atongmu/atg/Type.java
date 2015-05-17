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
package atongmu.atg;

public class Type{
	private String name="";
	private long typeid;
	public Type(){}
	public Type(String newname,long id){
		name = newname;
		typeid=id;
	}
	public String getName(){return name;}
	public void setName(String n){name=n;}
	public String toString(){return name+"<"+ typeid +">";}
	public long getTypeID(){return typeid;}
	public void setTypeID(long id){typeid=id;}
}
