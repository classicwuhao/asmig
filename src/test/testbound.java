
package test;
import java.io.*;
import java.util.*;
import atongmu.ast.*;
import atongmu.atg.*;
import atongmu.type.*;
import atongmu.util.*;
import atongmu.value.*;
import atongmu.err.*;
import atongmu.ast.visitor.*;
import atongmu.util.SMT2Writer;
import atongmu.smt.*;
import atongmu.translator.*;
import atongmu.interpreter.*;

public class testbound{
	private static String testfile="../../test/student.ecore"; 


	public static void main (String args[]){
		if (args.length>=1){
			testfile = args[0];
			testBound1();
		}
		
	}

	public static void testBound1(){
		System.out.println("#########Testing Bound Calculation#########");
		Bound bound = new Bound(testfile);
		BoundCalculator bc = new BoundCalculator(bound);
		bc.calculate();
		//System.out.println(bc.itree2String());
		
	}
		
	

}
