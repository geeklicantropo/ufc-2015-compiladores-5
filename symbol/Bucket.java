//Taken from book Modern Compiler Implementation in Java.
package symbol;

class Bucket {
	String key; 
	Object binding; 
	Bucket next;
	Bucket(String k, Object b, Bucket n) 
	{
		key=k; 
		binding=b; 
		next=n;
	}
} 
