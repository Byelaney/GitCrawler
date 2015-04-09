package metadao;

import java.util.ArrayList;
import java.util.List;

import entity.User;

public interface UserDao {
	public boolean addUser(entity.User user);
	
	public boolean addUsers(List<entity.User> users);
	
	public entity.User getUser(String login);
	
	public boolean updateUser(entity.User user);
	
	public boolean CheckaddUser(entity.User user);
	
	public boolean CheckaddUsers(List<entity.User> users);
	
	public ArrayList<User> getAllUsers(List<entity.Contributor> contributors);
}
