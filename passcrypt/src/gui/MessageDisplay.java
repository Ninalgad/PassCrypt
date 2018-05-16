package gui;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageDisplay {
  static String programName = "Titan";
  public static void scrollDisplay(String message, String header) {
    JTextArea ta = new JTextArea(3, 10);
    ta.setText(message);
    ta.setWrapStyleWord(true);
    ta.setLineWrap(true);
    ta.setCaretPosition(0);
    ta.setEditable(false);

      JOptionPane.showMessageDialog(null, new JScrollPane(ta), header,
        JOptionPane.INFORMATION_MESSAGE);
  }
  
  public static void textDisplay(String message) {
    JOptionPane.showMessageDialog(null, message, programName, JOptionPane.INFORMATION_MESSAGE);
  } 
  
  
  public static String inputDisplay(String message) {
    return JOptionPane.showInputDialog(null, message, programName,
        JOptionPane.INFORMATION_MESSAGE);
  }
}
