/**
 *  Written by: Hao Wu
 *  Bug reports to: haowu@cs.num.ie
 * 
 * call dot to generate a graph for each instance.
 * all instance files are placed in the ./dot/
 */

package examples.graphcoloring;
import java.io.*;
	
public final class gendia{

	public static void main (String args[]){
		File d = new File("./dot/");
		for (File f : d.listFiles()){
			if (!f.isDirectory()){
				if (f.getName().endsWith(".dot")){
					try{
						Process p;
						System.out.println("generating..."+f.getName());
						p=Runtime.getRuntime().exec("dot -Tpng "+"./dot/"+f.getName()+" -o "+"./dot/"+f.getName()+".png");
						BufferedReader in= new BufferedReader(new InputStreamReader(p.getInputStream()));
						BufferedReader err= new BufferedReader(new InputStreamReader(p.getErrorStream()));
						String line;
						while ((line=in.readLine())!=null) System.out.println(line);
						while ((line = err.readLine())!=null) System.out.println(line);
						f.delete();
					}
					catch (Exception e){
						System.err.println("cannot generate "+e.getMessage());
					}
									
				}
			}
		}
	}
}
