package fileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cryption.RsaCryption;

public class SerialFiles {
	
	public static void serializeObject(Object obj, String fileName) {
		
		try {
			FileOutputStream f = new FileOutputStream(new File(fileName));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(obj);
			o.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object deSerializeObject(String fileName) throws Exception {
		FileInputStream fi;
		ObjectInputStream oi;
		Object obj = null;
			fi = new FileInputStream(new File(fileName));
			oi = new ObjectInputStream(fi);
			obj =  oi.readObject();
			oi.close();
			fi.close();
		return obj;
	}
	
	
}
