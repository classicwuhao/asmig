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
import java.io.*;
import java.util.*;
import atongmu.ast.Vars;
import atongmu.err.UnsatException;
import atongmu.err.UnknownException;
import atongmu.util.SMT2Writer;
import atongmu.ast.FOFormula;

public class SmtInvoker{
	private Solver smt_solver;
	private Solver default_solver;
	private String solver_path;
	private String file;
	private final String UNSAT="unsat";
	private final String SAT="sat";
	private final String UNKNOWN="unknown";
	private final char UNIX_SLASH='/';
	private final char WINDOWS_SLASH='\\';
	private char SLASH;
	private OS operating_system;
	private String args="";	
	private SMT2Writer writer;
	private boolean nothing=false;
	private String tmploc="tmp";
	private boolean warning_flag=false;
	private boolean const_supported=true;

	public SmtInvoker(Solver solver, SMT2Writer filewriter){
		smt_solver=solver;
		writer=filewriter;
		switch (solver){
			case Z3:
				args=" -m -smt2 ";
				const_supported=true;
				break;
			case MATHSAT:
				args=" < ";
				const_supported=false;
				break;
			case SMTINTERPOL: /* this is a solver that is written in java */
				args=" -q ";
				const_supported=false;
				break;
			default:
				args="";
				const_supported=false;
				
		}
		setSlash();
		warning_flag=createTmpLocation();
		nothing=!Vars.checkVars();
		solver_path=setSolverPath();
		
	}

	private void setSlash(){
		CheckOS();
		switch (operating_system){
			case UNIX:
			case LINUX:
			case MAC: 
			case SOLARIS:
				SLASH=UNIX_SLASH;
				break;
			case WINDOWS:
				SLASH=WINDOWS_SLASH;
			default:
				SLASH=UNIX_SLASH;
		}
	}

	/*public void setSolverPath(String location){
		solver_path=location;
		setSlash();
		if (solver_path.charAt(solver_path.length()-1) != SLASH)
			solver_path+=Character.toString(SLASH);
	}*/

	public void setFilePath(String filepath){
		file=filepath;
	}

	public void invoke() throws UnsatException, UnknownException{
		String line;
		if (nothing==true) throw 
			new UnsatException(new PrintWriter(System.out,true));
		writer.setPrintSuccess(false);
		writer.setProduceModel(true);
		writer.setLogic("QF_LIA");
		writer.Traverse(!const_supported);
		writer.Check();
		writer.close(true);
		nothing=false;

		long time=System.currentTimeMillis();
		try{
			Process p;
			if (smt_solver==Solver.SMTINTERPOL){
				//System.out.println("execute...");
				/* SLOW need API(s) to make an improvment */
				p=Runtime.getRuntime().exec("java -jar "+solver_path+smt_solver+".jar"+args+file);
				//System.out.println("java -jar "+solver_path+smt_solver+".jar"+args+file);
			}
			else{
				p=Runtime.getRuntime().exec(solver_path+smt_solver+args+file);
			}
			//Process p=Runtime.getRuntime().exec("../../smtsolver/mathsat ");
			BufferedReader in= new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader err= new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			line = in.readLine();
			//System.out.println("fist line:"+line);
			if (line.indexOf(UNSAT)>=0)
				throw new UnsatException(new PrintWriter(System.out,true));
			
			if (line.indexOf(UNKNOWN)>=0)
				throw new UnknownException(new PrintWriter(System.out,true));
			
			while((line=in.readLine())!=null)
				Parse(line);
			
			in.close();
			while ((line=err.readLine())!=null)
				System.out.println(line);
			err.close();
			//p.waitFor();
			time = System.currentTimeMillis()-time;
			System.out.println("Time took: "+time+" milliseconds");
			//File f = new File(file);
			//if (f.exists()) f.delete();
		}	
		catch (Exception e){
			nothing=true;
			System.err.println("error:"+e.getMessage());
		}
	}

