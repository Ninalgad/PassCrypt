package cryption;

import java.math.BigInteger;
import java.security.SecureRandom;

import config.ParamGroup;


public class RsaParamGenerator {
	private BigInteger privateExponent;
	private BigInteger publicMod;
	
	public RsaParamGenerator(ParamGroup parameters) {
		int bitLength = parameters.getRsasrength();
		BigInteger p = genBigPrime(bitLength);
		BigInteger q = genBigPrime(bitLength);
		BigInteger e = parameters.getRsapublicexp();
		BigInteger d = genPrivateKey(e, p, q);
		BigInteger mp = p.add(new BigInteger("-1"));
		BigInteger mq = q.add(new BigInteger("-1"));
		BigInteger L = lcm(mp, mq);
		
		while(!checks(p, q, e, d, L)) {
			p = genBigPrime(bitLength);
			q = genBigPrime(bitLength);
			d = genPrivateKey(e, p, q);
			mp = p.add(new BigInteger("-1"));
			mq = q.add(new BigInteger("-1"));
			L = lcm(mp, mq);
		}
		privateExponent = d;
		publicMod = p.multiply(q);
	}
	
	
	public BigInteger getPublicMod() {
		return publicMod;
	}
	
	public BigInteger getPrivateExponent() {
		return privateExponent;
	}
	
	private static BigInteger genBigPrime(int bitLength) {
		return BigInteger.probablePrime(bitLength, new SecureRandom());
	}

	private static BigInteger genPrivateKey(BigInteger e, BigInteger p, BigInteger q) {
		BigInteger mp = p.add(new BigInteger("-1"));
		BigInteger mq = q.add(new BigInteger("-1"));
		return e.modInverse(mp.multiply(mq));
	}

	private static BigInteger lcm(BigInteger a, BigInteger b) {
		return a.multiply(b.divide(a.gcd(b)));
	}

	private static Boolean checks(BigInteger p, BigInteger q, BigInteger e, BigInteger d, BigInteger l) {
		BigInteger mp = p.add(new BigInteger("-1"));
		BigInteger mq = q.add(new BigInteger("-1"));
		return (d.compareTo(l) == -1) && (mp.gcd(mq).compareTo(new BigInteger("2")) == 0) && (e.compareTo(l) == -1)
				&& (e.gcd(l).equals(BigInteger.ONE));
	}
}
