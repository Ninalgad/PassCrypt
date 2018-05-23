package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import config.ParamGroup;
import config.PrameterGroupFactory;
import config.PrameterGroupTypes;
import manager.DatabaseManager;
import manager.MangerSerializer;

public class SetupPanel extends JPanel 
implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame setupFrame;
	private JButton confirmButton = new JButton("Ok");

	private JLabel labelMasterPass = new JLabel("Master Password:");
	private JLabel labelRepeat = new JLabel("Repeat:");
	private JLabel labelSettings = new JLabel("Settings:");
	private JLabel databasePathLabel = new JLabel("Database Location:");
	private JLabel databaseNameLabel = new JLabel("Database Name:");

	// Text fields
	private JPasswordField masterPass = new JPasswordField(30);
	private JPasswordField masterPassRepeat = new JPasswordField(30);
	private JTextField databasePath = new JTextField(30);
	private JTextField databaseName = new JTextField(30);

	// JRadio buttons for PameterGroupTypes
	private static String balencedTypeStr = "Balanced";
	private static String heavyTypeStr = "Heavy";
	private static String lightTypeStr = "Light";

	// manager arguments
	private ParamGroup options;

	static boolean complete = false; // checks for when the set-up process is done

	public SetupPanel(JFrame frame) {
		super(new BorderLayout());

		this.setupFrame = frame;
		// Set-up confirmation button
		ConfirmListener listener = new ConfirmListener(confirmButton);
		confirmButton.setActionCommand("Confirm");
		confirmButton.addActionListener(listener);

		// Set-up JRadio buttons for PameterGroupTypes
		JRadioButton balencedTypeButton = new JRadioButton(balencedTypeStr);
		balencedTypeButton.setMnemonic(KeyEvent.VK_B);
		balencedTypeButton.setActionCommand(balencedTypeStr);
		balencedTypeButton.setSelected(true);
		balencedTypeButton.addActionListener(this);
		balencedTypeButton.setToolTipText("Moderate security settings with moderate computation time.");

		JRadioButton lightTypeButton = new JRadioButton(lightTypeStr);
		lightTypeButton.setMnemonic(KeyEvent.VK_C);
		lightTypeButton.setActionCommand(lightTypeStr);
		lightTypeButton.setSelected(false);
		lightTypeButton.addActionListener(this);
		lightTypeButton.setToolTipText("Lowest possible security settings but very fast.");

		JRadioButton heavyTypeButton = new JRadioButton(heavyTypeStr);
		heavyTypeButton.setMnemonic(KeyEvent.VK_C);
		heavyTypeButton.setActionCommand(heavyTypeStr);
		heavyTypeButton.setSelected(false);
		heavyTypeButton.addActionListener(this);
		heavyTypeButton.setToolTipText("High secuity settings in exchange for large computation time.");

		// Only one selection can be made
		ButtonGroup group = new ButtonGroup();
		group.add(balencedTypeButton);
		group.add(lightTypeButton);
		group.add(heavyTypeButton);

		// Create a panel that uses BoxLayout.
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		// Master password section
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(labelMasterPass, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 3;
		panel.add(masterPass, constraints);

		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(labelRepeat, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 3;
		panel.add(masterPassRepeat, constraints);

		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.gridwidth = 5;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

		// Security settings section
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(labelSettings, constraints);

		constraints.gridx = 1;
		panel.add(balencedTypeButton, constraints);

		constraints.gridx = 2;
		panel.add(lightTypeButton, constraints);

		constraints.gridx = 3;
		panel.add(heavyTypeButton, constraints);

		constraints.gridy = 4;
		constraints.gridx = 0;
		constraints.gridwidth = 5;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

		// Database section
		constraints.gridy = 5;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(databaseNameLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 3;
		panel.add(databaseName, constraints);

		constraints.gridy = 6;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(databasePathLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 3;
		panel.add(databasePath, constraints);

		constraints.gridy = 7;
		constraints.gridx = 0;
		constraints.gridwidth = 5;
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

		// Confirmation button
		constraints.gridy = 8;
		constraints.gridx = 3;
		constraints.gridwidth = 1;
		panel.add(confirmButton, constraints);

		add(panel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "Balanced":
			options = PrameterGroupFactory.create(PrameterGroupTypes.BALANCED);
		case "Light":
			options = PrameterGroupFactory.create(PrameterGroupTypes.LIGHT);
		case "Heavy":
			options = PrameterGroupFactory.create(PrameterGroupTypes.HEAVY);
		default:
			options = PrameterGroupFactory.create(PrameterGroupTypes.BALANCED);
		}
	}

	/**
	 * Governs the actions of the OK button.
	 * 
	 * @author Dean
	 *
	 */
	class ConfirmListener implements ActionListener {

		public ConfirmListener(JButton button) {
			button.setEnabled(true);
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			char[] pass = masterPass.getPassword();
			DatabaseManager newManager;
			if (!Arrays.equals(pass, masterPassRepeat.getPassword())) {
				MessageDisplay.textDisplay("Passwords dont't match. Please try again.");
				masterPass.setText("");
				masterPassRepeat.setText("");
			} else if (pass.length <= 15) {
				MessageDisplay.textDisplay("Passwords must be longer than 15 characters. Please try again.");
			} else if (!MangerSerializer.offerNewManager(
					pass,
					newManager = new DatabaseManager(pass, databaseName.getText(), databasePath.getText(), options),
					setupFrame)) {
				// This runs when we cannot serialize the the newManager object because of an IOException.
				newManager = null;
				MessageDisplay
				.textDisplay("An error occured. Perhaps that directory does not exist.");
			}  else {
				MessageDisplay.textDisplay("Succesfully created new manager!");
			}

			// Zero out the password field for security
			Arrays.fill(pass, '0');
		}
	}
	
	public static void createAndShow(JFrame startPanel) {
		// close the previous frame
		startPanel.dispose();
		
		// Create and set up the window.
		JFrame frame = new JFrame("Setup");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// Create and set up the content pane.
		JComponent newContentPane = new SetupPanel(frame);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	} 
}
