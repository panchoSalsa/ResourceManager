package dataStructures;

import java.util.LinkedList;

public class RCB
{
	public String rid;
	public int k;
	public int available; 
	public LinkedList<PCBNode> blocked_list; 
	
	// I will initialize each RCB as RCB(1), to mean RCB with rid "R1"
	public RCB(int rid) {
		this.rid = "R" + rid; 
		k = rid; 
		available = k; 
		blocked_list = new LinkedList<PCBNode>();
	}
	
	public void InsertIntoBlockList(PCB pcb, int requesting) {
		blocked_list.add(new PCBNode(pcb,requesting));
	}
	
	public void RemoveFromBlockList(PCB pcb) {
		blocked_list.remove(pcb);
	}
	
	public void AssignResources(int n) {
		available -= n; 
	}
	
	public boolean EnoughResources(int n) {
		return available >= n; 
	}

	public void RestoreResources(int n)
	{
		available += n;
		
	}

	public boolean CanIReleaseNext()
	{
		PCBNode next = blocked_list.getFirst();
		if (available >= next.GetRequestingAmount())
			return true; 
		else
			return false; 
	}

	public boolean ValidRequest(int n)
	{
		return k >= n; 
	}
}
