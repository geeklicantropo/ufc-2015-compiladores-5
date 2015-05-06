import java.io.IOException;

import reader.ReadFile;

import syntaxtree.*;
import visitor.*;
import reader.*;

public class Main {
   
   public static void main(String [] args) throws IOException {
      try {
    	  String input = new ReadFile().getContent("t.txt");
    	  Program p = new MiniJavaParser(new java.io.StringReader(input)).Prog();
    	  System.out.println("Sintatic analysis successfull");
    	  p.accept(new PrettyPrintVisitor());
    	  System.out.println("Semantic analysis successfull");
    	  SymbolTable s = new SymbolTable();
    	  p.accept(s);
    	  System.out.println("Symbol Table created successfull");
    	  s.PrettyPrint();
    	  TypeChecking tc = new TypeChecking(s.getProgramTable());
    	  p.accept(tc);
      }
      catch (ParseException e) {
         System.out.println("Error : \n"+ e.toString());
      }
   }
}
