package symbol;

public class TypeChecking
{
	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {
		Type t = n.t.accept(this);
		String id = n.i.toString();
		if (currMethod == null) 
		{
			if (!currClass.addVar(id,t))
			error.complain(id + "is already defined in " + currClass.getId()); 
		} 
		else if (!currMethod.addVar(id,t))
			error.complain(id + "is already defined in "+ currClass.getId() + "." + currMethod.getId());
	} 
}
