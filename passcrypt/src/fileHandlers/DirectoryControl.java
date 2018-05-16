package fileHandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DirectoryControl {

	public static ArrayList<String> getFilenames(String directory) {
		ArrayList<String> fileNames = new ArrayList<String>();
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileNames.add(listOfFiles[i].getName());
			}
		}
		return fileNames;
	}

	public static void printHashMap(HashMap<String, String> map) {
		for (String key : map.keySet()) {
			System.out.println(key + " : " + map.get(key));
		}
	}

	public static void deleteFilesInDir(String directory) {
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
			if (!file.isDirectory())
				file.delete();
		}
	}

	public static boolean fileExists(String filename) {
		File f = new File(filename);
		return f.exists() && !f.isDirectory();
	}

}
