package dao;

import java.util.List;

import usefuldata.Developer;
import usefuldata.Vitality;

public interface VitalityDao {

	/**
	 * try to find vitality list using developer's id
	 * @param developer
	 * @return list<vitality>
	 */
	public List<Vitality> getVitality(Developer developer);
	
	public List<Vitality> getVitality(String developer);
	
	public List<Vitality> getVitality(String projectName,String releaseName,String developer);
	
	/**
	 * try to update vitality
	 * @param vitality
	 * 
	 * @return
	 */
	public boolean updateVitality(Vitality vitality);

	/**
	 * try to add vitality
	 * @param vitality
	 * 
	 * 
	 * @return
	 */
	public boolean addVitality(Vitality vitality);
	
	
	/**
	 * //try to delete a vitality entry
	 * @param vitality
	 * 
	 * @return
	 */
	public boolean deleteVitality(Vitality vitality);
	
	/**
	 * try to delete all vitality entries using developer_id
	 * @param id
	 * @return
	 */
	public boolean deleteAllVitality(int id);
}
