/**
 *	A return visitor that returns different types of objects from OCL AST Trees
 *	Written by: Hao Wu
 *  JULY-2012
 *  bugs report to: haowu@cs.nuim.ie	
 */

package parser.ocl.visitor;
import java.util.*;

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
import parser.ocl.ASTConstraintDefinition;
import parser.ocl.ASTInvariantClause;
import parser.ocl.ASTIterateExpression;
import parser.ocl.ASTOCLExpression;
import parser.ocl.ASTUndefinedLiteral;
import parser.ocl.ASTEnumLiteral;
import atongmu.ast.Expression;
import atongmu.atg.Node;

public abstract class ReturnVisitor<E, LN, LE, V>{

	public abstract LE visit(ASTAllInstancesExpression e);
	public abstract LE visit(ASTBinaryExpression e);
	public abstract LE visit(ASTIfExpression e);
	public abstract LE visit(ASTLetExpression e);
	public abstract LE visit(ASTQueryExpression e);
	public abstract LE visit(ASTTypeArgExpression e);
	public abstract LE visit(ASTUnaryExpression e);
	public abstract LE visit(ASTIterateExpression e);
	public abstract LE visit(ASTInvariantClause e);
	public abstract E visit(ASTBooleanLiteral e);
	public abstract E visit(ASTCollectionLiteral e);
	public abstract E visit(ASTEmptyCollectionLiteral e);
	public abstract E visit(ASTEnumLiteral e);
	public abstract E visit(ASTIntegerLiteral e);
	public abstract E visit(ASTRealLiteral e);
	public abstract E visit(ASTStringLiteral e);
	public abstract E visit(ASTTupleLiteral e);
	public abstract E visit(ASTUndefinedLiteral e);

	public abstract LN visit(ASTOperationExpression e);

	public abstract V visit(ASTConstraintDefinition e);
	public abstract V visit(ASTOCLExpression e);

}