	public Result incSolve() throws UnsatException, UnknownException{	
		/* get the cache file if it does not exist, create one */
		if (nothing==true) return Result.UNSAT;
		Result result = Result.SAT;
		try{
			if (!warning_flag){
				System.err.println("fatal error: failed to create tmp location, aborted.");
				return result=Result.UNKNOWN;
			}
			long time=System.currentTimeMillis();
			nothing=false;
			File cache=new File(tmploc+SLASH+"model.cache");
			File newfile=new File(tmploc+SLASH+"tmp.cache");
			SMT2Writer tmpwriter;
			
			if (!cache.exists()){
				 tmpwriter = new SMT2Writer(new PrintWriter
						(new FileWriter(cache),false),
						writer.getFormula(true));
				//System.out.println("save model");
				tmpwriter.setPrintSuccess(false);
				tmpwriter.setProduceModel(true);
				tmpwriter.setLogic("QF_LIA");
				tmpwriter.Traverse(!const_supported);
				tmpwriter.close(true);
			}
			else{
				FileWriter w= new FileWriter(cache,true);
				tmpwriter=new SMT2Writer(new PrintWriter(w),writer.getFormula(true));
				tmpwriter.WriteFormula();
				tmpwriter.close(true);
			}
		
			/* update the model */
			OutputStream dest=new FileOutputStream(newfile);
			InputStream src = new FileInputStream(cache);
			byte[] buffer = new byte[1024];
			int len;
			while ((len=src.read(buffer))>0)
				dest.write(buffer,0,len);
			src.close();
			dest.close();

			FileWriter f=new FileWriter(newfile,true);
			SMT2Writer incwriter=new SMT2Writer(new PrintWriter(f),writer.getFormula());
			incwriter.Check();
			incwriter.close(true);
			
			/* setup the solver and invoke */
			List<String> solverArgs = new ArrayList<String>();
			solverArgs.add(solver_path+smt_solver);
			switch (smt_solver){
				case Z3:
					//solverArgs.add("-m"); //disabled for Z3-4.3.2
					solverArgs.add("-smt2");					
					break;
				case MATHSAT:
					solverArgs.add("-input='smt2'");
					break;
					//solverArgs.add("<");
				case SMTINTERPOL:
					solverArgs.set(0,"java");
					solverArgs.add("-jar");
					solverArgs.add(solver_path+smt_solver+".jar");
					solverArgs.add("-q");
					break;

				default:
					;
			}
			solverArgs.add(tmploc+SLASH+"tmp.cache");
			ProcessBuilder pb = new ProcessBuilder(solverArgs);
			pb.redirectErrorStream(true); //merge stdout and stderr
			Process p=pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			line = in.readLine();
			if (line.indexOf(Result.UNSAT.toString())>=0){
				result=Result.UNSAT;
				throw new UnsatException(new PrintWriter(System.out,true));
			}

			if (line.indexOf(Result.UNKNOWN.toString())>=0){
				result=Result.UNKNOWN;
				throw new UnknownException(new PrintWriter(System.out,true));
			}
			
			while((line=in.readLine())!=null){	
				//System.out.println(line);
				Parse(line);
			}

			in.close();
			time = System.currentTimeMillis()-time;
			System.out.println("Time took: "+time+" milliseconds");
			return result;			
		}
		catch (Exception e){
			System.err.println("error: no solution found -> "+result.toString());
			nothing=true;
			return result;
		}
	}

	private void Parse(String output){
		int i=0;
		char c=0;
		StringBuffer name = new StringBuffer();
		StringBuffer value = new StringBuffer();

		//System.out.println("output:"+output);
		/* get the name of the variable */
		c=output.charAt(0);
		while (c==' ' || c=='\t' || c=='\n')
			c=output.charAt(++i);

		if (c!=40) {
			System.out.println("getting values error");
			return;
		}
		
		while (c==40){
			c=output.charAt(++i);
			while (c==' ' || c=='\t' || c=='\n')
				c=output.charAt(++i);
		}

		while (c!=' ' && c!='\t' && c!='\n')
			name.append(c=output.charAt(i++));
		
		//System.out.print(name.toString().trim()+" -> ");

		/*get values*/
		while (c== ' ' || c== '\t' || c=='\n')
			c=output.charAt(i++);

		while (c==40){
			c=output.charAt(i++);
			while (c==' ' || c=='\t' || c=='\n')
				c=output.charAt(i++);
		}

		while (c!=')'){
			value.append(c);
			c=output.charAt(i++);
		}
		
		//System.out.println(value.toString().trim());
		updateVars(name.toString().trim(),value.toString().trim());
	}

	private void updateVars(String n, String v){
		if (n!=null && v!=null && n.length()>0 && v.length()>0)
			Vars.update(n,v);
		else
			nothing=true;
		
	}
	
	public boolean more(){
		return !nothing;
	}

	public void showVars(){
		System.out.println(Vars.print2String());
	}

	private boolean createTmpLocation(){
		boolean b;
		try{		
			File dir = new File(tmploc+SLASH);
			if (dir.exists())
				deleteFiles(dir);
			b=dir.mkdir();
			if (!b)
				return false;
			return true;
		}
		catch (Exception e){
			System.err.println("fatal error:"+e.getMessage());
			return false;
		}
	}

	private void deleteFiles(File d){
		for (File f:d.listFiles()){
			if (f.isDirectory())
				deleteFiles(f);
			f.delete();
		}
		d.delete();
	}

	public void releaseFiles(){
		if (!warning_flag) return;
		try{
			File dir = new File(tmploc+SLASH);
			if (dir.exists()) deleteFiles(dir);
		}
		catch(Exception e){
			System.err.println("fatal error:"+e.getMessage());
		}
	}

	private void FindDefaultSolver(){
		/* leave it right now come back when start gui development 
		 * 
 		 * We need to set top directory to the jar file, and find z3 in smtsolver folder.
         * run a test file for the solver.
   		 */
	}

	private String setSolverPath(){
		return 
			".."+
			Character.toString(SLASH)+".."+
			Character.toString(SLASH)+"smtsolver"+
			Character.toString(SLASH);
	}

	private void CheckOS(){
		String os =System.getProperty("os.name").toUpperCase();

		if (os.indexOf(OS.WINDOWS.toString())>=0){operating_system=OS.WINDOWS;return;}
		if (os.indexOf(OS.UNIX.toString())>=0){operating_system=OS.UNIX;return;}
		if (os.indexOf(OS.LINUX.toString())>=0){operating_system=OS.LINUX;return;}
		if (os.indexOf(OS.MAC.toString())>=0){operating_system=OS.MAC;return;}
		if (os.indexOf(OS.SOLARIS.toString())>=0) operating_system=OS.SOLARIS;
		else operating_system=OS.UNKNOWN;
	}
}
