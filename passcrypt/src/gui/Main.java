package gui;

import javax.swing.SwingUtilities;

public class Main {
	public static final String versionId = "2.1.0";
	public static final String startUpMessage = "Welcome to PassCrypt " + versionId + ".";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StartUpPanel.createAndShow();
			};
		});
	}
}
