/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  JUL-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package atongmu.translator;

import java.util.*;
import java.io.*;
import parser.ocl.OCLLexer;
import parser.ocl.OCLParser;
import parser.ocl.ASTExpression;
import parser.ocl.ASTElemVarsDeclaration;
import parser.ocl.ASTAllInstancesExpression;
import parser.ocl.ASTBinaryExpression;
import parser.ocl.ASTBooleanLiteral;
import parser.ocl.ASTCollectionLiteral;
import parser.ocl.ASTEmptyCollectionLiteral;
import parser.ocl.ASTEnumLiteral;
import parser.ocl.ASTIfExpression;
import parser.ocl.ASTIntegerLiteral;
import parser.ocl.ASTLetExpression;
import parser.ocl.ASTOperationExpression;
import parser.ocl.ASTQueryExpression;
import parser.ocl.ASTRealLiteral;
import parser.ocl.ASTStringLiteral;
import parser.ocl.ASTTupleLiteral;
import parser.ocl.ASTTypeArgExpression;
import parser.ocl.ASTUnaryExpression;
import parser.ocl.ASTIterateExpression;
import parser.ocl.ASTConstraintDefinition;
import parser.ocl.ASTInvariantClause;
import parser.ocl.ASTUndefinedLiteral;
import parser.ocl.ASTOCLExpression;
import parser.ocl.ASTContext;
import parser.ocl.ASTSimpleType;
import parser.ocl.visitor.ReturnVisitor;
import parser.ocl.ParseErrorHandler;
import org.antlr.runtime.*;
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
import atongmu.ast.Expression;
import atongmu.type.BoolType;
import atongmu.type.IntType;
import atongmu.type.Type;
import atongmu.atg.Node;
import atongmu.atg.NodeType;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.DataNode;
import atongmu.atg.PrimitiveType;
import atongmu.atg.Factory;

/**
 * NTOTE: This module is not fully completed yet, it supports a small subset of OCL.
 * @FIXME: Need to change OCL AST a bit in order to fit more nicely with a return visitor.
 */
public final class o2f extends ReturnVisitor<Expression,List<Node>,List<Expression>,Void>{
	private String oclFile;
	private OCLParser parser;
	private OCLLexer lexer;
	private Factory factory;
	private a2f modelTranslator;
	private ASTContext currentContext;// = new ASTContext();
	private List<Expression> exprs = new ArrayList<Expression>();
	private List<Expression> cons = new ArrayList<Expression>();
	private List<Node> nodes = new ArrayList<Node>();
	private FOFormula oclfof = new FOFormula();	
	private HashMap<Integer,String> vartable = null;
	private boolean edge_set = false;
	private boolean left_edge=false;
	private boolean right_edge=false;
	private TreeSet<Edge> edges;

	public o2f(){}

	public o2f(String file,Factory fact, a2f translator){
		oclFile=file;
		factory=fact;
		modelTranslator = translator;
	}
	
	public Void visit(ASTOCLExpression e){
		int i=0;
		System.out.println("==========================OCL ReturnVisitor==========================");
		for (i=0;i<e.getConstraints().size();i++){
			System.out.println("Constraint "+ (i+1) +":");
			ASTConstraintDefinition consd = e.getConstraints().get(i);
			consd.accept(this);
		}
		//return cons;
		return null;
	}

	public Void visit(ASTConstraintDefinition e){
		List<Expression> list = new ArrayList<Expression>();
		for (int i=0;i<e.getInvs().size();i++){
			currentContext = new ASTContext(e.getVars().get(0).getText());
			//Token tok = e.getVars().get(0);
			//System.out.println(tok);
			//System.out.println("size:"+e.getInvs().size());
			list = e.getInvs().get(i).accept(this);
			if (list==null) continue;
			for (int j=0;j<list.size();j++)
				oclfof.addExpression(list.get(j));
			
		}
		//return exprs;
		return null;
	}

