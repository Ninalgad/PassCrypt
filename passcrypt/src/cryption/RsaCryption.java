package cryption;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.Serializable;
import java.math.*;

import org.bouncycastle.util.encoders.Base64;

public class RsaCryption implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8775668732166875441L;
	private static final String CIPHER_ALGO = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
	private static final String FACTORY_ALGO = "RSA";
	
	private RSAPublicKey pubKey;

	// private String encrypted;s
	ArrayList<String> encryptedArray = new ArrayList<String>();

	public RsaCryption(String message, String pubExponent, String pubModulus, String priExponent) {
		ArrayList<String> messageArray = new ArrayList<String>(Arrays.asList(message.split("")));
		Cipher cipher = null;
		KeyFactory keyFactory = null;
		RSAPublicKeySpec pubKeySpec = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGO);
			keyFactory = KeyFactory.getInstance(FACTORY_ALGO);
			pubKeySpec = new RSAPublicKeySpec(new BigInteger(pubModulus), new BigInteger(pubExponent));
			pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] cipherText = null;
		for (String s : messageArray) {
			try {
				
				cipherText = cipher.doFinal(s.getBytes());
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			encryptedArray.add(Base64.toBase64String(cipherText));
		}
		messageArray.clear();
	}

	public String decrypt(String pubModulus, String priExponent) {
		RSAPrivateKey privKey = null;
		RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(pubModulus), new BigInteger(priExponent));
		byte[] plainText = null;
		Cipher cipher = null;
		String message = "";
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(FACTORY_ALGO);
			privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);
			cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			for (String x : encryptedArray) {
				plainText = cipher.doFinal(Base64.decode(x));
				message += new String(plainText);
			}
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
