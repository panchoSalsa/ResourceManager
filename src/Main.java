import fileHandlers.InputFileHandler;
import fileHandlers.OutputFileHandler;
import parser.Parser;
import resourceManager.ResourceManager;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("**start**");
		
		InputFileHandler input_file_handler = new InputFileHandler("input.txt");
		OutputFileHandler output_file_handler = new OutputFileHandler("8926501.txt");
		
		ResourceManager resource_manager = new ResourceManager();
		// this line will print the first init process
		output_file_handler.Print(resource_manager.GetReply());
		
		Parser parser = new Parser();
		parser.SetOutputFileHandler(output_file_handler);
		
		while (! input_file_handler.end_of_file) {
			// driver ...
			resource_manager.InvokeFunctionCall(input_file_handler.current_line);
			output_file_handler.Print(resource_manager.GetReply());
			input_file_handler.GetNextLine();
		}
		
		output_file_handler.Close();
		System.out.println("**end**");
	}
}
