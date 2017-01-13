package dataStructures;

import java.util.LinkedList;

public class PCB
{
	public String pid; 
	public String type; 
	// other resources list;
	public PCB parent; 
	public LinkedList<PCB> children;
	public int priority; 
	
	// parent is self, current running process. 
	public PCB (String[] parameters, PCB parent) {
		pid = parameters[1];
		this.parent = parent;
		type = "ready";
		children = new LinkedList<PCB>();
		InitializePriority(parameters[2]);
	}
	
	private void InitializePriority(String priority) {
		try {
			this.priority = Integer.parseInt(priority);
		} catch (NumberFormatException e) {
		      //Will Throw exception!
		      //do something! anything to handle the exception.
			System.out.println("priority string is not a number");
		}
	}
}
