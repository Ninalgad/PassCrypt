package hashing;

import config.ParamGroup;

public class CompoundHash {
  public static String hash(char[] word, String salt, ParamGroup options) {
    return Argon2Wrap.hash(Sha3Cryption.hash(jBcrypt.hashpw(new String(word), salt), options).toCharArray(), options);
  }
  
  public static boolean verify(String hash, char[] password, String salt, ParamGroup options) {
    return Argon2Wrap.verify(hash, Sha3Cryption.hash(jBcrypt.hashpw(new String(password), salt), options).toCharArray(), options);
  }
}
