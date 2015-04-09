package dao;

import java.util.List;

import usefuldata.Developer;
import usefuldata.Vitality;

public interface DeveloperDao {
	
	/**
	 * try to find developer with id(for other operations)
	 * @param name
	 * @return Developer with basic info
	 */
	public Developer findDeveloper(String name);
	
	
	/**
	 * try to find developer with id(for other operations)
	 * @param id
	 * @return Developer with basic info
	 */
	public Developer findDeveloperForName(int id);
	
	 
	/**
	 * try to find vitality list using developer's id
	 * @param developer
	 * @return Developer with list<vitality>
	 */
	public Developer findDeveloperWithVitality(Developer developer);
	
	/**
	 * try to find vitality list using developer's id
	 * @param developer
	 * @return list<vitality>
	 */
	public List<Vitality> findDeveloperForVitality(Developer developer);
		
	/**
	 * try to find project list using developer's id
	 * @param developer
	 * @return list<project> with reference uninitialized
	 */
	public List<usefuldata.Project> findDeveloperForProjects(Developer developer);
	
	/**
	 * try to delete developer.
	 * this doesn't delete involved project or release
	 * @param name
	 * @return
	 */
	public boolean deleteDeveloper(String name);
	
	/**
	 * try to add developer.do nothing to involved project or release
	 * @param developer
	 * @return
	 */
	public boolean addDeveloper(Developer developer);
	
	
	public boolean addDevelopers(List<Developer> dps);
	
	public boolean updateDeveloper(Developer developer);
	
}
