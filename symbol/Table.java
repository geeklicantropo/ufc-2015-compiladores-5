//Taken from book Modern Compiler Implementation in Java.
package symbol;

public class Table 
{
	public Table();
	public void put(Symbol key, Object value);
	public Object get(Symbol key);
	public java.util.Enumeration keys();
} 
