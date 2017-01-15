package dataStructures;

public class RCBNode
{
	private RCB rcb;
	private int n; 
	public RCBNode(RCB rcb, int n) {
		this.rcb = rcb;
		this.n = n; 
	}
	
	public void Increment(int n) {
		this.n += n; 
	}
	
	public void Decrement(int n) {
		this.n -= n; 
	}
	
	public boolean ContainsRCB(RCB rcb) {
		if (this.rcb.equals(rcb))
			return true; 
		else
			return false; 
	}
	
	public int GetN() {
		return n; 
	}

}
