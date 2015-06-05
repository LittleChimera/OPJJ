package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.buttons.ExportAction;
import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectChooserGroup;
import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectCreatorButton;
import hr.fer.zemris.java.hw12.jvdraw.buttons.SaveAction;
import hr.fer.zemris.java.hw12.jvdraw.buttons.SaveAsAction;
import hr.fer.zemris.java.hw12.jvdraw.colors.JColorArea;
import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw12.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw12.jvdraw.objects.EmptyCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.FullCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw12.jvdraw.objects.LineDrawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * JVDraw is a drawing program which cna draw circles and lines. It can export
 * drawing in popular image formats and save or load existing drawing in .jvd
 * format.
 * 
 * @author Luka Skugor
 *
 */
public class JVDraw extends JFrame {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Program's drawing canvas.
	 */
	private JDrawingCanvas drawingCanvas;
	/**
	 * Model containing all drawn objects.
	 */
	private DrawingModel drawingModel = new DrawingModel();

	/**
	 * Picker for background color.
	 */
	private JColorArea background;
	/**
	 * Picker for foreground color.
	 */
	private JColorArea foreground;

	/**
	 * Button group for selecting a drawing tool.
	 */
	private ObjectChooserGroup objectChooserGroup;

	/**
	 * Program's top panel.
	 */
	private JPanel topPanel;

	/**
	 * Program's bottom panel.
	 */
	private JPanel bottomPanel;

	/**
	 * Creates a new JVDraw window.
	 */
	public JVDraw() {
		setTitle("JVDraw");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(onClosingListener);
		setSize(600, 400);
		initGUI();
	}

	/**
	 * Initializes program's GUI.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		drawingCanvas = new JDrawingCanvas(drawingModel);
		getContentPane().add(drawingCanvas, BorderLayout.CENTER);
		initDrawingCanvas();

		initObjectList();
		initColorAreas();
		addButtons();

		getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

		getContentPane().add(topPanel, BorderLayout.PAGE_START);
	}

	/**
	 * Initializes color pickers.
	 */
	private void initColorAreas() {
		background = new JColorArea(Color.WHITE, "background", this);
		JLabel backgroundColorText = new JLabel();
		bottomPanel.add(backgroundColorText);
		initColorArea(background, "background", backgroundColorText);

		foreground = new JColorArea(Color.BLACK, "foreground", this);
		JLabel foregroundColorText = new JLabel();
		bottomPanel.add(foregroundColorText);
		initColorArea(foreground, "foreground", foregroundColorText);

		topPanel.add(foreground);
		topPanel.add(background);
	}

	/**
	 * Checks if current drawing is saved and then exits. If drawing is not
	 * saved, user will prompted to save changes.
	 * 
	 * @return true if user confirmed exit, otherwise false
	 */
	private int exit() {
		if (saveAction.isModelModified()) {
			return JOptionPane
					.showConfirmDialog(
							this,
							"Drawing has been changed. Do you want to save the changes?",
							"Save before exit",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);
		}
		return JOptionPane.NO_OPTION;
	}

	/**
	 * Save drawing action.
	 */
	private SaveAction saveAction = new SaveAction(null, drawingModel, this);

