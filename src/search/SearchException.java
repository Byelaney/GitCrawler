package search;

import entity.GroundhogException;


/**
 * Thrown when something nasty happens
 * 
 * 
 */
public class SearchException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public SearchException(Throwable e) {
		super(e);
	}
}