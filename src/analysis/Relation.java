package analysis;


public interface Relation {
	/**
	 *  force_echarts
	 * @param developers
	 *            ' name
	 * @param projectName
	 * @param releaseName
	 * @return relations with json format
	 */
	public String getRelations();

	/**
	 * force_echarts
	 * @param developers
	 *            ' name
	 * @param projectName
	 * @param releaseName
	 * @return main relations with json format
	 */

	public String getMainRelations();

}
