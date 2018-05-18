package hashing;

import config.ParamGroup;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;


public class Argon2Wrap {
  public static String hash(char[] word, ParamGroup parameters) {
    Argon2 cypher = Argon2Factory.create(parameters.getArgontype(), parameters.getArgonsaltlen(), parameters.getArgonhashlen());
    return cypher.hash(parameters.getArgoniters(), parameters.getArgonmemory(), parameters.getArgonparallelism(), word);
  }
  
  public static boolean verify(String hash, char[] word, ParamGroup parameters) {
    Argon2 cypher = Argon2Factory.create(parameters.getArgontype(), parameters.getArgonsaltlen(), parameters.getArgonhashlen());
    return cypher.verify(hash, word);
  }
}
