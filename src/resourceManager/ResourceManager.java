package resourceManager;

import java.util.LinkedList;
import dataStructures.PCB;
import dataStructures.ReadyList;
import parser.Parser.Commands;

public class ResourceManager
{
	public PCB self; 
	public ReadyList ready_list; 
	public LinkedList<PCB> created_processes;
	private boolean error = false; 
	
	public ResourceManager() {
		self = null;
		ready_list = new ReadyList();
		created_processes = new LinkedList<PCB>();
		CreateInit();
	}
	
	public void CreateInit() {
		self = new PCB(new String[]{"cr","init","0"}, null);
		self.type = "running";
		ready_list.Insert(self);
		created_processes.add(self);
	}
	
	public void Restart() {
		// must free all pointers to dynamic memory
		// in this example freeing whoever self references 
		self = null; 
		ready_list.Clear();
		created_processes.clear();
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
				Delete(array[1]);
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
		if (! PIDExists(array[1])) {
			PCB pcb = new PCB(array, self);
			created_processes.add(pcb);
			
			// link PCB to creation tree
			// i think we don't need this step since we figured out how to 
			// recursively delete from parent to children
			// and we figured out how to look up a parent
			
			ready_list.Insert(pcb);
			// adding new PCB to children list
			self.children.add(pcb);
			Scheduler();
		} else 
			error = true; 

	}
	
	private boolean PIDExists(String pid) {
		if (GetPCB(pid) != null)
			return true; 
		else
			return false; 
	}
	
	private PCB GetPCB ( String pid) {
		for (PCB pcb : created_processes) {
			if (pcb.pid.equals(pid))
				return pcb; 
		}
		
		return null; 
	}
	
	private void Delete (String pid) {
		// verify that if am an ancestor
		
		PCB target = GetPCB(pid);
		if (target == null) {
			// error whenever we try to delete a process that has not been created
			error = true; 
			return;
		}
			
		// verify if we are an ancestor
		if (CheckIfAncestor(target)) {
			KillTree(target);
		} else {
			// we cannot delete a process who is not a descendant of the currently running process "self"
			error = true;  
		}
		
	}
	
	private boolean CheckIfAncestor(PCB target) {
		// check if the currently running process is the target 
		if (self.pid.equals(target.pid)) {
			// since the currently running process will commit suicide 
			// we will set self to null in here
			self = null;
			return true; 
		}
		
		PCB parent = target.parent; 
		while (parent != null) {
			if (self.pid.equals(parent.pid))
				return true; 
			
			parent = parent.parent;
		}
		
		return false; 		
	}
	
	private void KillTree(PCB pcb) {
		RemoveMyself(pcb);
		
		// i need to recursively delete all my children 
		RemoveMyChildren(pcb);
	}
	
	private void RemoveMyself(PCB pcb) {
		// i need to go to my parent and remove from myself from his children list
		pcb.parent.children.remove(self);
		
		// ATTENTION
			// Must determine from which list i need to remove the pcb from 
			// For now I am assuming all of them are in the ready list
			// but in the future some of the children may be blocked in a resource list
			// and some can be in the ready list
			// must come up with a way to determine where they are located
		
		
		// i need to go to the ready list and remove myself from the list
		ready_list.RemovePCB(pcb);
		
		// remove from created_processes list 
		created_processes.remove(pcb);
	}
	
	private void RemoveMyChildren(PCB pcb) {
		for (PCB child: pcb.children)
			KillTree(child);
	}
	
	private void TimeOut() {
		ready_list.MoveToTheEnd(self);
		self.type = "ready";
		
	}
	
	private void Scheduler() {
		PCB highest_priority = ready_list.GetHighestPriority();
		
		// self == null occurs when current process destroyed itself
		if (self == null || self.priority < highest_priority.priority 
				|| ! self.type.equals("running") ) {
			Preempt(highest_priority);
		}
	}
	
	private void Preempt(PCB highest_priority) {
		self = highest_priority;
		self.type = "running";
	}
	
	
	// GetReply should only be called after processing a line
	// error gets reset every time GetReply() is called
	public String GetReply() {
		// must check if self is null
		// for now self is null whenever an empty line is read 
		
		if (error) {
			error = false; 
			return "error ";
		} else {
			if (self != null)
				return self.pid + " ";
			else
				return "\n";
		}
		
	}

}
