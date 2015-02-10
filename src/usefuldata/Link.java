package usefuldata;

/**
 * 
 * @author Bacchus_yl
 * 表示node 间关联
 */
public class Link {
	private int source;
	private int target;

	public Link(int i, int j) {
		// TODO Auto-generated constructor stub
		source=i;
		target=j;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}


