package cryption;

import java.security.MessageDigest;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;

import config.parameters;

public class Sha3Cryption {

	public static String hash(final String input) {
		final DigestSHA3 sha3 = new DigestSHA3(parameters.Sha3BitLen);
		sha3.update(input.getBytes());
		return Sha3Cryption.hashToString(sha3);
	}

	private static String hashToString(MessageDigest hash) {
		return hashToString(hash.digest());
	}

	private static String hashToString(byte[] hash) {
		StringBuffer buff = new StringBuffer();

		for (byte b : hash) {
			buff.append(String.format("%02x", b & 0xFF));
		}
		return buff.toString();
	}
}
