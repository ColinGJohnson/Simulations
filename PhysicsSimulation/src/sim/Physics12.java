package sim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JToggleButton;
import java.awt.GridLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.awt.Panel;

public class Physics12{
	
	GraphicsJPanel graphicsPanel;
	Simulation simulation;
	private int zoomAmount = 5; // amount to zoom in or out per button press
	private JFrame frmPhysics;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Physics12 window = new Physics12();
					window.frmPhysics.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} // main

	/**
	 * Create the application.
	 */
	public Physics12() {
		initialize();
	} // Physics12 constructor

	/**
	 * Initialize the contents of the frame and add event listeners
	 */
	private void initialize() {
		// create simulation environment
		simulation = new Simulation(new Dimension(20, 20));
		System.out.println("Simulation environment created");

		// create GUI
		frmPhysics = new JFrame();
		frmPhysics.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\colin\\Desktop\\icon.png"));
		frmPhysics.setTitle("Physics Sim V3");
		frmPhysics.setBounds(500, 200, 650, 650);
		frmPhysics.setMinimumSize(new Dimension(550, 400));
		frmPhysics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// graphicsJpanel defined early so it may be changed by tools
		graphicsPanel = new GraphicsJPanel(20, 20, 20, simulation);

		JPanel toolControls = new JPanel();
		frmPhysics.getContentPane().add(toolControls, BorderLayout.NORTH);
		toolControls.setLayout(new BoxLayout(toolControls, BoxLayout.Y_AXIS));

		JPanel dynamicObjectControls = new JPanel();
		dynamicObjectControls.setBorder(new EmptyBorder(6, 0, 7, 0));
		toolControls.add(dynamicObjectControls);
		dynamicObjectControls.setLayout(new GridLayout(0, 8, 5, 5));

		JLabel lblMasskg = new JLabel("Mass (kg):");
		lblMasskg.setHorizontalAlignment(SwingConstants.RIGHT);
		dynamicObjectControls.add(lblMasskg);

		JFormattedTextField formattedTextFieldMass = new JFormattedTextField();
		formattedTextFieldMass.setText("10");
		dynamicObjectControls.add(formattedTextFieldMass);

		JLabel label_1 = new JLabel("Name:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		dynamicObjectControls.add(label_1);

		JFormattedTextField frmtdtxtfldObjectName = new JFormattedTextField();
		frmtdtxtfldObjectName.setText("~Object");
		dynamicObjectControls.add(frmtdtxtfldObjectName);

		JLabel lblSizem = new JLabel("Size(m):");
		lblSizem.setHorizontalAlignment(SwingConstants.RIGHT);
		dynamicObjectControls.add(lblSizem);

		JFormattedTextField formattedTextFieldObjectSize = new JFormattedTextField();
		formattedTextFieldObjectSize.setText("2");
		dynamicObjectControls.add(formattedTextFieldObjectSize);

		JComboBox<String> comboBoxObjectShape = new JComboBox<String>();
		comboBoxObjectShape.addItem("Circle");
		comboBoxObjectShape.addItem("Square");

		JLabel lblShape = new JLabel("Shape:");
		lblShape.setHorizontalAlignment(SwingConstants.RIGHT);
		dynamicObjectControls.add(lblShape);
		dynamicObjectControls.add(comboBoxObjectShape);
		dynamicObjectControls.setVisible(false);

		JPanel zoomControls = new JPanel();
		toolControls.add(zoomControls);
		zoomControls.setVisible(false);

		// define zoom amount field before other zoom controls so it can update
		JFormattedTextField formattedTextFieldGrid = new JFormattedTextField();

		JButton buttonZoomIn = new JButton("+");
		buttonZoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.setGridScale(graphicsPanel.getGridScale() + zoomAmount);
				formattedTextFieldGrid.setValue("" + graphicsPanel.getGridScale());
			}
		});
		zoomControls.add(buttonZoomIn);

		JButton buttonZoomOut = new JButton("-");
		buttonZoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.setGridScale(graphicsPanel.getGridScale() - zoomAmount);
				formattedTextFieldGrid.setValue("" + graphicsPanel.getGridScale());
			}
		});
		zoomControls.add(buttonZoomOut);

		JLabel lblZoomAmount = new JLabel("Zoom Amount:");
		zoomControls.add(lblZoomAmount);

		JSpinner zoomAmountSpinner = new JSpinner();
		zoomAmountSpinner.setModel(new SpinnerNumberModel(5, 0, 1000, 1));
		zoomAmountSpinner.setToolTipText("Amount To Zoom Per Click");
		zoomAmountSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				zoomAmount = (int) zoomAmountSpinner.getValue();
			}
		});
		zoomControls.add(zoomAmountSpinner);

		JLabel lblGrid = new JLabel("Grid Scale (Px/Square):");
		zoomControls.add(lblGrid);

		zoomControls.add(formattedTextFieldGrid);
		formattedTextFieldGrid.setText("20");
		formattedTextFieldGrid.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateGridScale();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateGridScale();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateGridScale();
			}

			public void updateGridScale() {
				if (formattedTextFieldGrid.getText() != null && !formattedTextFieldGrid.getText().isEmpty()) {
					graphicsPanel.setGridScale(Integer.parseInt(formattedTextFieldGrid.getText()));
				}
			}
		});

		formattedTextFieldGrid.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

				// resolve unacceptable values when user is done editing field
				if (formattedTextFieldGrid.getText().isEmpty() || formattedTextFieldGrid == null) {
					formattedTextFieldGrid.setText("20");
					graphicsPanel.setGridScale(20);
				} else if (Integer.parseInt(formattedTextFieldGrid.getText()) <= 2) {
					formattedTextFieldGrid.setText("3");
					graphicsPanel.setGridScale(3);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		JButton btnResetZoom = new JButton("Reset Zoom");
		btnResetZoom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				zoomAmount = 5;
				zoomAmountSpinner.setValue(5);

				formattedTextFieldGrid.setText("20");
			}
		});
		zoomControls.add(btnResetZoom);

		// Pan Controls
		Panel panControls = new Panel();
		toolControls.add(panControls);
		panControls.setVisible(false);

		JButton panLeft = new JButton("\u25C4");
		panControls.add(panLeft);

		JButton panDown = new JButton("\u25BC");
		panControls.add(panDown);

		JButton panUp = new JButton("\u25B2");
		panControls.add(panUp);

		JButton panRight = new JButton("\u25BA");
		panControls.add(panRight);

		// Delete Controls
		JPanel deleteControls = new JPanel();
		toolControls.add(deleteControls);

		JButton btnDeleteAll = new JButton("Delete All");
		deleteControls.add(btnDeleteAll);
		deleteControls.setVisible(false);
		
		JButton btnDeleteSelected = new JButton("Delete Selected");
		deleteControls.add(btnDeleteSelected);
		deleteControls.setVisible(false);

		// Select Controls
		JPanel selectControls = new JPanel();
		toolControls.add(selectControls);

		JButton btnSelectAll = new JButton("Select All");
		btnSelectAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.selectAll();
			} // actionPerformed
		});
		selectControls.add(btnSelectAll);
		selectControls.setVisible(true);
		
		JButton btnDeselectAll = new JButton("Select None");
		btnDeselectAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.selectNone();
			} // actionPerformed
		});
		selectControls.add(btnDeselectAll);
		selectControls.setVisible(true);

		// define scroll pane early to update it for zoomed graphicsPanel
		JScrollPane scrollPane = new JScrollPane();

		// scroll to zoom
		graphicsPanel.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (graphicsPanel.getGridScale() > 2 || e.getWheelRotation() < 0) {
					graphicsPanel.setGridScale(graphicsPanel.getGridScale() + e.getWheelRotation() * -2);
					formattedTextFieldGrid.setValue("" + graphicsPanel.getGridScale());
					scrollPane.setViewportView(graphicsPanel);
				}
			} // mouseWheel Moved
		});

		JPanel bottomControls = new JPanel();
		bottomControls.setBorder(new EmptyBorder(5, 5, 5, 5));
		frmPhysics.getContentPane().add(bottomControls, BorderLayout.SOUTH);
		bottomControls.setLayout(new BoxLayout(bottomControls, BoxLayout.Y_AXIS));

		JPanel AnimationControl = new JPanel();
		bottomControls.add(AnimationControl);
		AnimationControl.setLayout(new GridLayout(0, 4, 0, 0));

		// Reset Button
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frmPhysics, "Reset Objects to their initial positions?", "Confirm",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					simulation.reset();
				} else {
					System.out.println("Reset Cancelled");
				}
			}
		});
		AnimationControl.add(btnReset);

		// Start Button
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.start();
			}
		});
		AnimationControl.add(btnStart);

		// Pause Button
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.pause();
			}
		});
		AnimationControl.add(btnPause);

		// "New" Button
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showOptionDialog(frmPhysics, "Clear simulation and start again?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null,
						JOptionPane.UNINITIALIZED_VALUE);

				// remove all dynamic objects on user confirmation
				if (choice == JOptionPane.YES_OPTION) {
					simulation.clearDynamicObjects();
				}
			}
		});
		AnimationControl.add(btnNew);

		JPanel GeneralProperties = new JPanel();
		bottomControls.add(GeneralProperties);
		GeneralProperties.setLayout(new GridLayout(2, 2, 0, 0));

		JPanel EdgeBehavior = new JPanel();
		GeneralProperties.add(EdgeBehavior);
		EdgeBehavior.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JRadioButton rdbtnStop = new JRadioButton("Stop");
		buttonGroup_1.add(rdbtnStop);
		EdgeBehavior.add(rdbtnStop);
		rdbtnStop.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				simulation.setEdgeBehavior(3);
			}
		});

		JRadioButton rdbtnBounce = new JRadioButton("Bounce");
		buttonGroup_1.add(rdbtnBounce);
		EdgeBehavior.add(rdbtnBounce);
		rdbtnBounce.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				simulation.setEdgeBehavior(1);
			}
		});

		JRadioButton rdbtnWrap = new JRadioButton("Wrap");
		buttonGroup_1.add(rdbtnWrap);
		EdgeBehavior.add(rdbtnWrap);
		rdbtnWrap.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				simulation.setEdgeBehavior(2);
			}
		});

		JRadioButton rdbtnRemove = new JRadioButton("Remove");
		rdbtnRemove.setSelected(true);
		buttonGroup_1.add(rdbtnRemove);
		EdgeBehavior.add(rdbtnRemove);
		rdbtnRemove.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				simulation.setEdgeBehavior(0);
			}
		});

		JPanel GravityOptions = new JPanel();
		GeneralProperties.add(GravityOptions);

		JCheckBox chckbxEnableGravity = new JCheckBox("Enable Gravity");
		GravityOptions.add(chckbxEnableGravity);

		JCheckBox chckbxGlobalGravity = new JCheckBox("Global Gravity:");
		chckbxGlobalGravity.setSelected(true);
		GravityOptions.add(chckbxGlobalGravity);

		JFormattedTextField frmtdtxtfldGlobalGravity = new JFormattedTextField();
		frmtdtxtfldGlobalGravity.setName("");
		frmtdtxtfldGlobalGravity.setToolTipText("Global Gravity Constant (m/s)");
		frmtdtxtfldGlobalGravity.setText("9.8");
		GravityOptions.add(frmtdtxtfldGlobalGravity);

		JFormattedTextField formattedTextFieldGravDirection = new JFormattedTextField();
		formattedTextFieldGravDirection.setText("90");
		formattedTextFieldGravDirection.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				gravDirectionChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				gravDirectionChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				gravDirectionChanged();
			}

			public void gravDirectionChanged() {
				if (formattedTextFieldGravDirection.getText() != null
						&& !formattedTextFieldGravDirection.getText().isEmpty()) {
					simulation.setGlobalGravAngle(Double.parseDouble(formattedTextFieldGravDirection.getText()));
				}
			}
		});
		GravityOptions.add(formattedTextFieldGravDirection);

		JPanel Graphsettings = new JPanel();
		GeneralProperties.add(Graphsettings);

		JFormattedTextField formattedTextFieldGraphScale = new JFormattedTextField();
		formattedTextFieldGraphScale.setText("1");
		Graphsettings.add(formattedTextFieldGraphScale);

		JLabel lblXLength = new JLabel("X (m):");
		lblXLength.setHorizontalAlignment(SwingConstants.RIGHT);
		Graphsettings.add(lblXLength);

		JFormattedTextField formattedTextFieldXSize = new JFormattedTextField();
		formattedTextFieldXSize.setHorizontalAlignment(SwingConstants.LEFT);
		formattedTextFieldXSize.setText("20");
		formattedTextFieldXSize.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				graphicsPanel.setGridWidth(Integer.parseInt(formattedTextFieldXSize.getText()));
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Graphsettings.add(formattedTextFieldXSize);

		JLabel lblY = new JLabel("Y (m):");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		Graphsettings.add(lblY);

		JFormattedTextField formattedTextFieldYSize = new JFormattedTextField();
		formattedTextFieldYSize.setText("20");
		formattedTextFieldYSize.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				graphicsPanel.setGridHeight(Integer.parseInt(formattedTextFieldYSize.getText()));
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Graphsettings.add(formattedTextFieldYSize);

		JPanel tools = new JPanel();
		tools.setBorder(new EmptyBorder(0, 5, 0, 0));
		frmPhysics.getContentPane().add(tools, BorderLayout.WEST);
		tools.setLayout(new GridLayout(0, 1, 0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		tools.add(toolBar);

		JToggleButton tglbtnSelect = new JToggleButton("Select");
		tglbtnSelect.setSelected(true);
		tglbtnSelect.setToolTipText("Select (S)");
		buttonGroup.add(tglbtnSelect);
		tglbtnSelect.setFocusPainted(false);
		tglbtnSelect.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectControls.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					selectControls.setVisible(false);
				}
			}
		});
		toolBar.add(tglbtnSelect);

		JToggleButton tglbtnDelete = new JToggleButton("Delete");
		tglbtnDelete.setToolTipText("Delete (D)");
		buttonGroup.add(tglbtnDelete);
		tglbtnDelete.setFocusPainted(false);
		tglbtnDelete.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					deleteControls.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					deleteControls.setVisible(false);
				}
			}
		});
		toolBar.add(tglbtnDelete);

		JToggleButton tglbtnObj = new JToggleButton("Place");
		buttonGroup.add(tglbtnObj);
		tglbtnObj.setFocusPainted(false);
		tglbtnObj.setToolTipText("Place (O)");
		tglbtnObj.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					dynamicObjectControls.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					dynamicObjectControls.setVisible(false);
				}

			}
		});
		toolBar.add(tglbtnObj);

		JToggleButton btnZoom = new JToggleButton("Zoom ");
		buttonGroup.add(btnZoom);
		btnZoom.setFocusPainted(false);
		btnZoom.setToolTipText("Zoom In or Out (Z)");
		btnZoom.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					zoomControls.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					zoomControls.setVisible(false);
				}
			}
		});
		toolBar.add(btnZoom);

		JToggleButton tglbtnPan = new JToggleButton("Pan");
		buttonGroup.add(tglbtnPan);
		tglbtnPan.setFocusPainted(false);
		tglbtnPan.setToolTipText("Pan Viewpane (P)");
		tglbtnPan.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panControls.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					panControls.setVisible(false);
				}
			}
		});
		toolBar.add(tglbtnPan);

		JPanel graphicsPanelContainer = new JPanel();
		graphicsPanelContainer.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frmPhysics.getContentPane().add(graphicsPanelContainer, BorderLayout.CENTER);
		graphicsPanelContainer.setLayout(new BorderLayout(0, 0));

		graphicsPanelContainer.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(graphicsPanel);

		JMenuBar menuBar = new JMenuBar();
		frmPhysics.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnFile.add(mntmNew);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setPreferredSize(new Dimension(180, (int) mnFile.getPreferredSize().getHeight()));
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnOpen = new JMenu("Open");
		mnFile.add(mnOpen);

		JMenu mnPresets = new JMenu("Presets");
		mnOpen.add(mnPresets);

		JMenuItem mntmPreset = new JMenuItem("Preset #1");
		mnPresets.add(mntmPreset);

		JMenuItem mntmpsimFile = new JMenuItem(".psim File");
		mntmpsimFile.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnOpen.add(mntmpsimFile);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnProperties = new JMenu("Properties");
		mnProperties.setPreferredSize(new Dimension(180, (int) mnProperties.getPreferredSize().getHeight()));
		mnEdit.add(mnProperties);

		JMenu mnGravity = new JMenu("Gravity");
		mnProperties.add(mnGravity);

		JRadioButtonMenuItem rdbtnmntmEnableGlobalGravity = new JRadioButtonMenuItem("Enable Global Gravity");
		mnGravity.add(rdbtnmntmEnableGlobalGravity);

		JMenu mnObjects = new JMenu("Objects");
		mnEdit.add(mnObjects);

		JMenuItem mntmStopObjects = new JMenuItem("Stop Objects");
		mntmStopObjects.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.stopDynamicObjects();
			}
		});
		mnObjects.add(mntmStopObjects);

		JSeparator separator = new JSeparator();
		mnEdit.add(separator);

		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.setEnabled(false);
		mnEdit.add(mntmPaste);

		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.setEnabled(false);
		mnEdit.add(mntmCopy);

		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.setEnabled(false);
		mnEdit.add(mntmCut);

		JMenu mnSelect = new JMenu("Select");
		menuBar.add(mnSelect);
		
		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mnSelect.add(mntmSelectAll);
		
		JMenuItem mntmSelectNone = new JMenuItem("Select None");
		mnSelect.add(mntmSelectNone);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);

		JRadioButtonMenuItem rdbtnmntmShowCartesianPlane = new JRadioButtonMenuItem("Show Cartesian Plane");
		rdbtnmntmShowCartesianPlane
				.setPreferredSize(new Dimension(180, (int) rdbtnmntmShowCartesianPlane.getPreferredSize().getHeight()));
		mnView.add(rdbtnmntmShowCartesianPlane);

		JRadioButtonMenuItem rdbtnmntmDrawScaleNumbers = new JRadioButtonMenuItem("Draw Scale Numbers");
		mnView.add(rdbtnmntmDrawScaleNumbers);

		JRadioButtonMenuItem rdbtnmntmDrawObjectInfo = new JRadioButtonMenuItem("Draw Object Info");
		mnView.add(rdbtnmntmDrawObjectInfo);

		JRadioButtonMenuItem rdbtnmntmDrawMovementTrails = new JRadioButtonMenuItem("Draw Movement Trails");
		mnView.add(rdbtnmntmDrawMovementTrails);

		JRadioButtonMenuItem rdbtnmntmDrawVelocityVectors = new JRadioButtonMenuItem("Draw Velocity Vectors");
		mnView.add(rdbtnmntmDrawVelocityVectors);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmViewHelp = new JMenuItem("View Help");

		mntmViewHelp.setEnabled(false);
		mntmViewHelp.setPreferredSize(new Dimension(180, (int) mntmViewHelp.getPreferredSize().getHeight()));
		mnHelp.add(mntmViewHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmPhysics,
						"A basic physics simulator.\n<html><i>Created By Colin Johnson, November 2016</html></i>",
						"About", JOptionPane.DEFAULT_OPTION);
			}
		});
		mnHelp.add(mntmAbout);
		
		System.out.println("GUI initialized");

		// Event Listeners for dragging to select
		graphicsPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				graphicsPanel.updateMousePosition(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (tglbtnSelect.isSelected()) {
					// TODO click to select
				}
			}
		});

		// Event Listeners for placing objects
		graphicsPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			} // mousePressed

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (tglbtnObj.isSelected()) {
					if (e.getPoint().getX() < graphicsPanel.getGridWidth() * graphicsPanel.getGridScale()
							&& e.getPoint().getY() < graphicsPanel.getGridHeight() * graphicsPanel.getGridScale()) {

						// convert mouse location point to graph coordinate
						simulation.addDynamicObject(((double) (e.getX())) / graphicsPanel.getGridScale(),
								((double) (e.getY())) / graphicsPanel.getGridScale(),
								Integer.parseInt(formattedTextFieldMass.getText()),
								Double.parseDouble(formattedTextFieldObjectSize.getText()), Color.BLUE,
								frmtdtxtfldObjectName.getText());
						graphicsPanel.repaint();

					} else {
						System.err.println("Object out of bounds!");
					}
				} else if (tglbtnSelect.isSelected()) {
					// TODO click to select
				}
			} // mouseClicked
		});
		System.out.println("Place tool event listeners initialized");
	} // initialize
} // physics 12
