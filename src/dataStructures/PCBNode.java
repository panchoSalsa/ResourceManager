package dataStructures;

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