package config.groups;

import java.io.Serializable;
import java.math.BigInteger;

import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import config.ParamGroup;

public class LightParameters implements ParamGroup, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5429403622328481464L;
	/* RSA Parameters */
	static final int RsaSrength = 1024;
	static final BigInteger RsaPublicExp = new BigInteger("138179");

	/* Bcrypt Parameters */
	static final int BcryptSaltLogRounds = 10; // must fall between 10 and 30; the larger the longer to generate

	/* AES Parameters */
	static final int AesIters = 30;
	static final int AesKeySize = 256;

	/* Argon2 Parameters */
	static final int ArgonSaltLen = 64;
	static final int ArgonHashLen = 64;
	static final int ArgonMemory = 50;
	static final int ArgonParallelism = 8;
	static final int ArgonIters = 10;
	static final Argon2Types ArgonType = Argon2Factory.Argon2Types.ARGON2id;

	/* SHA3 Parameters */
	static final int Sha3BitLen = 512;

	
	public int getRsasrength() {
		return RsaSrength;
	}

	public BigInteger getRsapublicexp() {
		return RsaPublicExp;
	}

	public int getBcryptsaltlogrounds() {
		return BcryptSaltLogRounds;
	}

	public int getAesiters() {
		return AesIters;
	}

	public int getAeskeysize() {
		return AesKeySize;
	}

	public int getArgonsaltlen() {
		return ArgonSaltLen;
	}

	public int getArgonhashlen() {
		return ArgonHashLen;
	}

	public int getArgonmemory() {
		return ArgonMemory;
	}

	public int getArgonparallelism() {
		return ArgonParallelism;
	}

	public int getArgoniters() {
		return ArgonIters;
	}

	public Argon2Types getArgontype() {
		return ArgonType;
	}

	public int getSha3bitlen() {
		return Sha3BitLen;
	}
	}
