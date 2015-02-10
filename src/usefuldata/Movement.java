package usefuldata;

/**
 * 
 * @author Bacchus_yl
 * 表示用户迁移状况
 *name为开发者用语名
 *move表示 进入莒南迁出（leave or join）
 *date 表示迁移时间
 *leave:某次版本开发用户没有参加，以上一版本最后一次提交时间记
 *join:第一次提交或者move状态变为leave后的第一次提交时间记
 */

public class Movement 
{
	private String name;
    private String move;
    private String date;

	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}
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
    

}
