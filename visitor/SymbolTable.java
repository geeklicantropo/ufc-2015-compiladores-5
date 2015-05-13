package visitor;

import java.util.*;
import syntaxtree.*;
import symbol.*;

public class SymbolTable implements Visitor
{

	private ClassTable currentClass;
	private MethodTable currentMethod;
	private ProgramTable pt;
	private ErrorMsg error;
	
	public boolean hasError()
	{
		return error.hasError();
	}
	public ProgramTable getProgramTable()
	{
		return pt; 
	}
	public SymbolTable()
	{
		error = new ErrorMsg();
		pt = new ProgramTable();
	}
	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		error = new ErrorMsg();
		n.m.accept(this);
		for ( int i = 0; i < n.cl.size(); i++ ) {
			n.cl.elementAt(i).accept(this);
		}
	}

	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
		n.i1.accept(this);
		ClassTable c = new ClassTable(n.i1.toString());
		if (!pt.addClass(Symbol.symbol(n.i1.toString()),c))
			error.complain("Class "+n.i1.toString()+" already defined.");
		else
			currentClass = c;
		n.i2.accept(this);
		MethodTable m = new MethodTable("main",null);
		if (!currentClass.addMethod(Symbol.symbol("main"),m))
			error.complain("Method main in class "+n.i1.toString()+" already defined.");
		else
		{
			currentMethod = m;
			currentMethod.addParam(Symbol.symbol(n.i2.toString()),null);
		}
		n.s.accept(this);
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {
		n.i.accept(this);
		ClassTable c = new ClassTable(n.i.toString());
		if (!pt.addClass(Symbol.symbol(n.i.toString()),c))
			error.complain("Class "+n.i.toString()+" already defined.");
		else
			currentClass = c;
		currentMethod = null;
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.elementAt(i).accept(this);
		}
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
		n.i.accept(this);
		ClassTable c = new ClassTable(n.i.toString());
		if (!pt.addClass(Symbol.symbol(n.i.toString()),c))
			error.complain("Class "+n.i.toString()+" already defined.");
		else
			currentClass = c;
		n.j.accept(this);
		currentMethod = null;
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.elementAt(i).accept(this);
		}
	}

	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {
		/*n.t.accept(this);
		n.i.accept(this);*/
		n.t.accept(this);
		String id = n.i.toString();
		if (currentMethod == null) 
		{
			if (!currentClass.addVar(Symbol.symbol(id),n.t))
				error.complain(id + "is already defined in " + currentClass.toString()); 
		} 
		else if (!currentMethod.addVar(Symbol.symbol(id),n.t))
			error.complain(id + "is already defined in "+ currentClass.toString() + "." + currentMethod.toString());
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public void visit(MethodDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		MethodTable m = new MethodTable(n.i.toString(),n.t);
		if (!currentClass.addMethod(Symbol.symbol(n.i.toString()),m))
			error.complain("Method "+n.i.toString()+" in class "+n.i.toString()+" already defined.");
		else
			currentMethod = m;
		for ( int i = 0; i < n.fl.size(); i++ ) {
			n.fl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
	}

	// Type t;
	// Identifier i;
	public void visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		if (!currentMethod.addParam(Symbol.symbol(n.i.toString()),n.t))
			error.complain("Param "+n.i.toString()+" already defined in Method "+currentMethod.toString());
	}

	public void visit(IntArrayType n) {
	}

	public void visit(BooleanType n) {
	}

	public void visit(IntegerType n) {
	}

	// String s;
	public void visit(IdentifierType n) {
	}

	// StatementList sl;
	public void visit(Block n) {
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.elementAt(i).accept(this);
		}
	}

	// Exp e;
	// Statement s1,s2;
	public void visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
	}

	// Exp e;
	// Statement s;
	public void visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
	}

	// Exp e;
	public void visit(Print n) {
		n.e.accept(this);
	}

	// Identifier i;
	// Exp e;
	public void visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
	}

	// Identifier i;
	// Exp e1,e2;
	public void visit(ArrayAssign n) {
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e1,e2;
	public void visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	// Exp e;
	public void visit(ArrayLength n) {
		n.e.accept(this);
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public void visit(Call n) {
		n.e.accept(this);
		n.i.accept(this);
		for ( int i = 0; i < n.el.size(); i++ ) {
			n.el.elementAt(i).accept(this);
		}
	}

	// int i;
	public void visit(IntegerLiteral n) {
	}

	public void visit(True n) {
	}

	public void visit(False n) {
	}

	// String s;
	public void visit(IdentifierExp n) {
	}

	public void visit(This n) {
	}

	// Exp e;
	public void visit(NewArray n) {
	n.e.accept(this);
	}

	// Identifier i;
	public void visit(NewObject n) {
	}

	// Exp e;
	public void visit(Not n) {
	n.e.accept(this);
	}

	// String s;
	public void visit(Identifier n) {
	}
	
	public void PrettyPrint()
	{
		pt.PrettyPrint();
	}
	
}
