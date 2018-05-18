package config;

import java.math.BigInteger;

import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public interface ParamGroup {
	public int getRsasrength();
	
	public BigInteger getRsapublicexp();

	public int getBcryptsaltlogrounds();

	public int getAesiters();

	public int getAeskeysize();

	public int getArgonsaltlen();

	public int getArgonhashlen();

	public int getArgonmemory();

	public int getArgonparallelism();

	public int getArgoniters();

	public Argon2Types getArgontype();

	public int getSha3bitlen();
	
}
