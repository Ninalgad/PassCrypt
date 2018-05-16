package cryption;

public class CompoundHash {
  public static String hash(char[] word, String salt) {
    return Argon2Wrap.hash(Sha3Cryption.hash(jBcrypt.hashpw(new String(word), salt)).toCharArray());
  }
  
  public static boolean verify(String hash, char[] password, String salt) {
    return Argon2Wrap.verify(hash, Sha3Cryption.hash(jBcrypt.hashpw(new String(password), salt)).toCharArray());
  }
}
