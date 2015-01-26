package entity;

/**
 * Enumerator for the supported source code manager 
 * 
 */
public enum SCM {
	SOURCE_FORGE, GIT, SVN, HG, UNKNOWN, NONE;

	public String toString() {
		return name();
	}
}