	public List<Expression> visit(ASTInvariantClause e){
		/* cast ??? this is ugly.... */
		ASTExpression expr = e.getExpression();
		if (expr instanceof ASTAllInstancesExpression){
			ASTAllInstancesExpression expr1 = (ASTAllInstancesExpression) expr; return expr1.accept(this);}
		else if (expr instanceof ASTBinaryExpression){
			ASTBinaryExpression expr2 = (ASTBinaryExpression) expr; return expr2.accept(this);}
		else if (expr instanceof ASTQueryExpression){
			ASTQueryExpression expr3 = (ASTQueryExpression) expr; return expr3.accept(this);}
		else if (expr instanceof ASTUnaryExpression){
			ASTUnaryExpression expr4 = (ASTUnaryExpression) expr; return expr4.accept(this);}
		else if (expr instanceof ASTOperationExpression){
			ASTOperationExpression expr5 = (ASTOperationExpression) expr; expr5.accept(this); return null;}
		else
			throw new TranslatorException("unsupported return value: "+expr);			
	}

	public List<Node> visit(ASTOperationExpression e){
		List<Node> n;
		if (e.IsAttrOperation()){
			if (e.source()!=null){
				if (e.source() instanceof ASTOperationExpression){
					ASTOperationExpression astoe = (ASTOperationExpression) e.source();
					n = astoe.accept(this);
					/* set context */
					//System.out.println("ASTOperationExpression:"+e.operator());
					e.context = astoe.context;
					List<Node> dn;
					//if (!astoe.getRef())
					System.out.println("operator:"+e.operator());
					dn = getDataNodes(n,e.operator());
					/*else
						dn = getDataNodes(getAllNodes
							(modelTranslator.getRefType(astoe.operator().getText())), 
							e.operator());*/
					if (dn.size()==0){
						//System.out.println("operator:"+e.operator().getText());
						edges = factory.getEdgesByName(e.operator().getText());
						if (edges==null)
							throw new TranslatorException("Error: No such <"+e.operator().getText()
								+ "> attribute or navgation available.");
						else{
							System.out.println("Edges found...");
							e.setRef();
							edge_set=true;
							e.context = new ASTContext(modelTranslator.getRefType(
														e.operator().getText()),
														e.operator().getText());
							return getAllNodes(modelTranslator.getRefType(e.operator().getText()));
						}
					}
					return dn;
				}
				else
					throw new TranslatorException("unsupported translation on "+e.source());
			}
			else if (e.IsSelfOp()){ /* self.xxx */
				e.context = new ASTContext(currentContext.name());
				return getAllNodes(currentContext.name());
			}
			else{ /* object specified ClassA.xxx */
				if (vartable!=null){
					String t = vartable.get(e.operator().getText().hashCode());
					for (Integer i : vartable.keySet())
						System.out.println(i+"->"+vartable.get(i));
					e.context = new ASTContext(t);
					return getAllNodes(t);
				}
				else{
					e.context = new ASTContext(e.operator().getText());
					return getAllNodes(e.operator().getText());
				}
			}
		}
		else{
			throw new TranslatorException("unsupported translation: not an attribute operation [ "
				+e.source()+", "+e.operator()+" ]");
		}
		
		//return null;		
	}

