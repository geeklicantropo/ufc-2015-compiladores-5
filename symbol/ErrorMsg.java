package symbol;

//Taken from book
public class ErrorMsg
{
	private boolean anyErrors;
	public ErrorMsg()
	{
		anyErrors = false;
	}
	public void complain(String msg)
	{
		if (!anyErrors)
			System.out.println("Errors founded!");
		anyErrors = true;
		System.out.println(msg);
	}
}