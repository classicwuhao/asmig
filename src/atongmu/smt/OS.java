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
package atongmu.smt;

public enum OS{
	WINDOWS {public String toString(){return "WIN";}},
	UNIX {public String toString(){return "UNIX";}},
	LINUX {public String toString(){return "LINUX";}},
	MAC {public String toString(){return "MAC";}},
	SOLARIS{public String toString(){return "SUNOS";}},
	UNKNOWN{public String toString(){return "UNKNOWN";}};
	
}
