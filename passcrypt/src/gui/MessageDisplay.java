package gui;

import javax.swing.JOptionPane;

public class MessageDisplay {

	/**
	 * Provides a simple text pop-up window for any kind of
	 * message to the user.
	 * 
	 * @param message	text to be displayed.
	 * @return			<code>null<code/>
	 */
	public static void textDisplay(String message) {

	} 

	/**
	 * Provides a simple text input pop-up window.
	 * 
	 * @param message	text to be displayed
	 * @return			the text inputed by the user.
	 */
	public static String inputDisplay(String message) {
		return JOptionPane.showInputDialog(null, message,
				Main.programName,
				JOptionPane.INFORMATION_MESSAGE
				);
	}
}
