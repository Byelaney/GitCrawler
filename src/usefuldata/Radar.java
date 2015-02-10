package usefuldata;

/**
 * 
 * @author Bacchus_yl
 * 用以存放单位雷达图数据的实体
 *axis:issue  or comment or codes or developers or commit
 *value:axis对应的值
 */
public class Radar 
{
	private String axis;
	private int value;
	public Radar(String axis,int i)
	{
		this.setAxis(axis);
		this.value=i;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getAxis() {
		return axis;
	}
	public void setAxis(String axis) {
		this.axis = axis;
	}
	
	

}
