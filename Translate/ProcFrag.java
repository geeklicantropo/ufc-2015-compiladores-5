package Translate;

import tree.*;
import MipsFrame.*;

public class ProcFrag extends Frag
{
    private Stm body;
    private Frame frame;
    public ProcFrag (Stm b, Frame f) 
    {
	body = b;
	frame = f;
    }
}
