package scmclient;

import entity.GroundhogException;



/**
 * Thrown when a checkout operation is attempted for a date that corresponds
 * to phase when the project has zero commits.
 * 
 *
 */
public class EmptyProjectAtDateException extends GroundhogException {	
	private static final long serialVersionUID = 1L;
	
	public EmptyProjectAtDateException(String msg) {
		super("No source code was found to this date: " + msg);
	}
}