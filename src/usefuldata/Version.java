package usefuldata;

import java.util.ArrayList;
import java.util.List;
 /**
  * 
  * @author Bacchus_ly
  * name:release name
  * date: 版本发布时间
  * radar：存放雷达图数据
  */
public class Version 
{
	private String name;
	private String date;
	private List<List<Radar>> radar=new ArrayList<List<Radar>>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<List<Radar>> getRadar() {
		return radar;
	}
	public void setRadar(List<List<Radar>> radarlist) {
		this.radar = radarlist;
	}

}
