package cryption;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import config.parameters;


public class Argon2Wrap {
  public static String hash(char[] word) {
    Argon2 cypher = Argon2Factory.create(parameters.ArgonType, parameters.saltLen, parameters.hashLen);
    return cypher.hash(parameters.iters, parameters.memory, parameters.parallelism, word);
  }
  
  public static boolean verify(String hash, char[] word) {
    Argon2 cypher = Argon2Factory.create(parameters.ArgonType, parameters.saltLen, parameters.hashLen);
    return cypher.verify(hash, word);
  }
}
