package Translate;

public abstract class Exp {
    public Exp(Exp e)
    {
    	exp = e;
    }
    private tree.Exp exp;
    public tree.Exp unEx()
    {
    	return exp;
    }
}
