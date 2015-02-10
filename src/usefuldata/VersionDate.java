package usefuldata;

/**
 * @author Bacchus_yl
 * 
 * version:release name
 * date:版本发布时间
 * order:是发布的第几个版本
 *
 */
public class VersionDate 
{
	private String version;//版本名称
	private String date;//发布时间
	private int order;//是第几个版本
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

}

