package visitor;

import java.util.*;
import syntaxtree.*;
import symbol.*;

public class IRTree implements IRVisitor
{
	private Frame currentFrame;
	private Frag initial;
	private Frag frags;
	private ClassTable currentClass;
	private MethodTable currentMethod;
	private ProgramTable pt;
	
	public IRTree (ProgramTable pt, Frame f)
	{
		this.pt = pt;
		frags = new Frag(null);
		initial = frags;
		currentFrame = f;
	}
	
	public procEntryExit (Exp body)
	{
		ProcFrag f = new ProcFrag(body, frame);
		frags.addNext(f);
		frags = frags.getNext();
	}
	
	public Frag getResults()
	{
		return frags;
	}
	
	// MainClass m;
	// ClassDeclList cl;
	public Exp visit(Program n) {
		n.m.accept(this);
		for ( int i = 0; i < n.cl.size(); i++ ) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Exp visit(MainClass n) {
		n.i1.accept(this);
		currentClass = pt.getClass(Symbol.symbol(n.i1.toString()));
		n.i2.accept(this);
		Symbol sy = Symbol.symbol(n.i2.toString());
		currentMethod = currentClass.getMethod(sy);
		Frame mFrame = currentFrame(sy,new BoolList(false,null));
		currentFrame = mFrame;
		Exp se = n.s.accept(this);
		Exp retExp = new CONST(0);
		Exp body = new MOVE( new TEMP(currentFrame.RV()) , new ESEQ( se , retExp ) );
		procEntryExit(body);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Exp visit(ClassDeclSimple n) {
		n.i.accept(this);
		currentClass = pt.getClass(Symbol.symbol(n.i.toString()));
		currentMethod = null;
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Exp visit(ClassDeclExtends n) {
		n.i.accept(this);
		currentClass = pt.getClass(Symbol.symbol(n.i.toString()));
		n.j.accept(this);
		currentMethod = null;
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		for ( int i = 0; i < n.ml.size(); i++ ) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Exp visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Exp visit(MethodDecl n) {
		n.t.accept(this);
		currentMethod = currentClass.getMethod(Symbol.symbol(n.i.toString()));
		BoolList b = new BoolList(false, null);
		BoolList init = b;
		for ( int i = 0; i < n.fl.size(); i++ ) {
			n.fl.elementAt(i).accept(this);
			b.tail = new BoolList(false,null);
			b = b.tail;
		}
		b = init;
		Frame newf = currentFrame(Symbol.symbol(n.i.toString()),b);
		Frame oldf = currentFrame;
		currentFrame = newf;
		for ( int i = 0; i < n.vl.size(); i++ ) {
			n.vl.elementAt(i).accept(this);
		}
		Exp retExp = null;
		Exp body = null;
		if (!n.sl.size())
		{
			retExp = n.e.accept(this);
			body = new MOVE( new TEMP(currentFrame.RV()) , new ESEQ( null , retExp ) );
		}
		else
		{
			body = n.sl.elementAt(0).accept(this);
			for ( int i = 1; i < n.sl.size(); i++ ) {
				body = new SEQ(body, n.sl.elementAt(i).accept(this));
			}
			retExp = n.e.accept(this);
			body = new MOVE( new TEMP(currentFrame.RV()) , new ESEQ( body , retExp ) );
		}
		procEntryExit(body);
		return null;
	}

	// Type t;
	// Identifier i;
	public Exp visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	//NOTHING TO DO HERE!!!
	public Exp visit(IntArrayType n) {
		return null;
	}

	//NOTHING TO DO HERE!!!
	public Exp visit(BooleanType n) {
		return null;
	}

	//NOTHING TO DO HERE!!!
	public Exp visit(IntegerType n) {
		return null;
	}
	
	//NOTHING TO DO HERE!!!
	// String s;
	public Exp visit(IdentifierType n) {
		return null;
	}

	// StatementList sl;
	public Exp visit(Block n) {
		for ( int i = 0; i < n.sl.size(); i++ ) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Exp visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Exp visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Exp visit(Assign n) {
		n.i.accept();
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Exp visit(ArrayAssign n) {
		n.i.accept();
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Exp visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e;
	public Exp visit(ArrayLength n) {
		n.e.accept(this);
		return null;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Exp visit(Call n) {
		n.e.accept(this);
		ClassTable c = pt.getClass(Symbol.symbol(((IdentifierType) t).toString()));
		MethodTable m = c.getMethod(Symbol.symbol(n.i.toString()));
		for ( int i = 0; i < n.el.size(); i++ ) {
			Type tparam = n.el.elementAt(i).accept(this);
		}
		return null;
	}

	// int i;
	public Exp visit(IntegerLiteral n) {
		return null;
	}

	public Exp visit(True n) {
		return null;
	}

	public Exp visit(False n) {
		return null;
	}

	// String s;
	public Exp visit(IdentifierExp n) {
		return null;
	}

	public Exp visit(This n) {
		if (currentClass == null)
			error.complain("Class Environment not found");
		return new IdentifierType(currentClass.toString());
	}

	// Exp e;
	public Exp visit(NewArray n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	public Exp visit(NewObject n) {
		ClassTable c = pt.getClass(Symbol.symbol(n.i.toString()));
		if (c == null)
			error.complain("Class used in new object not found");
		return new IdentifierType(c.toString());
	}

	// Exp e;
	public Exp visit(Not n) {
		n.e.accept(this);
		return null;
	}

	// String s;
	public Exp visit(Identifier n) {
		return null;
	}
	
}
