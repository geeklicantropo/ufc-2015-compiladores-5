import java.io.IOException;

import reader.ReadFile;

public class Main {
   
   public static void main(String [] args) throws IOException {
      try {
    	  String input = new ReadFile().getContent("src/t.txt");
    	  new MiniJavaParser(new java.io.StringReader(input)).Goal();
    	  System.out.println("Sintatic analysis successfull");
      }
      catch (ParseException e) {
         System.out.println("Error : \n"+ e.toString());
      }
   }
}
