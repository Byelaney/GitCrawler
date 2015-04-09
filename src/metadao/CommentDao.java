package metadao;

import java.util.ArrayList;
import java.util.List;

public interface CommentDao {
	public boolean addComment(entity.Comment comment,int project_id);
	
	public boolean addComments(List<entity.Comment> comments,int project_id);
	
	public boolean CheckaddComments(List<entity.Comment> comments,int project_id);
		
	public ArrayList<usefuldata.Comment> getUsefulComments(String projectName,int project_id);
	
	public entity.Comment getComment(int id,int project_id);
	
	public boolean updateComment(entity.Comment comment,int project_id);
}
