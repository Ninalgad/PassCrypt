package manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import fileHandlers.SerialFiles;
import gui.MessageDisplay;

public class MangerSerializer {
  public static boolean createNewManager() {
    String dir = DatabaseManager.MAIN_DIR;
    File f1 = new File(dir);
    Boolean check = true;
    String pass = null;
    while (check) {
      check = false;
      
      // Get Master Password
      pass = MessageDisplay.inputDisplay("Input the master password.");
      if (pass != null) {
        try {
          Files.createDirectory(f1.toPath());
        } catch (IOException e) {
          MessageDisplay
              .textDisplay("A manager already exists on this system. Please delete the current"
                  + " manager if you wish to create a new one.");
          check = true;
        }
        DatabaseManager manager = new DatabaseManager(pass.toCharArray());
        SerialFiles.serializeObject(manager, DatabaseManager.MAIN_PATH);
        MessageDisplay.textDisplay("Succesfully created new manager!");
        return true;
      }
      
      
      // Get Parameters
      pass = MessageDisplay.inputDisplay("Input the master password."); 
    }
    return false;
  }

  public static DatabaseManager getManager() throws Exception {
    return (DatabaseManager) SerialFiles.deSerializeObject(DatabaseManager.MAIN_PATH);
  }
}
