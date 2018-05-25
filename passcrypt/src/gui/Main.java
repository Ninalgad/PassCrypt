package gui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static final String versionId = "2.1.0";
	public static final String startUpMessage = "Welcome to PassCrypt " + versionId + ".";
	public static final String programName = "PassCrypt";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StartUpPanel.createAndShow();
			};
		});
	}
	
	static void transitionFrames(JFrame startFrame, JComponent nextFrame, String nextFrameTitle) {
		// close the previous frame
		startFrame.dispose();
		
		// Create and set up the window.
		JFrame frame = new JFrame(nextFrameTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// Create and set up the next content pane.
		nextFrame.setOpaque(true); // content panes must be opaque
		frame.setContentPane(nextFrame);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	} 
}
