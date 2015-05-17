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

public enum PrimitiveType{
	INT{
		public String toString(){return "integer";}
		public long id(){return toString().hashCode();}
	},
	STRING{
		public long id(){return toString().hashCode();}
		public String toString(){return "string";}
	},
	ENUM{
		public long id(){return toString().hashCode();}
		public String toString(){return "enum";}
	},
	BOOL{
		public long id(){return toString().hashCode();}
		public String toString(){return "boolean";}
	};
}
