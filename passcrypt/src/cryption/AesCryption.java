package cryption;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import config.parameters;


public class AesCryption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4991667901785311885L;
	private byte[] ivBytes;
	
	private String encryptedText;
	
	public AesCryption(char[] password, String message, String salt) {
		try {
			encryptedText = encrypt(password, message, salt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getEncryptedText() {
		return encryptedText;
	}
	
	public String getDecryptedText(char[] password, String salt) {
		String plainText = "";
		try {
			plainText = decrypt(password, this.encryptedText, salt);
		} catch (Exception e) {
			return "Couldn't decode " + this.getClass();
		}
		return plainText;
	}

	private String encrypt(char[] password, String message, String salt) throws Exception {
		fixKeyLength();
		byte[] saltBytes = salt.getBytes();

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(password, saltBytes, parameters.iterations, parameters.keySize);
		SecretKey secretKey = skf.generateSecret(spec);
		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
		AlgorithmParameters params = cipher.getParameters();
		ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));

		return DatatypeConverter.printBase64Binary(encryptedTextBytes);
	}

	private String decrypt(char[] password, String encryptedText, String salt) throws Exception {
		fixKeyLength();
		byte[] saltBytes = salt.getBytes();
		
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(password, saltBytes, parameters.iterations, parameters.keySize);
		SecretKey secretKey = skf.generateSecret(spec);

		byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(encryptedText);
		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

		return new String(decryptedTextBytes);

	}

	public static String getSalt() throws Exception {

		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[20];
		sr.nextBytes(salt);
		return new String(salt);
	}
	
	@SuppressWarnings("unchecked")
	public static void fixKeyLength() {
	    String errorString = "Failed manually overriding key-length permissions.";
	    int newMaxKeyLength;
	    try {
	        if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
	            Class<?> c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
	            Constructor<?> con = c.getDeclaredConstructor();
	            con.setAccessible(true);
	            Object allPermissionCollection = con.newInstance();
	            Field f = c.getDeclaredField("all_allowed");
	            f.setAccessible(true);
	            f.setBoolean(allPermissionCollection, true);

	            c = Class.forName("javax.crypto.CryptoPermissions");
	            con = c.getDeclaredConstructor();
	            con.setAccessible(true);
	            Object allPermissions = con.newInstance();
	            f = c.getDeclaredField("perms");
	            f.setAccessible(true);
	            ((Map<String, Object>) f.get(allPermissions)).put("*", allPermissionCollection);

	            c = Class.forName("javax.crypto.JceSecurityManager");
	            f = c.getDeclaredField("defaultPolicy");
	            f.setAccessible(true);
	            Field mf = Field.class.getDeclaredField("modifiers");
	            mf.setAccessible(true);
	            mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
	            f.set(null, allPermissions);

	            newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
	        }
	    } catch (Exception e) {
	        throw new RuntimeException(errorString, e);
	    }
	    if (newMaxKeyLength < 256)
	        throw new RuntimeException(errorString); // hack failed
	}
}

