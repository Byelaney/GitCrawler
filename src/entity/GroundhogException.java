package entity;

/**
 * The base {@link Exception} type. All exceptions must
 * extend this class.
 * 
 * 
 */
public class GroundhogException extends RuntimeException {
	private static final long serialVersionUID = -3563928567447310893L;

	public GroundhogException() {
		super();
	}

	public GroundhogException(String msg) {
		super(msg);
	}

	public GroundhogException(Throwable cause) {
		super(cause);
	}

	public GroundhogException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
