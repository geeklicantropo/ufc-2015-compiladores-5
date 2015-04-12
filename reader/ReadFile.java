package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	
	public String getContent(String filename) throws IOException
	{
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		String strContent = "";
		
		while (br.ready())
			strContent += ( br.readLine() + "\n" );
		
		br.close();
		
		return strContent;
	}

}
