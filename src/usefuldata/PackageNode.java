package usefuldata;

import java.util.ArrayList;
import java.util.List;

public class PackageNode {
	private List<PackageNode> children = new ArrayList<PackageNode>();
	private List<Integer> childrenIndex = new ArrayList<Integer>();
	private String path;
	private String name;

	public PackageNode(String path, String name) {
		super();
		this.setName(name);
		this.setPath(path);
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	public String getName() {
		return name;
	}

	public List<PackageNode> getChildren() {
		return children;
	}

	public void setChildren(List<PackageNode> children) {
		this.children = children;
	}
    public List<Integer> takeCIndex()
    {
    	return childrenIndex;
    }

	public void addChild(int i) {
		childrenIndex.add(i);
	}	

	public void addChild(PackageNode pn) {
		children.add(pn);
	}

	

	public void setName(String name) {
		this.name = name;
	}

	public String takePath() {
		return path;
	}


}
