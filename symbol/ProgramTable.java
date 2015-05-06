package symbol;

import java.util.LinkedHashMap;
import syntaxtree.*;

public class ProgramTable 
{
	private LinkedHashMap<Symbol, ClassTable> classTable;
	public ProgramTable()
	{
		classTable = new LinkedHashMap<Symbol, Type>();
	}
	public boolean addClass(Symbol s, ClassTable t)
	{
		if (!classTable.containsKey(s))
		{
			classTable.add(s,t);
			return true;
		}
		return false;
	}
	public void PrettyPrint()
	{
		Set<Symbol> keys = classTable.keySet();
		for (Symbol i : keys)
		{
			System.out.println("Class "+i.toString()+" {");
			classTable.get(i).PrettyPrint();
			System.out.println("}");
		}
	}
	public ClassTable getClass(Symbol s)
	{
		if (classTable.contains(s))
			return classTable.get(s);
		return null;
	}
} 
