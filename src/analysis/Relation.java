package analysis;


public interface Relation {
	/**
	 * 
	 * @param developers
	 *            ' name
	 * @param projectName
	 * @param releaseName
	 * @return relations with json format
	 */
	public String getRelations();

	/**
	 * 
	 * @param developers
	 *            ' name
	 * @param projectName
	 * @param releaseName
	 * @return main relations with json format
	 */

	public String getMainRelations();

}
