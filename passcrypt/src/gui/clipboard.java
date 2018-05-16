package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Timer;
import java.util.TimerTask;


public class clipboard {
	public static void setClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(stringSelection, null);
    }
	
	public static void temporaryCopy(String text, int seconds) {
		setClipboard(text);
		Timer timer = new Timer();
		clipboard clip = new clipboard();
		timer.schedule(clip.new ClipboardSetter(), seconds * 1000);
	}
	
	class ClipboardSetter extends TimerTask {

		@Override
		public void run() {
			setClipboard("");
		}
		
	}
	

}
