package parser.ocl.visitor;

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

//import parser.ocl.ASTPropertyCallExpression;

public abstract class PrintVisitor{

	public abstract void visit(ASTAllInstancesExpression e);
	public abstract void visit(ASTBinaryExpression e);
	public abstract void visit(ASTBooleanLiteral e);
	public abstract void visit(ASTCollectionLiteral e);
	public abstract void visit(ASTEmptyCollectionLiteral e);
	public abstract void visit(ASTEnumLiteral e);
	public abstract void visit(ASTIfExpression e);
	public abstract void visit(ASTIntegerLiteral e);
	public abstract void visit(ASTLetExpression e);
	public abstract void visit(ASTOperationExpression e);
	public abstract void visit(ASTQueryExpression e);
	public abstract void visit(ASTRealLiteral e);
	public abstract void visit(ASTStringLiteral e);
	public abstract void visit(ASTTupleLiteral e);
	public abstract void visit(ASTTypeArgExpression e);
	public abstract void visit(ASTUnaryExpression e);
	public abstract void visit(ASTConstraintDefinition e);
	public abstract void visit(ASTInvariantClause e);
	public abstract void visit(ASTIterateExpression e);
	public abstract void visit(ASTOCLExpression e);
	public abstract void visit(ASTUndefinedLiteral e);
		//abstract void visitASTPropertyCallExpression (ASTPropertyCallExpression e);
}
