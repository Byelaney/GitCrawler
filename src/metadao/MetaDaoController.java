package metadao;
import crawler.DataSource;

public abstract class MetaDaoController {
	protected DataSource dataSource;
	
	public MetaDaoController(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public abstract void IntoDataBase();

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
