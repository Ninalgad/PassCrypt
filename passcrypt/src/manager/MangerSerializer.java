package manager;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

import fileHandlers.SerialFiles;
import gui.InterfacePanel;


public class MangerSerializer {
	public static boolean offerNewManager(char[] password, DatabaseManager manager, JFrame setupFrame) {
		Path path = Paths.get(manager.getDatabaseDirectory());
		if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
			return false;
		}
		
		SerialFiles.serializeObject(manager, manager.getDatabasePath());
		InterfacePanel.createAndShow(manager, setupFrame);
		return true;
	}
	
	public static boolean isManager(String path) {
		boolean check = true;
		try {
			DatabaseManager proposed = (DatabaseManager) SerialFiles.deSerializeObject(path);
		} catch (Exception e) {
			check = false;
			e.printStackTrace();
		}
		return check;
	}

	public static DatabaseManager getManager(String managerPath) {
		DatabaseManager proposed = null;
		try {
			proposed = (DatabaseManager) SerialFiles.deSerializeObject(managerPath);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return proposed;
	}
}
