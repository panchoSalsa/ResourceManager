package dataStructures;


// Used by the RCB class to keep track of which processes are blocked and how many resources they are requesting
public class PCBNode
{
	private PCB pcb;
	private int requesting; 
	
	public PCBNode(PCB pcb, int requesting) {
		this.pcb = pcb;
		this.requesting = requesting; 
	}
	
	public int GetRequestingAmount() {
		return requesting; 
	}

	public PCB GetPCB()
	{
		return pcb; 
	}

}