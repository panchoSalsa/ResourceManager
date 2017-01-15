package dataStructures;

import java.util.LinkedList;

public class PCB
{
	public String pid; 
	public String type; 
	public LinkedList<RCBNode> other_resources;
	public LinkedList<PCBNode> blocked_list; 
	public PCB parent; 
	public LinkedList<PCB> children;
	public int priority; 
	
	// parent is self, current running process. 
	public PCB (String[] parameters, PCB parent) {
		pid = parameters[1];
		type = "ready";
		other_resources = new LinkedList<RCBNode>();
		blocked_list = null; 
		this.parent = parent;
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
	
	public RCBNode GetRCBNode(RCB rcb) {
		for (RCBNode node: other_resources) {
			if (node.ContainsRCB(rcb))
				return node;
		}
		
		// null is return if resource is not found inside other_resources list
		return null; 
	}

	public void UpdateOtherResources(RCB rcb, int n)
	{
		RCBNode node = GetRCBNode(rcb);
		
		node.Decrement(n);
		
		// when n gets to zero we can remove that resource from the 
		// other_resources list
		if (node.GetN() == 0)
			other_resources.remove(node);
	}
}
