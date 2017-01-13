package resourceManager;

import dataStructures.PCB;
import dataStructures.ReadyList;
import parser.Parser.Commands;

public class ResourceManager
{
	public PCB self; 
	public ReadyList ready_list; 
	
	public ResourceManager() {
		self = null;
		ready_list = new ReadyList();
		CreateInit();
	}
	
	public void CreateInit() {
		self = new PCB(new String[]{"cr","init","0"}, null);
		self.type = "running";
		ready_list.Insert(self);
	}
	
	public void Restart() {
		// must free all pointers to dynamic memory
		// in this example freeing whoever self references 
		self = null; 
		ready_list.Clear();
	}
	
	public void InvokeFunctionCall(String command) {
		String[] array = command.split(" ");
		
		if (NewLine(array[0])) {
			Restart();
			return; 
		}
		
		InvokeFunction(array);
		Scheduler();	
	}
	
	private void InvokeFunction(String[] array) {
		Commands command = Commands.valueOf(array[0].toUpperCase());
		switch(command) {
			case INIT:
				//System.out.println("creating init process ");
				CreateInit();
				//output_file_handler.Print("creating init process ");
				break;
			case CR:
				//System.out.println("creating process " + array[1] + " with priority " + array[2]);
				Create(array);
				//output_file_handler.Print("creating process " + array[1] + " with priority " + array[2]);
				break;
			case DE:
				//System.out.println("deleting process " + array[1]);
				//output_file_handler.Print("deleting process " + array[1]);
				break;
			case REQ:
				//System.out.println("requesting resource " + array[1] + " with units " + array[2]);
				//output_file_handler.Print("requesting resource " + array[1] + " with units " + array[2]);
				break;
			case REL:
				//System.out.println("releasing resource " + array[1] + " with units " + array[2]);
				//output_file_handler.Print("releasing resource " + array[1] + " with units " + array[2]);
				break;
			case TO:
				//System.out.println("timeout");
				TimeOut();
				//output_file_handler.Print("timeout");
				break;
		}
	}
	
	private boolean NewLine(String command) {
		if (command.equals("")) {
			//output_file_handler.Print("empty-line");
			return true;
		}
		else
			return false; 
	}
	
	public enum Commands {
		INIT,
		CR,
		DE,
		REQ,
		REL,
		TO
	}
	
	private void Create(String[] array) {
		PCB pcb = new PCB(array, self);
		// link PCB to creation tree
		ready_list.Insert(pcb);
		Scheduler();
	}
	
	private void TimeOut() {
		ready_list.MoveToTheEnd(self);
		self.type = "ready";
		
	}
	
	private void Scheduler() {
		PCB highest_priority = ready_list.GetHighestPriority();
		
		// self == null occurs when current process destroyed itself
		if (self.priority < highest_priority.priority || ! self.type.equals("running") 
				|| self == null) {
			Preempt(highest_priority);
		}
	}
	
	private void Preempt(PCB highest_priority) {
		self = highest_priority;
		self.type = "running";
	}
	
	public String GetReply() {
		// must check if self is null
		// for now self is null whenever an empty line is read 
		if (self != null)
			return self.pid + " ";
		else
			return "\n";
	}

}
