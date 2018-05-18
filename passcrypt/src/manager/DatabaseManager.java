package manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import config.PrameterGroupFactory;
import config.ParamGroup;
import config.PrameterGroupTypes;
import cryption.AesCryption;
import hashing.CompoundHash;
import cryption.RsaCryption;
import cryption.RsaParamGenerator;
import hashing.jBcrypt;
import gui.MessageDisplay;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class DatabaseManager implements Serializable {
  /**
   * Stores hashes, encryption 
   */
  public static final String MAIN_FILE_NAME = "Cronus.txt";
  public static final String MAIN_DIR = "C:\\Users\\User\\Desktop\\titan1";
  public static final String MAIN_PATH = MAIN_DIR + "/" + MAIN_FILE_NAME;

  private static final long serialVersionUID = -8832239264230670442L;
  String baseSalt; // really is not necessary, works well against non-personal attacks

  private HashMap<String, AesCryption> cryptedPasswords = new HashMap<String, AesCryption>();
  private HashMap<String, RsaCryption> cryptedSalts = new HashMap<String, RsaCryption>();

  private AesCryption rsaPublicMod;
  private AesCryption rsaPrivateKey;
  private String masterPasswordHash;
  
  private static ParamGroup parameters = PrameterGroupFactory.create(PrameterGroupTypes.BALANCED);



public DatabaseManager(char[] masterPass) {
    baseSalt = jBcrypt.gensalt(parameters.getBcryptsaltlogrounds());
    masterPasswordHash = CompoundHash.hash(masterPass, baseSalt, parameters);
    RsaParamGenerator params = new RsaParamGenerator(parameters);
    rsaPublicMod = new AesCryption(masterPass, params.getPublicMod().toString(), baseSalt, parameters);
    rsaPrivateKey = new AesCryption(masterPass, params.getPrivateExponent().toString(), baseSalt, parameters);
    wipe(masterPass);
  }


  public boolean offerNewEncrypted(String label, char[] password, String newPass) {

    if (cryptedPasswords.containsKey(label)) {
      MessageDisplay.textDisplay("'" + label + "' already exists visible directory.");
      return false;
    }
    if (!CompoundHash.verify(masterPasswordHash, password, baseSalt, parameters)) {
      // This check only insures that all encryption performed in
      // encryptedPassKeys use the master key, for consistency.
      MessageDisplay.textDisplay("Incorrect master password");
      return false;
    }

    String salt = jBcrypt.gensalt(parameters.getBcryptsaltlogrounds());
    String pubExp = parameters.getRsapublicexp().toString();
    RsaCryption saltEncoder =
        new RsaCryption(salt, pubExp, getPublicMod(password), getPrivateKey(password));
    cryptedSalts.put(label, saltEncoder);
    cryptedPasswords.put(label, new AesCryption(password, newPass, salt, parameters));
    wipe(password);
    MessageDisplay.textDisplay("Successfully added new Password");
    return true;
  }

  public String getEncrypted(String label, char[] masterPass) {
    if (!cryptedPasswords.containsKey(label)) {
      MessageDisplay.textDisplay(label + " doesn't exists in visible directory");
      return null;
    }
    if (!CompoundHash.verify(masterPasswordHash, masterPass, baseSalt, parameters)) {
      // This check only insures that all encryption performed in
      // encryptedPassKeys use the master key, for consistency.
      MessageDisplay.textDisplay("Incorrect master password");
      return null;
    }

    RsaCryption saltEncoder = cryptedSalts.get(label);
    String salt = saltEncoder.decrypt(getPublicMod(masterPass), getPrivateKey(masterPass));
    String word = cryptedPasswords.get(label).getDecryptedText(masterPass, salt, parameters);
    wipe(masterPass);
    return word;
  }

  public boolean delete(String usage, char[] masterPass) {
    if (!CompoundHash.verify(masterPasswordHash, masterPass, baseSalt, parameters)) {
      MessageDisplay.textDisplay("Incorrect master password");
      return false;
    } else {
      wipe(masterPass);
      cryptedPasswords.remove(usage);
      cryptedSalts.remove(usage);
      return true;
    }
  }
  
  public Set<String> getUsages() {
    return cryptedPasswords.keySet();
  } 
  
  
  private String getPrivateKey(char[] masterPass) {
    return rsaPrivateKey.getDecryptedText(masterPass, baseSalt, parameters);
  }

  private String getPublicMod(char[] masterPass) {
    return rsaPublicMod.getDecryptedText(masterPass, baseSalt, parameters);
  }
  
  private static void wipe(char[] word) {
    Argon2 cypher = Argon2Factory.create(parameters.getArgontype(), parameters.getArgonsaltlen(), parameters.getArgonhashlen());
    cypher.wipeArray(word);
  }
}
