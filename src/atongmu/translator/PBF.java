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

package atongmu.translator;

import java.util.*;
import org.eclipse.emf.ecore.*;
import atongmu.ast.Argument;
import atongmu.ast.BinaryExpression;
import atongmu.ast.Constant;
import atongmu.ast.ConstDecl;
import atongmu.ast.Declaration;
import atongmu.ast.FunExpression;
import atongmu.ast.NegFun;
import atongmu.ast.Var;
import atongmu.ast.NumLiteral;
import atongmu.ast.BoolLiteral;
import atongmu.ast.ArithmeticExpression;
import atongmu.ast.Connective;
import atongmu.ast.Arithmetic;
import atongmu.ast.FOFormula;
import atongmu.ast.StrVar;
import atongmu.ast.Expression;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.EnumType;
import atongmu.type.Type;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;


/* Partition Based Formula Generation */
/* If you are reading this code, I bet you won't understand the meaning of final formula. :-D */
public final class PBF{	
	private HashMap<Criteria, List<Var>> criteria;
	private HashMap<Integer, List<Var>> partitions;
	private HashMap<Var, Expression> formulaMap;
	private HashMap<Var, Var> map = new HashMap<Var, Var>();
	private FOFormula pbformula= new FOFormula();
	private a2f modelTranslator;
	private Criteria _criteria;
	private FormulaCache cache;
	private static int totalCount=0;
	private static int auxCount=0;
	private boolean isUnique=true;
	

	public PBF(a2f translator, Criteria c){
		modelTranslator = translator;
		criteria = new HashMap<Criteria,List<Var>>();
		_criteria = c;
		createCriteria(c);
		cache = modelTranslator.getCache();
		partitions = new HashMap<Integer, List<Var>>();
		formulaMap = new HashMap<Var, Expression>();
	}
	
	public void genAttributeCriteria(){
		int index=0;
		List<Var> pbv = criteria.get(Criteria.CA);
		
		for (Edge e : modelTranslator.getAttributes1().keySet()){
			Var node = modelTranslator.getObjects().get(e.source());
			Var attr = modelTranslator.getData().get(e.dest());
			if (attr.isEnumType())
				continue;
			if (attr.isIntType()){
				Var v = createPBV();
				pbv.add(v);
				map.put(v,attr);
				pbformula.addExpression(node);					
			}
			if (attr.isBoolType()){
				Var v = createPBV();
				pbv.add(v);
				map.put(v,attr);
				pbformula.addExpression(node);
			}
		}
		gen(pbv);
	}

	public void genAssociationCriteria(){
		
		List<Symbol> symbols = cache.getSymbols();
				
		for (Symbol sym : symbols){
			if (sym.isUnaryAssociation())
				genUnaryAssociation((AssociationSymbol)sym);
			if (sym.isBinaryAssociation())
				genBinaryAssociation((AssociationSymbol)sym);
			
		}

		genAssoc();
		
	}
	
	private void genUnaryAssociation(AssociationSymbol sym){
		if (sym.getLower()==0 && sym.getUpper()==1){
			//System.out.println("1-->0..1"+sym);
			genZeroOrOne(sym);		
		}
		else if (sym.getLower()==0 && sym.getUpper()==-1){
			//System.out.println("1-->0..*"+sym);
			genNoneOrStar(sym);
		}
		else if (sym.getLower()==1 && sym.getUpper()==-1){
			//System.out.println("1-->1..*"+sym);
			genSome(sym);
		}
	}

	private void genBinaryAssociation(AssociationSymbol sym){
		if (sym.getLower()==0 && sym.getUpper()==1){
			//System.out.println("1<-->0..1"+sym);
			genBiZero2One(sym);
		}
		else if (sym.getLower()==1 && sym.getUpper()==-1){
			//System.out.println("1-->1..*"+sym);
			genBiSome(sym);
		}
		else if (sym.getLower()==0 && sym.getUpper()==-1){
			//System.out.println("1-->0..*"+sym);
			genBiZero2Star(sym);
		}
		
	}

