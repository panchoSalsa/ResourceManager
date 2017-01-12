package parser;

import fileHandlers.OutputFileHandler;

public class Parser
{
	private String current_parsing_line;
	private OutputFileHandler output_file_handler; 
	
	public void LineToParse(String input) {
		current_parsing_line = input; 
	}
	
	public void SetOutputFileHandler(OutputFileHandler output_file_handler) {
		this.output_file_handler = output_file_handler;
	}
	
	public void ParseLine() {
		String[] array = current_parsing_line.split(" ");
		ParserControl(array);	
	}
	
	private boolean NewLine(String command) {
		if (command.equals("")) {
			//System.out.println("empty-line");
			output_file_handler.Print("empty-line");
			return true;
		}
		else
			return false; 
		
	}
	
	private void ParserControl(String[] array) {
		
		// must handle array[0] = ""
		// case for an empty line; 
		if (NewLine(array[0])) {
			return; 
		}
		
		
		Commands command = Commands.valueOf(array[0].toUpperCase());
		switch(command) {
			case INIT:
				//System.out.println("creating init process ");
				output_file_handler.Print("creating init process ");
				break;
			case CR:
				//System.out.println("creating process " + array[1] + " with priority " + array[2]);
				output_file_handler.Print("creating process " + array[1] + " with priority " + array[2]);
				break;
			case DE:
				//System.out.println("deleting process " + array[1]);
				output_file_handler.Print("deleting process " + array[1]);
				break;
			case REQ:
				//System.out.println("requesting resource " + array[1] + " with units " + array[2]);
				output_file_handler.Print("requesting resource " + array[1] + " with units " + array[2]);
				break;
			case REL:
				//System.out.println("releasing resource " + array[1] + " with units " + array[2]);
				output_file_handler.Print("releasing resource " + array[1] + " with units " + array[2]);
				break;
			case TO:
				//System.out.println("timeout");
				output_file_handler.Print("timeout");
				break; 
		}
	}
	
	public void DisplayCurrentParsingLine() {
		System.out.println(current_parsing_line);
	}
	
	public enum Commands {
		INIT,
		CR,
		DE,
		REQ,
		REL,
		TO
	}
}
