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
 * The user interface for the manager system. Implemented using Swing.
 * 
 */
public class InterfacePanel extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = -825543287948305318L;

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
	 * Constructs the  system. Requires the existence of an manager on local desktop.
	 * 
	 */
	public InterfacePanel(String managerPath) {
		super(new BorderLayout());
		this.dbManagerPath = managerPath;
		run();
	}
	
	public InterfacePanel(DatabaseManager man) {
		super(new BorderLayout());
		dbManagerPath = man.getDatabasePath();
		run();
	}
	
	private void run() {
		listModel = new DefaultListModel<String>();
		// Create the list and put it in a scroll pane.
		list = new JList<String>(listModel);
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(6);

		buttonAutoGen = new JButton("Auto-Generate");

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
		DecodeListener decodeListener = new DecodeListener(decodeButton);
		decodeButton.setActionCommand("Decode");
		decodeButton.addActionListener(decodeListener);
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
		buttonAutoGen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int randomStringLen = 20;
				String rand = new RandomString(randomStringLen).nextString();
				newPass.setText(rand);
			}
		});
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane listScrollPane = new JScrollPane(list);
		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
		// setPreferredSize(new Dimension(300, 340));
		loadPreviousElements();
	}

	class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DatabaseManager manager = null;
			boolean manCheck = true;
			try {
				manager = MangerSerializer.getManager(dbManagerPath);
			} catch (Exception ex) {
				ex.printStackTrace();
				manCheck = false;
			}
			if (manCheck) {
				if (manager.delete(list.getSelectedValue(), masterPasswordField.getPassword())) {
					deleteFromList();
					update(manager);
				}
			}
		}

		private void deleteFromList() {
			// This method can be called only if
			// there's a valid selection
			// so go ahead and remove whatever's selected.
			int index = list.getSelectedIndex();

			listModel.remove(index);

			int size = listModel.getSize();

			if (size == 0) { // Nobody's left, disable the button.
				deleteButton.setEnabled(false);

			} else { // Select an index.
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
	 * Governs the actions of the Encode button.
	 * 
	 * @author Dean
	 *
	 */
	class EncodeListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public EncodeListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			encodeNewPass();

			// Reset the text field.
			newUsage.requestFocusInWindow();
			newUsage.setText("");
			newPass.setText("");
			masterPasswordField.setText("");
		}

		private void encodeNewPass() {
			DatabaseManager manager = null;
			boolean manCheck = true;
			try {
				manager = MangerSerializer.getManager(dbManagerPath);
			} catch (Exception ex) {
				ex.printStackTrace();
				manCheck = false;
			}
			if (manCheck) {
				String passUsage = newUsage.getText();
				if (manager.offerNewEncrypted(passUsage, masterPasswordField.getPassword(),
						newPass.getText())) {
					updateList();
					update(manager);
				}
			}
		}

		private void updateList() {
			String name = newUsage.getText();

			// User didn't type in a unique name...
			if (name.equals("") || alreadyInList(name)) {
				Toolkit.getDefaultToolkit().beep();
				newUsage.requestFocusInWindow();
				newUsage.selectAll();
				return;
			}
			insertElement(newUsage.getText());
		}

		// This method tests for string equality. You could certainly
		// get more sophisticated about the algorithm. For example,
		// you might want to ignore white space and capitalization.
		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
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

	class DecodeListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public DecodeListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			decodeNewPass();

			// Reset the text fields.
			masterPasswordField.setText("");
			newUsage.requestFocusInWindow();
			newUsage.setText("");
			newPass.setText("");
		}

		private void decodeNewPass() {
			DatabaseManager manager = null;
			boolean manCheck = true;
			try {
				manager = MangerSerializer.getManager(dbManagerPath);
			} catch (Exception ex) {
				ex.printStackTrace();
				manCheck = false;
			}
			if (manCheck) {
				// copy decoded text to clip-board
				String use = list.getSelectedValue();
				String proposed = manager.getEncrypted(use, masterPasswordField.getPassword());
				if (proposed != null) {
					int copyTime = 10;
					clipboard.temporaryCopy(proposed, copyTime);
					MessageDisplay.textDisplay(
							"Succesfully decoded your password and it has been copied to your clip board for " + copyTime + " seconds.");
				}
			}
		}

		// This method tests for string equality. You could certainly
		// get more sophisticated about the algorithm. For example,
		// you might want to ignore white space and capitalization.
		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
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

	private void update(DatabaseManager updatedManager) {
		DatabaseManager currentManager = MangerSerializer.getManager(dbManagerPath);
		DirectoryControl.deleteFilesInDir(currentManager.getDatabaseDirectory());
		SerialFiles.serializeObject(updatedManager, dbManagerPath);
	}

	/**
	 * Enable or disable the button if a selection is possible. This method is required by
	 * ListSelectionListener.
	 * 
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

	private void loadPreviousElements() {
		DatabaseManager manager = null;
		boolean manCheck = true;
		try {
			manager = MangerSerializer.getManager(dbManagerPath);
		} catch (Exception ex) {
			ex.printStackTrace();
			manCheck = false;
		}
		if (manCheck) {
			for (String usage : manager.getUsages()) {
				insertElement(usage);
			}
		}
	}

	private void insertElement(String element) {
		int index = list.getSelectedIndex(); // get selected index
		if (index == -1) { // no selection, so insert at beginning
			index = 0;
		} else { // add after the selected item
			index++;
		}

		listModel.insertElementAt(element, index);
		// If we just wanted to add to the end, we'd do this:
		// listModel.addElement(newPass.getText());

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