	private void genSome(AssociationSymbol sym){
		List<Var> vars = createPartition(2);
		List<Var> list = new ArrayList<Var>();
		List<Var> aux = new ArrayList<Var>();
		ArithmeticExpression arith0,arith1;
		int max = 0;
		Var pbv;
	
		/**
         * Iterate each formula record (from the cache), 
		 * For each record, create a pbv and get the size of vars.
         * For each var: there is a unique auxiliary variable corresponding to each edge.	 
         * The domain for each auxiliary variable is between 0 and 1, 1: edge is selected (0: cannot be chosen).
         * An equation can be formed based on this encoding which controls the number of edges to be chosen by a SMT solver.
         * In the last step, each pbv implies each sub-formula which captures each partition.
         */
		for (FormulaRecord record : cache.lookup(sym)){
			Expression formula = record.getRecord();
			list.clear();aux.clear();
			if (formula.isBinaryExpression()){
				extractVars((BinaryExpression)formula,list);
				vars.add(pbv = createPBV()); /* update the table */
				pbformula.addExpression(FOFormula.Range(0,1,pbv)); /* domain for pbv */
				if ((max = list.size())==0)
					throw new CriteriaException("Error: Formula is empty, cannot form an appropriate criteria predicate.");

				for (int i=0;i<list.size();i++){ /*constraints for each aux var.*/	
					Var auxv = createAux();
					aux.add(auxv);
					pbformula.addExpression(FOFormula.Range(0,1,auxv));				
					pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxv,new NumLiteral(1))
											,list.get(i)));
					pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxv,new NumLiteral(0))
											,new NegFun(list.get(i))));
				}
				/* prevent FOFormula from throwing exceptions on an unhappy formula size */
				if (list.size()>1){
					arith0 = FOFormula.join(Arithmetic.PLUS,aux);
					arith1 = FOFormula.join(Arithmetic.PLUS,aux);
				}
				else{
					arith0 = new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(1));
					arith1 = new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(max));
				}
				
				/* generate final formula for this partition. */
			
				pbformula.addExpression(new BinaryExpression(Connective.OR,
										new BinaryExpression(Connective.AND,
										new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)),
										new ArithmeticExpression(Arithmetic.EQUAL,arith0,new NumLiteral(1))),
										new BinaryExpression(Connective.AND,
										new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)),
										new ArithmeticExpression(Arithmetic.EQUAL,arith1,new NumLiteral(max)))
									));
			}
		}// end of for
	}

	private void genNoneOrStar(AssociationSymbol sym){
		List<Var> vars = createPartition(2);
		List<Var> list = new ArrayList<Var>();
		List<Var> aux = new ArrayList<Var>();
		ArithmeticExpression arith0,arith1;
		int max = 0;
		Var pbv;

		for (FormulaRecord record : cache.lookup(sym)){
			Expression formula = record.getRecord();
			list.clear();aux.clear();
			if (formula.isBinaryExpression()){
				extractVars((BinaryExpression)formula,list);
				vars.add(pbv = createPBV()); /* update the table */
				pbformula.addExpression(FOFormula.Range(0,1,pbv)); /* domain for pbv */
				if ((max = list.size())==0)
					throw new CriteriaException("Error: Formula is empty, cannot form an appropriate criteria predicate.");

				for (int i=0;i<list.size();i++){ /*constraints for each aux var.*/	
					Var auxv = createAux();
					aux.add(auxv);
					pbformula.addExpression(FOFormula.Range(0,1,auxv));				
					pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxv,new NumLiteral(1))
											,list.get(i)));
					pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxv,new NumLiteral(0))
											,new NegFun(list.get(i))));
				}
				/* prevent FOFormula from throwing exceptions on an unhappy formula size */
				if (list.size()>1){
					arith0 = new ArithmeticExpression(Arithmetic.EQUAL,FOFormula.join(Arithmetic.PLUS,aux),new NumLiteral(0));
					arith1 = new ArithmeticExpression(Arithmetic.EQUAL,FOFormula.join(Arithmetic.PLUS,aux),new NumLiteral(max));
				}
				else{
					arith0 = new ArithmeticExpression(Arithmetic.EQUAL,aux.get(0),new NumLiteral(0));
					arith1 = new ArithmeticExpression(Arithmetic.EQUAL,aux.get(0),new NumLiteral(max));
				}
				
				/* generate final formula for this partition. */
				pbformula.addExpression(new BinaryExpression(Connective.OR,
											new BinaryExpression(Connective.AND,new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)),arith0),
											new BinaryExpression(Connective.AND,new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)),arith1))
										);
			}
		}// end of for		
	
	}

	private void genZeroOrOne(AssociationSymbol sym){
		List<Expression> left = new ArrayList<Expression>();
		List<Expression> right = new ArrayList<Expression>();
		List<Var> vars = createPartition(2);
		Var var0;		

		for (FormulaRecord record : cache.lookup(sym)){
			Expression formula = record.getRecord();
			if (formula.isBinaryExpression()){
				left.add(((BinaryExpression)formula).getLeft());
				right.add(((BinaryExpression)formula).getRight());
			}
		}

		vars.add(var0=createPBV());
		/* set up a range for each pbv */		
		pbformula.addExpression(FOFormula.Range(0,1,var0));

		/* formulate formuals based on children's size */	
		BinaryExpression b0 = (left.size()>1) ? 
								new BinaryExpression(Connective.AND,
								new BinaryExpression(Connective.EQUAL, var0, new NumLiteral(0)),
								FOFormula.join(Connective.AND,left)) :
								new BinaryExpression(Connective.IMPLIES,
								new BinaryExpression(Connective.EQUAL,var0, new NumLiteral(0)),left.get(0));

		BinaryExpression b1 = (right.size()>1) ? 
								new BinaryExpression(Connective.AND,
								new BinaryExpression(Connective.EQUAL, var0, new NumLiteral(1)),
								FOFormula.join(Connective.AND,right)) :
								new BinaryExpression(Connective.IMPLIES,
								new BinaryExpression(Connective.EQUAL,var0, new NumLiteral(1)),right.get(0));

		pbformula.addExpression(FOFormula.join(Connective.OR,b0,b1));	
					
	}
	
	private void genBiSome(AssociationSymbol sym){
		List<Var> vars = createPartition(2);
		Var pbv;
		MultiFormulaRecord record;
		List<Var> list = new ArrayList<Var>();		
		ArrayList<FormulaRecord> records = cache.lookup(sym);
		List<Expression> exprs0 = new ArrayList<Expression>();
		List<Expression> exprs1 = new ArrayList<Expression>();
		BinaryExpression f0,f1;
		int max;

		if (!records.get(0).isMultiFormulaRecord())
			throw new CacheException("Error: I am expecting a MultiFormulaRecord type here.");

		record = (MultiFormulaRecord)records.get(0);

		vars.add(pbv=createPBV());
		pbformula.addExpression(FOFormula.Range(0,1,pbv));
	
		SingleFormulaRecord[][] singleRecords = record.getFormulas();
		Var[][] auxs = new Var[record.getM()][record.getN()];				
		
			/* setup auxiliary varaibles for each e */
		for (int i=0;i<singleRecords.length;i++){
			list.clear();
			for (int j=0;j<singleRecords[i].length;j++){
				auxs[i][j]=createAux();
				pbformula.addExpression(FOFormula.Range(0,1,auxs[i][j]));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(1))
											,singleRecords[i][j].getRecord()));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(0))
											,new NegFun(singleRecords[i][j].getRecord())));
			}
		}

		for (int i=0;i<auxs[0].length;i++){
			list.clear();
			for (int j=0;j<auxs.length;j++)
				list.add(auxs[j][i]);

			if (list.size()>1){
				exprs0.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(1)));

				if (auxs[0].length>1)
					exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(auxs.length-(auxs[0].length-1))));
				else
					exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(auxs.length)));
			}
			else{
				exprs0.add(new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(1)));
				exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(1)));
			}
		}

		if (exprs0.size()>1){
			f0 = new BinaryExpression(Connective.AND,FOFormula.join(Connective.OR,exprs0),
			new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)));
			f1 = new BinaryExpression(Connective.AND,FOFormula.join(Connective.OR,exprs1),
			new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));
		}
		else{
			f0 = new BinaryExpression(Connective.AND,exprs0.get(0),new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)));
			f1 = new BinaryExpression(Connective.AND,exprs1.get(0),new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));
		}
		pbformula.addExpression(FOFormula.join(Connective.OR,f0,f1));
	}

	private void genBiZero2One(AssociationSymbol sym){
		List<Var> vars = createPartition(2);
		Var pbv;
		MultiFormulaRecord record;
		List<Var> list = new ArrayList<Var>();
		List<Expression> exprs = new ArrayList<Expression>();
		Expression formula0=null;
		BinaryExpression f0,f1;
		ArrayList<FormulaRecord> records = cache.lookup(sym);
	
		if (!records.get(0).isMultiFormulaRecord())
			throw new CacheException("Error: I am expecting a MultiFormulaRecord type here.");

		record = (MultiFormulaRecord)records.get(0);

		vars.add(pbv=createPBV());
		pbformula.addExpression(FOFormula.Range(0,1,pbv));
	
		SingleFormulaRecord[][] singleRecords = record.getFormulas();
		Var[][] auxs = new Var[record.getM()][record.getN()];

		/* setup auxiliary varaibles for each e */
		for (int i=0;i<singleRecords.length;i++){
			list.clear();
			for (int j=0;j<singleRecords[i].length;j++){
				auxs[i][j]=createAux();
				pbformula.addExpression(FOFormula.Range(0,1,auxs[i][j]));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(1))
											,singleRecords[i][j].getRecord()));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(0))
											,new NegFun(singleRecords[i][j].getRecord())));

				list.add((Var)singleRecords[i][j].getRecord()); /* are you sure it must be a var ? */
				//System.out.print(singleRecords[i][j].getRecord()+" , ");
			}
				//System.out.println();
				formula0 = (formula0==null) ? FOFormula.None(list) : 
				new BinaryExpression(Connective.AND,formula0,FOFormula.None(list));
		}
		
		
		f0 = new BinaryExpression(Connective.AND,new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)),formula0);
		
		for (int i=0;i<auxs[0].length;i++){
			list.clear();
			for (int j=0;j<auxs.length;j++)
				list.add(auxs[j][i]);

			if (list.size()>1)
				exprs.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(1)));
			else
				exprs.add(new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(1)));
		}
		
		if (exprs.size()>1)		
			f1= new BinaryExpression(Connective.AND,FOFormula.join(Connective.OR,exprs),
				new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));
		else
			f1= new BinaryExpression(Connective.AND,exprs.get(0),
				new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));

		pbformula.addExpression(FOFormula.join(Connective.OR,f0,f1));
	}
		
	private void genBiZero2Star(AssociationSymbol sym){
		List<Var> vars = createPartition(2);
		Var pbv;
		MultiFormulaRecord record;
		List<Var> list = new ArrayList<Var>();		
		ArrayList<FormulaRecord> records = cache.lookup(sym);
		List<Expression> exprs0 = new ArrayList<Expression>();
		List<Expression> exprs1 = new ArrayList<Expression>();
		BinaryExpression f0,f1;
		int max;

		if (!records.get(0).isMultiFormulaRecord())
			throw new CacheException("Error: I am expecting a MultiFormulaRecord type here.");

		record = (MultiFormulaRecord)records.get(0);

		vars.add(pbv=createPBV());
		pbformula.addExpression(FOFormula.Range(0,1,pbv));
	
		SingleFormulaRecord[][] singleRecords = record.getFormulas();
		Var[][] auxs = new Var[record.getM()][record.getN()];				
		
			/* setup auxiliary varaibles for each e */
		for (int i=0;i<singleRecords.length;i++){
			list.clear();
			for (int j=0;j<singleRecords[i].length;j++){
				auxs[i][j]=createAux();
				pbformula.addExpression(FOFormula.Range(0,1,auxs[i][j]));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(1))
											,singleRecords[i][j].getRecord()));
				pbformula.addExpression(new BinaryExpression(Connective.IMPLIES,
											new ArithmeticExpression(Arithmetic.EQUAL,auxs[i][j],new NumLiteral(0))
											,new NegFun(singleRecords[i][j].getRecord())));
			}
		}

		for (int i=0;i<auxs[0].length;i++){
			list.clear();
			for (int j=0;j<auxs.length;j++)
				list.add(auxs[j][i]);

			if (list.size()>1){
				exprs0.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(0)));

				if (auxs[0].length>1)
					exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(list.size())));
				else
					exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,
						FOFormula.join(Arithmetic.PLUS,list),new NumLiteral(auxs.length)));
			}
			else{
				exprs0.add(new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(0)));
				exprs1.add(new ArithmeticExpression(Arithmetic.EQUAL,list.get(0),new NumLiteral(1)));
			}
		}

		if (exprs0.size()>1){
			f0 = new BinaryExpression(Connective.AND,FOFormula.join(Connective.OR,exprs0),
			new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)));
			f1 = new BinaryExpression(Connective.AND,FOFormula.join(Connective.OR,exprs1),
			new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));
		}
		else{
			f0 = new BinaryExpression(Connective.AND,exprs0.get(0),new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(0)));
			f1 = new BinaryExpression(Connective.AND,exprs1.get(0),new ArithmeticExpression(Arithmetic.EQUAL,pbv,new NumLiteral(1)));
		}
		pbformula.addExpression(FOFormula.join(Connective.OR,f0,f1));	

	}

	

	/* recursively traverse a binary expression. */
	private void extractVars(Expression expr,List<Var> list){
		if (expr.isVar()){
			list.add((Var)expr);}
		else {
			if (expr.isBinaryExpression()){
				extractVars(((BinaryExpression)expr).getLeft(),list);
				extractVars(((BinaryExpression)expr).getRight(),list);
			}
		}
	}

	private List<Var> createPartition(int k){
		List<Var> vars;		
		vars = (partitions.containsKey(k)) ? partitions.get(k) : new ArrayList<Var>();
		
		partitions.put(k,vars);
		return vars;
	}

	private void gen(List<Var> pbv){
		List<Var> vari = new ArrayList<Var>();

		/* set up the range for each pbv */
		for (int i=0;i<pbv.size();i++){
			Var attrv = map.get(pbv.get(i));

			if (attrv.isIntType())
				pbformula.addExpression(FOFormula.Range(0,2,pbv.get(i)));
			else if (attrv.isBoolType())
				pbformula.addExpression(FOFormula.Range(0,1,pbv.get(i)));

			// by this stage, it should be only int and bool type, no other cases exist.
		}

		for (int i=0;i<pbv.size();i++){
			Var _pbv = pbv.get(i);
			Var _datav = map.get(_pbv);
			if (_datav.isIntType()){
				vari.add(_datav);
				pbformula.addExpression(genIntAttr(_pbv,_datav));
			}
			if (_datav.isBoolType()){
				pbformula.addExpression(genBoolAttr(_pbv,_datav));
			}
		}
		
		/* additional constraints */
		/*if (map.size()>1){
			Var vars[] = new Var[map.size()];
			for (Var v : map.keySet()) vars[index++]=v;
			pbformula.addExpression(FOFormula.Same(vars));
		}*/
		
		genAttr(pbv);
		/* set up a unique value for each int attr */
		//if (isUnique) pbformula.addExpression(FOFormula.Unique(vari));

	}
	
	private Expression genIntAttr(Var _pbv, Var _datav){

		BinaryExpression formula0 = new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,_pbv,new NumLiteral(0)),
			new ArithmeticExpression(Arithmetic.LESS,_datav,new NumLiteral(3)));

		BinaryExpression formula1 = new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,_pbv,new NumLiteral(1)),
			new ArithmeticExpression(Arithmetic.EQUAL,_datav,new NumLiteral(3)));

		BinaryExpression formula2 = new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,_pbv,new NumLiteral(2)),
			new ArithmeticExpression(Arithmetic.GREATER,_datav,new NumLiteral(3)));
	
		return FOFormula.join(Connective.AND,formula0,formula1,formula2);

	}

	private Expression genBoolAttr(Var _pbv, Var _datav){
		BinaryExpression formula0 = new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,_pbv,new NumLiteral(0)),
			_datav);

		BinaryExpression formula1 = new BinaryExpression(Connective.IMPLIES,
			new ArithmeticExpression(Arithmetic.EQUAL,_pbv,new NumLiteral(1)),
			new NegFun(_datav));

		return FOFormula.join(Connective.AND,formula0,formula1);
	}

	private void genAttr(List<Var> pbv){
		List<Var> vari = new ArrayList<Var>();
		List<Var> varb = new ArrayList<Var>();
		
		for (int i=0;i<pbv.size();i++){
			Var attrv = map.get(pbv.get(i));
			if (attrv.isIntType())
				vari.add(pbv.get(i));
			else if (attrv.isBoolType())
				varb.add(pbv.get(i));
		}

		if (vari.size()>1) pbformula.addExpression(FOFormula.Same(vari));
		if (varb.size()>1) pbformula.addExpression(FOFormula.Same(varb));
		
	}

	private void genAssoc(){
		for (Integer k : partitions.keySet())
			if (partitions.get(k).size()>1) pbformula.addExpression(FOFormula.join(Connective.OR,
							FOFormula.DifferentColor(partitions.get(k)),FOFormula.Same(partitions.get(k))));
		//pbformula.addExpression(FOFormula.Same(partitions.get(k)));
		
	}

	private void createCriteria(Criteria c){
		if (criteria.containsKey(c))
			throw new CriteriaException("Error: Criteria ["+c+"] already exists.");

		criteria.put(c,new ArrayList<Var>());
	}
	
	private Var[][] transpose(Var[][] m){
		if (m==null) throw new CriteriaException("Error: Cannot transpose this array.");

		Var newm[][] = new Var[m[0].length][m.length];
		
		for (int i=0;i<m.length;i++){
			for (int j=0;j<m[i].length;j++){
				newm[j][i] = m[i][j];
			}
		}
		
		return newm;
	}

	private Var createPBV(){
		Var v = new Var("pbv"+totalCount++,new IntType()); 
		ConstDecl d = new ConstDecl(v);
		return v;
	}

	private Var createAux(){
		Var v = new Var("aux"+auxCount++,new IntType());
		ConstDecl d = new ConstDecl(v);
		return v;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		List<Expression> exprs = pbformula.getExpressions();
		for (int i=0;i<exprs.size();i++)
			sb.append(exprs.get(i)+"\n");

		return sb.toString();
	}

	public Var[] getPBV(){
		int i=0,s=0;

		Var vars[] =new Var[map.size()+getTotalVars()];
		for (Var v:map.keySet()) vars[i++]=v;
		for (Integer k:partitions.keySet()){
			List<Var> list = partitions.get(k);
			for (int j=0;j<list.size();j++) vars[i++]=list.get(j);	
		}
		
		return vars;
	}

	private int getTotalVars(){
		int s=0;
		for (Integer k : partitions.keySet()) s+=partitions.get(k).size();
		return s;
	}

	public List<Expression> getFormula(){return pbformula.getExpressions();}
}
