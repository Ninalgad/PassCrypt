package config;

import java.math.BigInteger;

import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class parameters {
	/* RSA Parameters */
	public static final int RsaSrength = 4096;
	public static final BigInteger PUBLIC_EXP = new BigInteger("138179");

	/* Bcrypt Parameters */
	public static final int BcryptSaltLogRounds = 16; // must fall between 10 and 30; the larger the longer to generate

	/* AES Parameters */
	public static final int iterations = 65536;
	public static final int keySize = 256;

	/* Argon2 Parameters */
	public static final int saltLen = 128;
	public static final int hashLen = 128;
	public static final int memory = 100;
	public static final int parallelism = 5;
	public static final int iters = 97543;
	public static final Argon2Types ArgonType = Argon2Factory.Argon2Types.ARGON2id;

	/* SHA3 Parameters */
	public static final int Sha3BitLen = 512;
}
