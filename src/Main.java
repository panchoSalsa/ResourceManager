import fileHandlers.InputFileHandler;
import fileHandlers.OutputFileHandler;
import parser.Parser;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("**start**");
		
		InputFileHandler input_file_handler = new InputFileHandler("input.txt");
		OutputFileHandler output_file_handler = new OutputFileHandler("8926501.txt");
		
		output_file_handler.ShowOutputFileName();
		
		Parser parser = new Parser();
		parser.SetOutputFileHandler(output_file_handler);
		
		while (! input_file_handler.end_of_file) {
			// driver ...
			parser.LineToParse(input_file_handler.current_line);
			parser.ParseLine();
			//parser.DisplayCurrentParsingLine();
			input_file_handler.GetNextLine();
		}
		
		output_file_handler.Close();
		
		System.out.println("**end**");
	}
}
