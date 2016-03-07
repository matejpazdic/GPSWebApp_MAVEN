package sk.tuke.fei.kpi.utils.hash;

/**
 * Exception implementation for getting known why the hashing of string failed
 *
 * @author matejpazdic
 * @date 07.03.2016.
 */
public class HashingException extends Exception {

	/**
	 * Constructor for defining the custom message with this exception
	 *
	 * @param message The message
	 */
	public HashingException(final String message) {
		super(message);
	}

	/**
	 * Constructor for defining the cause of exception
	 *
	 * @param cause The upper exception
	 */
	public HashingException(final Throwable cause) {
		super(cause);
	}

}
