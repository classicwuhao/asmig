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
package atongmu.ast;
import java.util.*;
import java.io.PrintWriter;
import atongmu.err.VarExistsException;
import atongmu.err.DuplicatedDeclaration;
import atongmu.err.VarNullException;
import atongmu.type.*;
import atongmu.value.*;

public final class Vars{
	/* A hash map for variables and declarations */
	private static Map<Declaration,Var> vars=new Hashtable<Declaration,Var>();
	/* A hash map for variables exist in current context */
	private static Map<Integer,Var> context = new Hashtable<Integer,Var>();

	public static boolean addVar(Var var) throws VarExistsException, VarNullException{
		
		if (context.containsKey(var.getName().hashCode())){
			throw new VarExistsException(new PrintWriter(System.err,true));
		}
		if (var.getName()=="" || var.getName()==null){
			//throw an exception
			throw new VarNullException(new PrintWriter(System.err,true));
		}
		context.put(var.getName().hashCode(),var);
		return true;
	}

	public static boolean addDecl(Declaration d, Var v) throws DuplicatedDeclaration {
		if (vars.containsValue(v)){
			//throw an exception
			throw new DuplicatedDeclaration(new PrintWriter(System.err,true));
		}
		vars.put(d,v);
		return true;
	}
	
	/* remove var only */
	public static boolean removeVar(Var v){
		int key = v.getName().hashCode();
		Declaration d=null;
		if (context.containsKey(key))
			context.remove(key);
		else
			return false;		

		for (Declaration dec : vars.keySet())
			if (vars.get(dec)==v){d=dec; break;}
		
		if (d==null)
			throw new FormulaException("variable does not have a declaration definition.");

		vars.remove(d);
		
		return true;
	}

	public static boolean removeConstDecl(ConstDecl dec){
		if (vars.containsValue(dec.getVar())){
			vars.remove(dec);
			return true;
		}
		return false;
	}

	public static Map<Declaration, Var> getVars(){
		return vars;
	}

	public static void resetContext(){
		vars.clear();
		context.clear();	
	}

	public static int varSize(){
		return context.size();
	}

	public static int decVarSize(){
		int size=0;
		for (Declaration d : vars.keySet()){
			Var v = vars.get(d);
			if (!v.isFixed()) size++;
		}
	
		return size;
	}	

	public static Var getVar(int k){
		return context.get(k);
	}

	public static Map<Integer,Var> getContext(){
		return context;
	}

	public static void update(String key, String value){
		int k=key.hashCode();
		if (context.containsKey(k)){
			Var var=context.get(k);
			Type type=var.getType();
			if (type instanceof BoolType && !var.isFixed()){
				var.setValue(new BoolValue (Boolean.valueOf(value)));
			}
			else if (type instanceof IntType && !var.isFixed()){
				/* eliminate the space <BUG>*/
				if (value.charAt(0)=='-'){
					StringBuffer sb = new StringBuffer();
					sb.append('-');
					for (int i=1;i<value.length();i++)
						if (value.charAt(i)!=' ')
							sb.append(value.charAt(i));
					
					value=sb.toString();
				}
				if (type.isEnumType()){
					EnumType t = (EnumType) type;
					var.setValue(new EnumValue(Integer.parseInt(value),t.getLiterals()));
				}
				else
					var.setValue(new IntValue (Integer.parseInt(value)));
			}
		}
	}

	/* check whether a variable has a corresponding declaration */
	public static boolean checkVars(){
		boolean r = true;
		for (int key : context.keySet()){
			Var v = context.get(key);
			if (!vars.containsValue(v)){
				System.err.println("error: Variable ["+v.toString()+"]"+" is not declaraed.");
				r=false;
			}
		}
		return r;
	}

	public static String print2String(){
		StringBuffer sb = new StringBuffer();
		for (int key : context.keySet()){
			Var v = context.get(key);
			sb.append(v.toString()+"\n");
		}
		return sb.toString();
	}

	public static boolean checkVar(Var v){
		return context.containsKey(v.getName())
		&& vars.containsValue(v);
	}
	
}
