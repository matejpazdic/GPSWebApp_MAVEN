package sk.tuke.fei.kpi.utils.hash;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for hashing the input as string and generating the hash with different methods
 *
 * @author matejpazdic
 * @date 07.03.2016.
 */
public final class StringHasher {

	/** Enum that defines the hashing algorithm */
	public enum HashingType {
		MD5, SHA1
	}

	/** Defines the charset for encoding into hash */
	public static final String CHARSET = "UTF8";

	/**
	 * Private helper method for getting the instance of MessageDigest with specified algorithm
	 *
	 * @param type The HashingType for selecting the hashing alrogithm to return
	 * @return The MessageDigest object
	 * @throws NoSuchAlgorithmException
	 */
	private static MessageDigest getMessageDigest(final HashingType type) throws NoSuchAlgorithmException {
		if (type == null) {
			throw new NoSuchAlgorithmException("Algorithm cannot be null.");
		}

		return MessageDigest.getInstance(type.name());
	}

	/**
	 * Helper method that tries to hash input strin by selected method
	 *
	 * @param input The input string
	 * @param type The selected hash algorithm
	 * @return The hashed result or null when something went wrong
	 */
	public static String toHash(final String input, final HashingType type) throws HashingException {
		if (input == null) {
			throw new HashingException("The input string cannot be null");
		}

		final MessageDigest digest;
		try {
			digest = getMessageDigest(type);
		} catch (NoSuchAlgorithmException e) {
			throw new HashingException(e);
		}

		digest.update(input.getBytes(Charset.forName(CHARSET)));
		return new String(Hex.encodeHex(digest.digest()));
	}
}
