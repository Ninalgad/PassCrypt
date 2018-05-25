package gui;

import fileHandlers.DirectoryControl;
import fileHandlers.SerialFiles;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import gui.clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import manager.MangerSerializer;
import manager.DatabaseManager;
import manager.RandomString;

import java.awt.Image;


/**
 * Provides the GUI for encrypting and decrypting the database.
 * All input actions are interpreted here for a valid database. Outside of
 *  graphical elements, only the path to a valid database is stored.
 *  
 * @author user		Dean Ninalga
 * @version 		%I%, %G% 
 * @since			1.0
 */
public class InterfacePanel extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = -825543287948305318L;

	/* A */
	private DefaultListModel<String> listModel;
	private JList<String> list;

	private JLabel labelMasterPass = new JLabel("Master Password:");
	private JLabel newUsageLabel = new JLabel("New Password Usage:");
	private JLabel newPassLabel = new JLabel("New Password Entry:");

	private final JPasswordField masterPasswordField = new JPasswordField(30);

	JTextField newUsage = new JTextField(30);
	private JTextField newPass;

	private JButton deleteButton;
	private JButton buttonAutoGen;
	private JButton encodeButton;
	private JButton decodeButton;

	private String dbManagerPath;


	/**
	 * Places graphical elements on this window and makes available 
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
	public InterfacePanel(String managerPath) {
		super(new BorderLayout());
		this.dbManagerPath = managerPath;
		loadGrphics();
		loadPreviousElements();
	}

	/**
	 * Places the labels, buttons, button labels, text-areas and line
	 * separators on this panel. Buttons are then assigned to
	 * respective listeners.
	 * 
	 * @return	<code>null</code>
	 * @since	1.0
	 */
	private void loadGrphics() {
		listModel = new DefaultListModel<String>();
		// Create the list and put it in a scroll pane.
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(6);

		buttonAutoGen = new JButton("Auto-Generate");
		buttonAutoGen.addActionListener(new RandomStringListener());

		encodeButton = new JButton("Encode");
		EncodeListener encodeListener = new EncodeListener(encodeButton);
		encodeButton.setActionCommand("Encode");
		encodeButton.addActionListener(encodeListener);
		encodeButton.setEnabled(false);

		deleteButton = new JButton("Delete");
		deleteButton.setActionCommand("Delete");
		deleteButton.addActionListener(new DeleteListener());
		deleteButton.setEnabled(false);

		decodeButton = new JButton("Decode");
		decodeButton.setActionCommand("Decode");
		decodeButton.addActionListener(new DecodeListener(decodeButton));
		decodeButton.setEnabled(true);

		newPass = new JTextField(10);
		newPass.addActionListener(encodeListener);
		newPass.getDocument().addDocumentListener(encodeListener);

		// Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		buttonPane.add(decodeButton, constraints);
		constraints.gridx = 1;
		buttonPane.add(deleteButton, constraints);

		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		buttonPane.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		buttonPane.add(labelMasterPass, constraints);

		constraints.gridwidth = 4;
		constraints.gridx = 1;
		buttonPane.add(masterPasswordField, constraints);

		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		buttonPane.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

		constraints.gridy = 4;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		buttonPane.add(newUsageLabel, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		buttonPane.add(newUsage, constraints);

		constraints.gridy = 5;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		buttonPane.add(newPassLabel, constraints);
		buttonPane.add(Box.createHorizontalStrut(5));
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		buttonPane.add(newPass, constraints);

		constraints.gridy = 6;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		buttonPane.add(encodeButton, constraints);
		constraints.gridx = 1;
		buttonPane.add(buttonAutoGen, constraints);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane listScrollPane = new JScrollPane(list);
		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	}


	/**
	 * Renders a cryptographically strong random string of characters
	 * to the new password text area once the <code>buttonAutoGen</code>
	 * has been clicked.
	 * 
	 * 
	 * @author	Dean
	 * @since	1.0
	 */
	private class RandomStringListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int randomStringLen = 20;
			String rand = new RandomString(randomStringLen).nextString();
			newPass.setText(rand);
		}
	}


	/**
	 * DeleteListener listens for clicks on the <code>deleteButton</code>.
	 * Implements methods for deleting password named from the list
	 * on the graphical display and from the physical database. 
	 * 
	 * @author 	Dean N.
	 * @since	1.0
	 */
	private class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DatabaseManager manager = MangerSerializer.getManager(dbManagerPath);;
			if (manager.delete(list.getSelectedValue(), masterPasswordField.getPassword())) {
				deleteFromList();
				update(manager);
			}
		}

		private void deleteFromList() {
			int index = list.getSelectedIndex();

			listModel.remove(index);

			int size = listModel.getSize();

			if (size == 0) { 
				// Nobody's left, disable the button.
				deleteButton.setEnabled(false);

			} else { 
				// Select an index.
				if (index == listModel.getSize()) {
					// removed item in last position
					index--;
				}

				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		}
	}


	/**
	 * EncodeListener listens for clicks on the <code>encodeButton</code>,
	 * interacts with the database to encode a new password entry and updates
	 * the database and graphical display accordingly.
	 * 
	 * 
	 * @author 	Dean N.
	 * @since	1.0
	 */
	private class EncodeListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public EncodeListener(JButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			encodeNewPass();

			// Reset the relevant text fields.
			newUsage.requestFocusInWindow();
			newUsage.setText("");
			newPass.setText("");
			masterPasswordField.setText("");
		}

		/**
		 * Offers new password to the database manager then updates
		 * the graphical display and the database file accordingly.
		 * 
		 * @return	<code>null</code>
		 */
		private void encodeNewPass() {
			DatabaseManager manager = MangerSerializer.getManager(dbManagerPath);
			String passUsage = newUsage.getText();
			if (manager.offerNewEncrypted(passUsage, masterPasswordField.getPassword(),
					newPass.getText())) {
				updateList();
				update(manager);
			}
		}


		/**
		 * Update the display list with the registered text for 
		 * the usage of a newly encrypted password.
		 * 
		 * @return <code>null</code>
		 */
		private void updateList() {
			String name = newUsage.getText();

			// User didn't type in a unique name...
			if (name.equals("") || listModel.contains(name)) {
				Toolkit.getDefaultToolkit().beep();
				newUsage.requestFocusInWindow();
				newUsage.selectAll();
				return;
			}
			insertElement(newUsage.getText());
		}

		// Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		// Required by DocumentListener.
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		// Required by DocumentListener.
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	/**
	 * DecodeListener listens for clicks on the <code>decodeButton</code>,
	 * interacts with the database to decode a saved password entry.
	 * 
	 * @author 	Dean N.
	 * @since	1.0
	 */
	private class DecodeListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public DecodeListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			decodeNewPass();

			// Reset the relevant text fields.
			masterPasswordField.setText("");
			newUsage.requestFocusInWindow();
			newUsage.setText("");
			newPass.setText("");
		}

		/**
		 * Attempts to make the database manager to decode a database
		 * entry. The master-password must be typed in the relevant
		 * text area a successful decryption. If the decryption is
		 * successful then the entry is written on the system 
		 * clip-board to be pasted.
		 * 
		 * @return	<code>null</code>
		 * @since	1.0
		 */
		private void decodeNewPass() {
			DatabaseManager manager = MangerSerializer.getManager(dbManagerPath);
			// copy decoded text to clip-board
			String use = list.getSelectedValue();
			// If the master-password is wrong, getEncrypted returns null.
			String proposed = manager.getEncrypted(use, masterPasswordField.getPassword());
			if (proposed != null) {
				int copyTime = 10;
				clipboard.temporaryCopy(proposed, copyTime);
				MessageDisplay.textDisplay(
						"Succesfully decoded your password and it has been copied"
								+ " to your clip board for " + copyTime + " seconds."
						);
			}
		}

		// Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		// Required by DocumentListener.
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		// Required by DocumentListener.
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	/**
	 * Replace the saved database manager with an updated version,
	 * in the file system. 
	 * 
	 * @param updatedManager	the updated database manager
	 * 							to be serialized.
	 * @return					<code>null</code>
	 */
	private void update(DatabaseManager updatedManager) {
		DatabaseManager currentManager = MangerSerializer.getManager(dbManagerPath);
		DirectoryControl.deleteFilesInDir(currentManager.getDatabaseDirectory());
		SerialFiles.serializeObject(updatedManager, dbManagerPath);
	}

	/**
	 * Disables the delete button if the database is empty, otherwise
	 * it enables the button. This method is required by ListSelectionListener.
	 * 
	 * @param e	registered clicking actions
	 * @return	<code>null</code>
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				// No selection, disable decode button.
				deleteButton.setEnabled(false);

			} else {
				// Selection, enable the decode button.
				deleteButton.setEnabled(true);
			}
		}
	}

	/**
	 * Inserts the entry names in a previously saved database into 
	 * the current graphical list display.
	 * 
	 * @return	<code>null</code>
	 */
	private void loadPreviousElements() {
		DatabaseManager manager = MangerSerializer.getManager(dbManagerPath);
		for (String usage : manager.getUsages()) {
			insertElement(usage);
		}
	}

	/**
	 * Inserts text to the bottom of the current graphical list
	 * display.
	 * 
	 * @param element	the text to insert
	 */
	private void insertElement(String element) {
		int index = list.getSelectedIndex(); // get selected index
		if (index == -1) { // no selection, so insert at beginning
			index = 0;
		} else { // add after the selected item
			index++;
		}

		listModel.insertElementAt(element, index);

		// Select the new item and make it visible.
		list.setSelectedIndex(index);
		list.ensureIndexIsVisible(index);
	}

	public static void createAndShow(DatabaseManager man, JFrame setupFrame) {
		// close previous frame
		setupFrame.dispose();

		// Create and set up the window.
		JFrame frame = new JFrame("Cauchy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		Image im = Toolkit.getDefaultToolkit().getImage("icon_256.png");
		frame.setIconImage(im);

		// Create and set up the content pane.
		JComponent newContentPane = new InterfacePanel(man.getDatabasePath());
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}
