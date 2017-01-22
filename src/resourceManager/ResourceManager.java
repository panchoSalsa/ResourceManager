package resourceManager;

import java.util.LinkedList;
import dataStructures.PCB;
import dataStructures.PCBNode;
import dataStructures.RCB;
import dataStructures.RCBNode;
import dataStructures.ReadyList;
import parser.Parser.Commands;

public class ResourceManager
{
	public PCB self; 
	public ReadyList ready_list; 
	public LinkedList<PCB> created_processes;
	public LinkedList<RCB> rcb_list; 
	private boolean error = false; 
	
	public ResourceManager() {
		self = null;
		ready_list = new ReadyList();
		created_processes = new LinkedList<PCB>();
		InitializeRCBList();
		CreateInit();
	}
	
	public void InitializeRCBList() {
		rcb_list = new LinkedList<RCB>();
		
		for (int i = 1; i <= 4; ++i)
			rcb_list.add(new RCB(i));
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
		ResetRCBList();
	}
	
	private void ResetRCBList() {
		rcb_list.clear();
		InitializeRCBList();
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
				Request(array[1], Integer.parseInt(array[2]));
				//System.out.println("requesting resource " + array[1] + " with units " + array[2]);
				//output_file_handler.Print("requesting resource " + array[1] + " with units " + array[2]);
				break;
			case REL:
				Release(self, array[1], Integer.parseInt(array[2]));
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
		
		// check for init error
		if (DeletingInit(pid)) {
			error = true; 
			return;
		}
			
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
	
	private boolean DeletingInit(String pid) {
		return pid.equals("init");
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
		
		// restoring all resources the process is holding
		for (RCBNode node : pcb.other_resources ) {
			RCB rcb = node.GetRCB();
			
			// the process thats being deleted will free up all its resources
			// this will lead to unblocking some of its descendants 
			// but since the descendants will also be removed recursively
			// they will also free up any new resources they have gained
			ReleaseOnDelete(pcb, rcb, node.GetN());
		}
		
		// i need to go to the ready list and remove myself from the list
		ready_list.RemovePCB(pcb);
		
		// remove from created_processes list 
		created_processes.remove(pcb);
	}
	
	private void RemoveMyChildren(PCB pcb) {
		for (PCB child: pcb.children)
			KillTree(child);
	}
	
	private void Request(String rid, int n) {
		RCB rcb = GetRCB(rid);
		
		CheckForRequestErrors(rcb,n);
		
		if (error == true)
			return; 
		
		if (rcb.EnoughResources(n)) {
			
			rcb.AssignResources(n);
			
			AddToOtherResources(self,rcb,n);
				
		} else {
			self.type = "blocked";
			ready_list.RemovePCB(self);
			rcb.InsertIntoBlockList(self,n);
			
			// PCB will need to have pointer to blocked_list 
			self.blocked_list = rcb.blocked_list;
		}	
	}
	
	private void CheckForRequestErrors(RCB rcb, int n)
	{
		if (! rcb.ValidRequest(n))
			error = true; 
		
	}

	private void AddToOtherResources(PCB pcb, RCB rcb, int n)
	{
		for (RCBNode node : pcb.other_resources) {
			if (node.ContainsRCB(rcb)) {
				node.Increment(n);
				return;
			}
		}
		
		
		// if the RCBNode does not exist in other_resources list
		// then we have to create it 
		// we are requesting a resource for the first time
		pcb.other_resources.add(new RCBNode(rcb,n));
		
	}

	// this Release function only gets called when the current running process
	// releases a resource 
	private void Release(PCB pcb, String rid, int n) {				 
		RCB rcb = GetRCB(rid);
		
		CheckForReleaseErrors(rcb,n);
		
		if (error == true)
			return; 
		
		rcb.RestoreResources(n);
		pcb.UpdateOtherResources(rcb,n);
		
		while (! rcb.blocked_list.isEmpty() && rcb.CanIReleaseNext()) {
			RemoveFromBlockedList(rcb);
		}
		
	}
	
	private void CheckForReleaseErrors(RCB rcb, int n) {
		// checking if am releasing a resource i never held 
		if (! self.HasResource(rcb)) {
			error = true; 
			return; 
		}
		
		// checking if i am releasing more units than assigned
		if (! self.ValidRelease(rcb, n)) {
			error = true; 
			return; 
		}
	}
		
	// this ReleaseOnDelete function only gets called when the current running process
	// deletes itself and its children recursively
	private void ReleaseOnDelete(PCB pcb, RCB rcb, int n) {
		
		rcb.RestoreResources(n);
		pcb.UpdateOtherResources(rcb,n);
		
		while (! rcb.blocked_list.isEmpty() && rcb.CanIReleaseNext()) {
			RemoveFromBlockedList(rcb);
		}
		
	}
	
	
	
	
	private void RemoveFromBlockedList(RCB rcb)
	{
		PCBNode next = rcb.blocked_list.removeFirst();
		int n = next.GetRequestingAmount(); 
		
		rcb.AssignResources(n);
		
		PCB pcb = next.GetPCB();
		AddToOtherResources(pcb,rcb,n);
		pcb.type = "ready";
		ready_list.Insert(pcb);
		
	}

	private RCB GetRCB(String rid) {
		for (RCB rcb : rcb_list) {
			if (rcb.rid.equals(rid))
				return rcb; 
		}
		
		// it will never get to this case since we only have 4 Resources 
		return null; 
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
