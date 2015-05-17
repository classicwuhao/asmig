/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  JAN-2013
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package atongmu.ocl.exprs;
import java.util.List;
import java.util.ArrayList;
import atongmu.ast.Expression;

public final class StrObjExpr extends OCLExpr{
	private String text;

	public StrObjExpr(String name){text=name;}
	
	public List<Expression> eval(){
		List<Expression> formulas = new ArrayList<Expression>();
		return formulas;
	}

	
	
}
