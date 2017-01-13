package dataStructures;

import java.util.LinkedList;

public class ReadyList
{
	public LinkedList<PCB> level_two;
	public LinkedList<PCB> level_one;
	public LinkedList<PCB> level_zero;
	
	public ReadyList() {
		level_two = new LinkedList<PCB>();
		level_one = new LinkedList<PCB>();
		level_zero = new LinkedList<PCB>();
	}
	
	public void Insert(PCB pcb) {
		switch(pcb.priority) {
			case 0: 
				level_zero.add(pcb);
				break; 
			case 1: 
				level_one.add(pcb);
				break; 
			case 2: 
				level_two.add(pcb);
				break; 
		}
	}
	
	public void Clear() {
		level_two.clear();
		level_one.clear();
		level_zero.clear();
	}
	
	public void PrintContents() {
		System.out.println("level_two -> ");
		for (PCB pcb: level_two) {
			System.out.println( pcb.pid + " -> ");
		}
		System.out.println( "NULL");
		
		System.out.println("level_one -> ");
		for (PCB pcb: level_one) {
			System.out.println( pcb.pid + " -> ");
		}
		System.out.println( "NULL");
		
		System.out.println("level_zero -> ");
		for (PCB pcb: level_zero) {
			System.out.println( pcb.pid + " -> ");
		}
		System.out.println( "NULL");
	}

	public PCB GetHighestPriority()
	{
		if (! level_two.isEmpty())
			return level_two.getFirst();
		else if (! level_one.isEmpty())
			return level_one.getFirst();
		else
			return level_zero.getFirst();
	}

	public void MoveToTheEnd(PCB self)
	{
		switch(self.priority) {
			case 0:
				level_zero.remove();
				level_zero.add(self);
				break; 
			case 1: 
				level_one.remove();
				level_one.add(self);
				break; 
			case 2: 
				level_two.remove();
				level_two.add(self);
				break; 
		}
	}
	
	public void RemovePCB(PCB pcb) {
		switch(pcb.priority) {
			case 0:
				level_zero.remove(pcb);
				break; 
			case 1: 
				level_one.remove(pcb);
				break; 
			case 2: 
				level_two.remove(pcb);
				break; 
		}
	}
}
