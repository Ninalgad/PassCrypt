package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import manager.MangerSerializer;


public class StartUpPanel extends JPanel 
implements ActionListener{
	private JFrame setupFrame;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel startUpMessage = new JLabel(Main.startUpMessage);

	private JButton loadButton = new JButton("load");
	private JButton newButton = new JButton("new");

	JFileChooser fc;

	public StartUpPanel(JFrame frame) {
		super(new BorderLayout());
		this.setupFrame = frame;

		// Set-up file chooser
		fc = new JFileChooser();

		// Set-up new database button
		newButton.setActionCommand("New");
		newButton.addActionListener(this);

		// Set-up load database button
		loadButton.setActionCommand("Load");
		loadButton.addActionListener(this);


		// Create a panel that uses GridBagLayout.
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		panel.add(startUpMessage, constraints);
		
		// Layout buttons
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(newButton, constraints);
		constraints.gridx = 1;
		constraints.gridwidth = 1;
		panel.add(loadButton, constraints);


		add(panel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filePath = fc.getSelectedFile().getAbsolutePath();
				if (MangerSerializer.isManager(filePath)) {
					InterfacePanel.createAndShow(
							MangerSerializer.getManager(filePath),
							setupFrame
							);
				} else {
					MessageDisplay.textDisplay(filePath + "\n Does not seem to point to a file that PassCrypt can"
							+ "understand.");
				}
			}
		} else if (e.getSource() == newButton) {
			SetupPanel.createAndShow(this.setupFrame);
		}
	}


	public static void createAndShow() {
		// Create and set up the window and center it.
		JFrame frame = new JFrame("Setup");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Create and set up the content pane.
		JComponent newContentPane = new StartUpPanel(frame);
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}
