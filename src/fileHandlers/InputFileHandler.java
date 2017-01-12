package fileHandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputFileHandler
{
	private String file_name;
	private Scanner scanner; 
	public String current_line;
	public boolean end_of_file; 
	
	public InputFileHandler(String file_name) {
		this.file_name = file_name;
		InitializeScanner();
		GetNextLine();
	}
	
	
	
	public void GetNextLine() {
			// condition to prevent if (scanner.hasNext()) to be executed when scanner is closed
			// this function will never get executed if while loop condition is !end_of_file
			
			if (end_of_file) 
				return; 
			
			else {
				if (scanner.hasNext())
					current_line = scanner.nextLine();
				else {
					scanner.close();
					end_of_file = true; 
				}
			}
	}
	
	public void ReadFileToConsole() {
		System.out.println("Printing file initiated ...");
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			System.out.println(line);
		}
		scanner.close();
		System.out.println("Printing file complete");
	}
	
	public void InitializeScanner() {
		try
		{
			this.scanner = new Scanner(new File(file_name));
			end_of_file = false; 
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			end_of_file = true; 
		}
	}
	
	public void ShowInputFileName() {
		System.out.println("input file_name is: " + this.file_name);
	}
}
