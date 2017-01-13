package fileHandlers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class OutputFileHandler
{
	private String file_name;
	private PrintWriter writer; 
	
	public OutputFileHandler(String file_name) {
		this.file_name = file_name;
		InitializeWriter();
	}
	
	private void InitializeWriter() {
		try
		{
			writer = new PrintWriter(file_name, "UTF-8");
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Print(String string) {
		writer.print(string);
	}
	
	public void ShowOutputFileName() {
		System.out.println("output file_name is: " + this.file_name);
	}

	public void Close()
	{
		writer.close();
		
	}

}