	public List<Expression> visit(ASTQueryExpression e){
		System.out.println("visiting ASTQueryExpression...");
		List<Expression> ranges=null;
		ASTElemVarsDeclaration vars = e.vars();
		String type;
		List<Expression> exprs =null;
		List<Expression> reexpr=new ArrayList<Expression>();
		List<Expression> tmp = new ArrayList<Expression>();
		String scope;

		if (e.range()!=null){
			if (e.range() instanceof ASTAllInstancesExpression ){
				/* we know this is allInstances() */
				ASTAllInstancesExpression expr1 = (ASTAllInstancesExpression) e.range();
				ranges = expr1.accept(this);
				scope = expr1.token();
			if (ranges.size()==0)
				throw new TranslatorException
					("error allInstances() operations:<"+scope+">'s bound has to be greater than 0.");
			}
			else if (e.range() instanceof ASTOperationExpression){
				ASTOperationExpression opexpr = (ASTOperationExpression) e.range();
				opexpr.accept(this);
				scope = "";
				System.out.println("processing ASTOperationExpression...");
			}
			else
				throw new TranslatorException("unsupported range operation:"+e.range());
		}
		else{
			throw new TranslatorException("unsupported operation, range is not specified.");
		}
		
		if (scope.compareTo("self")==0)
			throw new TranslatorException("Can not use self here, object type must be specified.");
		
		/* get variables and their types */
		if (vars!=null){
			if (vartable==null) vartable = new HashMap<Integer,String>();
			List<Token> list =vars.idlist();
			if (vars.type()!=null){
				ASTSimpleType t = (ASTSimpleType) vars.type();
				type = new String(t.type());
			}
			else{
				type = new String(currentContext.name());
			}

			/*if (type.compareTo(scope)!=0) throw new TranslatorException("<"+type +"> does not match <"+scope+">");*/

			for (int i=0;i<list.size();i++){
				//System.out.println(list.get(i).getText()+" : "+type);
				vartable.put(list.get(i).getText().hashCode(),type);
			}
		}
		else{
			throw new TranslatorException("unsupported operation, no variables are specified.");
		}
		
		if (e.expr() instanceof ASTBinaryExpression){
			ASTBinaryExpression expr2 = (ASTBinaryExpression) e.expr();
			//expr2.context = new ASTContext(currentContext.name());
			exprs = expr2.accept(this);
		}
		else if (e.expr() instanceof ASTQueryExpression){
			ASTQueryExpression expr3 = (ASTQueryExpression) e.expr();
			exprs = expr3.accept(this);
		}
		else if (e.expr() instanceof ASTOperationExpression){
			ASTOperationExpression expr4= (ASTOperationExpression) e.expr();
			List<Node> nodes= expr4.accept(this);
			exprs = new ArrayList<Expression>();
			for (int i=0;i<nodes.size();i++)
					exprs.add(modelTranslator.getData().get(nodes.get(i)));
		}

		/* support forAll and exists operations on allInstances() */
		if (e.IsExists() && exprs!=null){
			tmp.clear();
			for (int i=0,b=0;i<exprs.size();i++){
				tmp.add(exprs.get(i));b++;
				if (b>=ranges.size()){
					reexpr.add(FOFormula.SomeExprs(tmp));
					tmp.clear();
					b=0;
				}
			}
			vartable = null;
			return reexpr;
		}
		else if (e.IsForAll() && exprs!=null){
			tmp.clear();
			for (int i=0,b=0;i<exprs.size();i++){
				tmp.add(exprs.get(i));b++;
				if (b>=ranges.size()){
					reexpr.add(FOFormula.AllExprs(tmp));
					tmp.clear();
					b=0;
				}
			}
			vartable = null;
			
			return reexpr;
		}
		else if (e.IsSelect() && exprs!=null){
			tmp.clear();
			//System.out.println("range:"+e.range());
			//System.out.println("exprs:"+exprs);
			/* partial solving should be completed by now. */
			/* set partial solutions now */
			
			throw new TranslatorException("Select is unsupported.");
		}
		else if (e.IsUnique() && exprs!=null){ // This is pretty cool we can use isUnique operator.
			if (exprs.get(0).isVar()){
				Var fvars[] = new Var[exprs.size()];
				for (int i=0;i<exprs.size();i++)
					fvars[i]=(Var)exprs.get(i);
				reexpr.add(FOFormula.Unique(fvars));
			}
			else{
				throw new TranslatorException("isUnique only supports attributes.");
			}

			/*if (e.range().context.hasRef()){
				//partial solving...	
			}*/

			return reexpr;

		}
		else{
			throw new TranslatorException("unsupported operations: "+e.operator().getText());
		}

		//throw new TranslatorException("unsupported operations.");
		
	}

