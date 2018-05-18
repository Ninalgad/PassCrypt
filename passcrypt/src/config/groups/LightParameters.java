package config.groups;

import java.math.BigInteger;

import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import config.ParamGroup;

public class LightParameters implements ParamGroup {
	/* RSA Parameters */
	static final int RsaSrength = 4096;
	static final BigInteger RsaPublicExp = new BigInteger("138179");

	/* Bcrypt Parameters */
	static final int BcryptSaltLogRounds = 16; // must fall between 10 and 30; the larger the longer to generate

	/* AES Parameters */
	static final int AesIters = 65536;
	static final int AesKeySize = 256;

	/* Argon2 Parameters */
	static final int ArgonSaltLen = 128;
	static final int ArgonHashLen = 128;
	static final int ArgonMemory = 100;
	static final int ArgonParallelism = 5;
	static final int ArgonIters = 97543;
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
