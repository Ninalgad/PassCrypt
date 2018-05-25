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


/**
 * Provides the GUI for creating a new password manager/database.
 * All input actions are interpreted here for a valid database. Outside of
 *  graphical elements, only the path to a valid database is stored.
 *  
 * @author user		Dean Ninalga
 * @version 		%I%, %G% 
 * @since			1.0
 */
public class SetupPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Set Up";

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

	/**
	 * Places graphical elements on this panel and makes available 
	 * previously saved passwords from the database located
	 * at the given path.
	 * <p>
	 * This assumes that a valid database file exists at the given
	 * path.
	 * 
	 * @param managerPath	path to a valid database
	 * @return				<code>null</code>
	 * @since				1.0
	 */
	public SetupPanel() {
		super(new BorderLayout());

		// Set-up confirmation button
		ConfirmListener listener = new ConfirmListener();
		confirmButton.setActionCommand("Confirm");
		confirmButton.addActionListener(listener);
		confirmButton.setEnabled(true);

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
	
	
	/**
	 * Chooses the encyption/hashing parameters for
	 * a new database manager based on what the user selects on
	 * this window.
	 * 
	 * @author Dean N.
	 * {@inheritDoc}
	 */
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
	 * Provides the functionality of the confirmation button. Using the registered
	 * text in the respective text fields this ensures the data entries are valid
	 * once the confirmation button is clicked.
	 * 
	 * @author	Dean N.
	 * @since 	1.0
	 */
	class ConfirmListener implements ActionListener {

		/**
		 * Ensures that the master-password is sufficiently secure and 
		 * that a new data can be saved at the user selected location
		 * with the provided name.
		 * 
		 * {@inheritDoc}
		 * @author 	Dean N.
		 * @since	1.0
		 */
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
}
