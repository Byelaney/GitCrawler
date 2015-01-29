package usefuldata;

public class Node 
{
	private int size;
	private String name;

	public Node(String name, int size) 
	{
		// TODO Auto-generated constructor stub
	this.size=size;
	this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}