	/**
	 * Load drawing action.
	 */
	private Action loadAction = new AbstractAction() {

		/**
		 * Serial
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"JVD", "jvd"));

			int r = fileChooser.showOpenDialog(JVDraw.this);
			if (r == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				List<String> lines = null;
				try {
					lines = Files.readAllLines(file.toPath());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(JVDraw.this,
							"Error while loading file: " + e1.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				drawingModel.setModel(DrawingModel.fromJvdFormat(lines));
				drawingCanvas.setDrawingModel(drawingModel);
				saveAction.update(file.toPath(), drawingModel);
				drawingCanvas.repaint();
			}
		}
	};

	/**
	 * On windows closing listener. Check if drawing has been saved by calling
	 * {@link #exit()}.
	 */
	private WindowListener onClosingListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			int exit = exit();
			switch (exit) {
			case JOptionPane.YES_OPTION:
				if (!saveAction.saveModel()) {
					break;
				}

			case JOptionPane.NO_OPTION:
				dispose();
				break;

			case JOptionPane.CANCEL_OPTION:
				return;
			}
		};
	};

	/**
	 * Adds buttons to the program's GUI.
	 */
	private void addButtons() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem saveButton = new JMenuItem(saveAction);
		saveButton.setText("Save");
		fileMenu.add(saveButton);

		JMenuItem saveAsButton = new JMenuItem(new SaveAsAction(drawingModel,
				this));
		saveAsButton.setText("Save As");
		fileMenu.add(saveAsButton);

		JMenuItem loadButton = new JMenuItem(loadAction);
		loadButton.setText("Load");
		fileMenu.add(loadButton);

		fileMenu.addSeparator();

		JMenuItem exportButton = new JMenuItem(new ExportAction(drawingModel,
				this));
		exportButton.setText("Export");
		fileMenu.add(exportButton);

		fileMenu.addSeparator();

		JMenuItem exit = new JMenuItem(new AbstractAction() {

			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				onClosingListener.windowClosing(null);
			}
		});
		exit.setText("Exit");
		fileMenu.add(exit);

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		topPanel.add(new JButton(new AbstractAction() {

			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;

			{
				putValue(Action.NAME, "Generate circle");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Random random = new Random();
				int x = Math.abs(random.nextInt() % getWidth());
				int y = Math.abs(random.nextInt() % getHeight());
				int r = 10 + Math.abs(random.nextInt()) % 50;
				drawingModel.addObject(new FullCircleDrawing(x, y, r,
						background.getCurrentColor(), foreground
								.getCurrentColor()));
			}
		}));

		for (ObjectCreatorButton creator : initObjectCreators()) {
			topPanel.add(creator);
		}
	}

	/**
	 * Initializes list which displays drawn objects.
	 */
	private void initObjectList() {
		JList<GeometricalObject> objectList = new JList<GeometricalObject>(
				new DrawingObjectListModel(drawingModel));

		JScrollPane objectListDecorator = new JScrollPane(objectList);
		objectListDecorator.setPreferredSize(new Dimension(150, 0));
		objectListDecorator.setBorder(BorderFactory
				.createRaisedSoftBevelBorder());

		getContentPane().add(objectListDecorator, BorderLayout.AFTER_LINE_ENDS);

		// set design
		objectList.setBackground(Color.DARK_GRAY);
		objectList.setForeground(Color.CYAN);

		objectList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = objectList.locationToIndex(e.getPoint());
					objectList.getModel().getElementAt(index)
							.showChangeDialog(JVDraw.this);
					DrawingModel.fireObjectsChanged(drawingModel, index, index);
				}
			}
		});

		objectList.setCellRenderer(new ListCellRenderer<GeometricalObject>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends GeometricalObject> list,
					GeometricalObject value, int index, boolean isSelected,
					boolean cellHasFocus) {

				DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
				JLabel label = (JLabel) defaultRenderer
						.getListCellRendererComponent(objectList, value, index,
								isSelected, cellHasFocus);

				DrawingObjectListModel model = (DrawingObjectListModel) list
						.getModel();
				int id = 0;

				for (int i = 0, count = model.getSize(); i < count; i++) {
					GeometricalObject current = model.getElementAt(i);
					if (current.getClass() == value.getClass()) {
						id++;
					}
					if (current == value) {
						break;
					}
				}

				label.setText(value.getName() + " " + id);
				return label;
			}
		});
	}

	/**
	 * Initializes drawing canvas.
	 */
	private void initDrawingCanvas() {
		drawingCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point start = drawingCanvas.getStartPoint();
				int ex = e.getX();
				int ey = e.getY();
				if (start != null) {
					int width = start.x - ex;
					int height = start.y - ey;
					drawingModel.addObject(objectChooserGroup
							.createSelectedObject(start.x, start.y, width,
									height, background.getCurrentColor(),
									foreground.getCurrentColor()));
					drawingCanvas.resetStartPoint();
				} else {
					drawingCanvas.setStartPoint(ex, ey);
				}
			}
		});

		drawingCanvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point start = drawingCanvas.getStartPoint();
				if (start != null) {
					int width = start.x - e.getX();
					int height = start.y - e.getY();
					drawingCanvas.paintDrawingObject(objectChooserGroup
							.createSelectedObject(start.x, start.y, width,
									height, background.getCurrentColor(),
									foreground.getCurrentColor()));
				}
			}
		});

	}

	/**
	 * Initializes a color picker.
	 * 
	 * @param colorArea
	 *            initializing color picker
	 * @param name
	 *            name of the color picker
	 * @param backgroundColorText
	 *            {@link JLabel} where area's color will be displayed as string
	 */
	private void initColorArea(JColorArea colorArea, String name,
			JLabel backgroundColorText) {
		backgroundColorText
				.setText(name + " color: " + colorArea.rgbToString());
		colorArea.addColorChangeListener((src, oldC, newC) -> {
			if (newC != null && !newC.equals(oldC)) {
				drawingCanvas.setBackground(newC);
				backgroundColorText.setText(name + " color: "
						+ colorArea.rgbToString());
			}
		});
	}

	/**
	 * Initializes tools for creating drawings.
	 * 
	 * @return array of buttons which activate tools
	 */
	private ObjectCreatorButton[] initObjectCreators() {
		ObjectCreatorButton[] buttons = new ObjectCreatorButton[3];
		// FullCircleDrawing creator
		buttons[0] = new ObjectCreatorButton() {
			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new FullCircleDrawing(x, y, (int) Math.sqrt(height
						* height + width * width),
						foreground.getCurrentColor(),
						background.getCurrentColor());
			}
		};
		buttons[0].setText("Full Circle");

		buttons[1] = new ObjectCreatorButton() {
			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new EmptyCircleDrawing(x, y, (int) Math.sqrt(height
						* height + width * width), foreground.getCurrentColor());
			}
		};
		buttons[1].setText("Empty Circle");

		buttons[2] = new ObjectCreatorButton() {
			/**
			 * Serial
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new LineDrawing(x, y, x - width, y - height,
						foreground.getCurrentColor());
			}
		};
		buttons[2].setText("Line");

		objectChooserGroup = new ObjectChooserGroup(buttons);
		return buttons;
	}

	/**
	 * Called on program start. Creates a single JVDraw window.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {

			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception ignorable) {
			}

			JFrame frame = new JVDraw();
			frame.setVisible(true);
		});
	}

}