	public Expression visit(ASTRealLiteral e){
		System.out.println("visiting ASTRealLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTStringLiteral e){
		System.out.println("visiting ASTStringLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTTupleLiteral e){
		System.out.println("visiting ASTTupleLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public List<Expression> visit(ASTTypeArgExpression e){
		System.out.println("visiting ASTTypeArgExpression...");
		throw new TranslatorException("unsupported operations.");
	}

	public List<Expression> visit(ASTIterateExpression e){
		System.out.println("visiting ASTIterateExpression...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTUndefinedLiteral e){
		System.out.println("visiting ASTUndefinedLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public List<Expression> visit(ASTUnaryExpression e){
		ASTExpression expr = e.expr();
		List<Expression> exprs=null;
		List<Expression> reexprs = new ArrayList<Expression>();

		if (expr instanceof ASTQueryExpression){
			ASTQueryExpression expr1 = (ASTQueryExpression) expr;
			exprs = expr1.accept(this);
		}
		else if (expr instanceof ASTBinaryExpression){
			ASTBinaryExpression expr2 = (ASTBinaryExpression) expr;
			exprs = expr2.accept(this);
		}
		else{
			throw new TranslatorException("unsupported expression:"+e.toString());			
		}

		if (e.IsNot()){
			NegFun negs[] = FOFormula.Negation(exprs);
			if (negs.length>1)
				reexprs.add(FOFormula.AllExprs(negs));
			else
				reexprs.add(negs[0]);

			return reexprs;
		}
			
		throw new TranslatorException("unsupported operations<"+e.operator()+">");
	
	}

	

	public List<Expression> visit(ASTAllInstancesExpression e){
		System.out.println("visiting allInstanceExpression...");
		
		if (e.token().compareTo("self")==0)
			return getAllNodesVars(currentContext.name());
		else
			return getAllNodesVars(e.token());

		//throw new TranslatorException("unsupported operations.");
	}

	public List<Expression> visit(ASTBinaryExpression e){
		String operator = e.getToken().getText();
		ASTExpression left = e.left();
		ASTExpression right = e.right();
		List<Expression> exprleft = new ArrayList<Expression>();
		List<Expression> exprright = new ArrayList<Expression>();	
		List<Expression> exprs = new ArrayList<Expression>();
		List<Node> nodes1=null;
		List<Node> nodes2=null;
		edge_set = false;
		left_edge=false;
		right_edge=false;

		if (left instanceof ASTBinaryExpression){
			ASTBinaryExpression beleft = (ASTBinaryExpression) left;
			exprleft = beleft.accept(this);
			left.context = beleft.context;
		}
		else if (left instanceof ASTOperationExpression){
			ASTOperationExpression oeleft = (ASTOperationExpression) left;
			nodes1 = oeleft.accept(this);
			//System.out.println("left expression:");
			if (!edge_set){
				left_edge=false;
				for (int i=0;i<nodes1.size();i++)
					exprleft.add(modelTranslator.getData().get(nodes1.get(i)));
			}
			else{
				left_edge=true;
				//for (Edge edge : edges)
				//	exprleft.add(modelTranslator.getVar(edge));
				if (nodes1.get(0) instanceof DataNode){
					for (int i=0;i<nodes1.size();i++)
						exprleft.add(modelTranslator.getData().get(nodes1.get(i)));
				}
			}
			
				
			left.context = oeleft.context;
		}	
		else if (left instanceof ASTIntegerLiteral){
			ASTIntegerLiteral illeft = (ASTIntegerLiteral) left;
			//System.out.println("Integer Expression:"+illeft.accept(this));
			exprleft.add(illeft.accept(this));
			left.context = illeft.context;
		}
		else{
			throw new TranslatorException("unsupported AST(left):"+left);
		}

		System.out.println("operator:"+operator);
		edge_set=false;
		if (right instanceof ASTBinaryExpression){
			ASTBinaryExpression beright = (ASTBinaryExpression) right;
			exprright = beright.accept(this);
			right.context = beright.context;
		}
		else if (right instanceof ASTOperationExpression){
			ASTOperationExpression oeright = (ASTOperationExpression) right;
			nodes2 = oeright.accept(this);	
			//System.out.println("right expression:");
			if (!edge_set){
				for (int i=0;i<nodes2.size();i++)
					exprright.add(modelTranslator.getData().get(nodes2.get(i)));
			}
			else{
				right_edge=true;
			}
			right.context = oeright.context;
		}
		else if (right instanceof ASTIntegerLiteral){
			ASTIntegerLiteral ilright = (ASTIntegerLiteral) right;
			//System.out.println("Integer Expression:"+ilright.accept(this));
			exprright.add(ilright.accept(this));
			right.context = ilright.context;
		}
		else if (right instanceof ASTEnumLiteral){
			ASTEnumLiteral elright = (ASTEnumLiteral) right;
			exprright.add(elright.accept(this));
			right.context = elright.context;
		}
		else{
			throw new TranslatorException("unsupported AST(right):"+right);
		}

		Connective conn = getLogicOperator(e.getToken());
		Arithmetic arith = getArithmeticOperator(e.getToken());

		//if (conn==null && arith==null) throw new TranslatorException("unknown operator:<"+e.getToken().getText()+">.");

		if (!(right instanceof ASTOperationExpression) && 
			left instanceof ASTOperationExpression) {e.context = right.context = left.context;}
		if (!(left instanceof ASTOperationExpression) && 
			right instanceof ASTOperationExpression) {e.context = left.context = right.context;}

		System.out.println("left:"+left.context);
		System.out.println("right:"+right.context);
		
		if (conn!=null){
			if (left.context==null || right.context==null){
				return crossProduct(conn,exprleft,exprright);
			}	
			else if (!left.context.compareTo(right.context) || exprleft.size()!=exprright.size()){
				return crossProduct(conn,exprleft,exprright);
			}
			else{
				if (exprleft.size()!=exprright.size())
					throw new TranslatorException("unexpected error: size of the two sub expressions are not the same.");
	
				for (int i=0;i<exprleft.size();i++)
					exprs.add(new BinaryExpression(conn,exprleft.get(i),exprright.get(i)));
			}
		}

		//if (arith==null) throw new TranslatorException("unsupported operation:"+e.getToken().getText());

		if (arith!=null && !edge_set){
			if (left.context==null || right.context==null){
				return crossProduct(arith,exprleft,exprright);
			}
			if (!right.context.compareTo(left.context) || exprleft.size()!=exprright.size()){
				return crossProduct(arith,exprleft,exprright);
			}
			else{
				if (exprright.size()!=exprleft.size()){
					throw new TranslatorException("unexpected error: size of the two sub expressions are not the same.");
				}
				for (int i=0;i<exprright.size();i++)
					exprs.add(new ArithmeticExpression(arith,exprleft.get(i),exprright.get(i)));
			}
		}
		else{
		/* this must be the case: edge_set is set */
		/* nasty code block ... we need a more clean version of OCL AST. */
		/* suppose we are getting the same nodes */
			if (IsNotEqual(e.getToken().getText())){
				if (nodes1==null && nodes2==null) throw new TranslatorException("Error: Cannot navigate to the other object.");
				if (nodes1.get(0) instanceof GraphNode && nodes2.get(0) instanceof GraphNode){
					for (Edge edge : edges){
						if (edge.source()==edge.dest())
							exprs.add(new NegFun(modelTranslator.getVar(edge)));
					}
				}
				if (nodes1.get(0) instanceof DataNode  && nodes2.get(0) instanceof DataNode){
					DataNode source = (DataNode) nodes2.get(0);
					for (Edge edge : edges){
						GraphNode n1 = (GraphNode) edge.source();
						DataNode d1 = getDataNode(n1,source.getAttrName());
						GraphNode n2 = (GraphNode) edge.dest();
						DataNode d2 = getDataNode(n2,source.getAttrName());
						//System.out.println(d1+" <-> "+d2);
						BinaryExpression b = new BinaryExpression(
							Connective.EQUAL,
							modelTranslator.getData().get(d1),modelTranslator.getData().get(d2));
						exprs.add(new BinaryExpression(
								Connective.IMPLIES,modelTranslator.getVar(edge),new NegFun(b)));
					}
				}
			}
			edge_set=false;	
			left_edge=false;
			right_edge=false;
		}
		
		//throw new TranslatorException("unsupported operations.");		
		/*for (int i=0;i<exprs.size();i++)
			System.out.println(exprs.get(i));*/
		edges=null;
		return exprs;
	}

	/* 
     * NOTE: this is tricky, we have to detect size of each list then decide which way to go. 
     * So order does matter in this case which is using quantifiers (espeically, a chain of quantifiers is applied). 
     *
     */
	private List<Expression> crossProduct(Connective conn, List<Expression> exprleft, List<Expression> exprright){
		List<Expression> result = new ArrayList<Expression>();			
		if (exprleft.size() < exprleft.size()){
			for (int i=0;i<exprright.size();i++)
				for (int j=0;j<exprleft.size();j++)
					result.add(new BinaryExpression(conn,exprleft.get(i),exprright.get(j)));
		}
		else{
			for (int i=0;i<exprright.size();i++)
				for (int j=0;j<exprleft.size();j++)
					result.add(new BinaryExpression(conn,exprleft.get(j),exprright.get(i)));
		}

		return result;
	}

	private List<Expression> crossProduct(Arithmetic arith, List<Expression> exprleft, List<Expression> exprright){
		List<Expression> result = new ArrayList<Expression>();
		if (exprleft.size() < exprright.size()){
			for (int i=0;i<exprleft.size();i++)
				for (int j=0;j<exprright.size();j++)
					result.add(new ArithmeticExpression(arith,exprleft.get(i),exprright.get(j)));
		}
		else{
			for (int i=0;i<exprright.size();i++)
				for (int j=0;j<exprleft.size();j++)
					result.add(new ArithmeticExpression(arith,exprleft.get(j),exprright.get(i)));
		}

		return result;
	}

 	public Expression visit(ASTBooleanLiteral e){
		//System.out.println("visiting ASTBooleanLiteral...");
		//throw new TranslatorException("unsupported operations.");
		return new BoolLiteral(e.value());
	}

	public Expression visit(ASTCollectionLiteral e){
		System.out.println("visiting ASTCollectionLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTEmptyCollectionLiteral e){
		System.out.println("visiting EmptyCollectionLiteral...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTEnumLiteral e){
		//System.out.println("visiting ASTEnumLiteral...");
		//throw new TranslatorException("unsupported operations.");
		String type = e.getType();
		String literal = e.getLiteral();
		int index = modelTranslator.getEnumLiteralIndex(type,literal);
		if (index!=-1)
			return new NumLiteral(index);
		else
			throw new TranslatorException("error: Enum <"+type+"> does not have literal <"+literal+">");
	}
	
	public List<Expression> visit(ASTIfExpression e){
		System.out.println("visiting ASTIfExpression...");
		throw new TranslatorException("unsupported operations.");
	}

	public Expression visit(ASTIntegerLiteral e){
		//System.out.println(e.value());
		//throw new TranslatorException("unsupported operations.");
		if (e.value()==-1)
			System.out.println("found...");
		return new NumLiteral(e.value());
	}

	public List<Expression> visit(ASTLetExpression e){
		System.out.println("visiting ASTLetExpression...");
		throw new TranslatorException("unsupported operations.");
	}	

	private List<Node> getDataNodes(List<Node> nodes, Token attrname){
		HashMap<GraphNode, TreeSet<Edge>> ena = factory.getENA();	
		List<Node> data = new ArrayList<Node>();
		List<Expression> exprs = new ArrayList<Expression>();

		for (int i=0;i<nodes.size();i++){
			GraphNode node = (GraphNode)nodes.get(i);
			TreeSet<Edge> edges = ena.get(node);
			if (edges==null)
				break;
				/*throw new TranslatorException("Error: No such <"+attrname.getText()
					+ "> exists in <"+node.getType()+">.");*/
			for (Edge e : edges){
				if (e.getName().compareTo(attrname.getText())==0)
					data.add(e.dest());
			}
		}
		
		return data;
	}

	private DataNode getDataNode(GraphNode node, String attr){
		TreeSet<Edge> attrs = factory.getDataNodes(node);
		for (Edge e : attrs){
			if (e.getName().compareTo(attr)==0){
				return (DataNode) e.dest();
			}
		}
		return null;
	}


	private List<Node> getNodes(String context){
		HashMap<EClass,TreeSet<GraphNode>> bvg = factory.getBVG();
		TreeSet<GraphNode> s = null;
		nodes.clear();
		
		if (bvg==null)		
			throw new TranslatorException("fatal error: VG is not defined.");

		for (EClass cls : bvg.keySet()){
			if (cls.getName().compareTo(context)==0)
				s = bvg.get(cls);
		}

		if (s==null)
			throw new TranslatorException("No such <"+context+"> found.");
		
		for (GraphNode n : s) nodes.add(n);

		return nodes;
	}

	private List<Node> getAllNodes(String context){
		List<GraphNode> list = modelTranslator.getAllChildren(context);
		nodes.clear();
		for (GraphNode n : list) nodes.add(n);
		return nodes;
	}

	private List<Expression> getNodesVars(List<Node> list){
		if (list==null) throw new TranslatorException("List cannot be null.");
		if (list.size()==0) throw new TranslatorException("Every class seems to be abstract!!! List cannot be empty.");

		List<Expression> exprs = new ArrayList<Expression>();
			
		for (int i=0;i<list.size();i++)
			exprs.add(modelTranslator.getObjects().get(list.get(i)));

		return exprs;
	}

	private List<Expression> getAllNodesVars(String context){
		List<Node> list = getAllNodes(context);
		return getNodesVars(list);
	}

	private Connective getLogicOperator(Token token){
		String operator = token.getText();
		if (operator.compareTo(Connective.AND.toString())==0)
			return Connective.AND;
		else if (operator.compareTo(Connective.OR.toString())==0)
			return Connective.OR;
		else if (operator.compareTo(Connective.XOR.toString())==0)
			return Connective.XOR;
		else if (operator.compareTo(Connective.IMPLIES.toString())==0)
			return Connective.IMPLIES;
		else if (operator.compareTo(Connective.NOT.toString())==0)
			return Connective.NOT;
		else 
			//return null;
			throw new TranslatorException("Error: unknown boolean operator ["+operator+"]");
	}

	private Arithmetic getArithmeticOperator(Token token){
		String operator = token.getText();
		if (operator.compareTo(Arithmetic.GREATER.toString())==0)
			return Arithmetic.GREATER;
		else if (operator.compareTo(Arithmetic.PLUS.toString())==0)
			return Arithmetic.PLUS;
		else if (operator.compareTo(Arithmetic.MINUS.toString())==0)
			return Arithmetic.MINUS;
		else if (operator.compareTo(Arithmetic.MUL.toString())==0)
			return Arithmetic.MUL;
		else if (operator.compareTo(Arithmetic.DIV.toString())==0)
			return Arithmetic.DIV;
		else if (operator.compareTo(Arithmetic.LESS.toString())==0)
			return Arithmetic.LESS;
		else if (operator.compareTo(Arithmetic.GREATER_EQUAL.toString())==0)
			return Arithmetic.GREATER_EQUAL;
		else if (operator.compareTo(Arithmetic.LESS_EQUAL.toString())==0)
			return Arithmetic.LESS_EQUAL;
		else if (operator.compareTo(Arithmetic.EQUAL.toString())==0)
			return Arithmetic.EQUAL;
		else
			//return null;
			throw new TranslatorException("Error: unknown arithmetic operator ["+operator+"]");
	}

	private boolean IsNotEqual(String operator){
		return "<>".compareTo(operator)==0;
	}

	private GraphNode getDataNodeContainer(Var key){
		HashMap<DataNode,Var> data=modelTranslator.getData();
		HashMap<Edge,Var> attrs=modelTranslator.getAttributes1();
		DataNode d=null;

		for (DataNode n : data.keySet()){
			Var v = data.get(n);
			if (v==key) {d=n;break;}
		}							
	
		if (d==null) return null;

		for (Edge e : attrs.keySet()){
			if (e.dest()==d)
				return (GraphNode)e.source();
		}
		
		/* it never reaches this statement if everything is correct. */
		return null;
	}

	public void visit(){
		PrintWriter err=new PrintWriter(System.err,true);
		try{
			ParseErrorHandler errh = new ParseErrorHandler(oclFile,err);
			lexer = new OCLLexer(new ANTLRFileStream(oclFile));
			CommonTokenStream token = new CommonTokenStream(lexer);
			parser = new OCLParser(token);
			lexer.init(errh);
			ASTOCLExpression astexpt = parser.oclexpression();
			if (errh.errorCount()>0)
				throw new TranslatorException("Parsing error(s) occur(s), translation is aborted.");
			else
				astexpt.accept(this);
			
		}					
		catch (RecognitionException e) {
           	 err.println(oclFile +":" + 
           	             e.line + ":" +
           	             e.charPositionInLine + ": " + 
           	             e.getMessage());
		}
		catch (IOException e){
			e.printStackTrace();
		}		

	}
	
	public FOFormula getFormula(){return oclfof;}

}


