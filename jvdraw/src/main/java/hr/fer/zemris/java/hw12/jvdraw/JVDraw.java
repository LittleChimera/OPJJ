package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.buttons.ExportAction;
import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectChooserGroup;
import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectCreatorButton;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JVDraw extends JFrame {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	private JDrawingCanvas drawingCanvas;
	private DrawingModel drawingModel;

	private JColorArea background;
	private JColorArea foreground;

	private ObjectChooserGroup objectChooserGroup;

	public JVDraw() {
		setTitle("JVDraw");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		drawingModel = new DrawingModel();
		drawingCanvas = new JDrawingCanvas(drawingModel);
		getContentPane().add(drawingCanvas, BorderLayout.CENTER);
		initDrawingCanvas();

		JList<GeometricalObject> objectList = new JList<GeometricalObject>(
				new DrawingObjectListModel(drawingModel));
		initObjectList(objectList);
		JScrollPane objectListDecorator = new JScrollPane(objectList);
		objectListDecorator.setPreferredSize(new Dimension(100, 0));
		objectListDecorator.setBorder(BorderFactory
				.createRaisedSoftBevelBorder());

		getContentPane().add(objectListDecorator, BorderLayout.AFTER_LINE_ENDS);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		background = new JColorArea(Color.WHITE, "background", this);
		JLabel backgroundColorText = new JLabel();
		bottomPanel.add(backgroundColorText);
		initColorArea(background, "background", backgroundColorText);

		foreground = new JColorArea(Color.BLACK, "foreground", this);
		JLabel foregroundColorText = new JLabel();
		bottomPanel.add(foregroundColorText);
		initColorArea(foreground, "foreground", foregroundColorText);
		
		getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

		topPanel.add(foreground);
		topPanel.add(background);
		JButton exportButton = new JButton(new ExportAction(drawingModel, this));
		exportButton.setText("Export");
		topPanel.add(exportButton);
		topPanel.add(new JButton(new AbstractAction() {

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

		getContentPane().add(topPanel, BorderLayout.PAGE_START);
	}

	private void initObjectList(JList<GeometricalObject> objectList) {
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
	}

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

	private void initColorArea(JColorArea colorArea, String name, JLabel backgroundColorText) {
		backgroundColorText.setText(name + " color: " + colorArea.rgbToString());
		colorArea.addColorChangeListener((src, oldC, newC) -> {
			if (newC != null && !newC.equals(oldC)) {
				drawingCanvas.setBackground(newC);
				backgroundColorText.setText(name + " color: " + colorArea.rgbToString());
			}
		});
	}

	private ObjectCreatorButton[] initObjectCreators() {
		ObjectCreatorButton[] buttons = new ObjectCreatorButton[3];
		// FullCircleDrawing creator
		buttons[0] = new ObjectCreatorButton() {
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
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new EmptyCircleDrawing(x, y, (int) Math.sqrt(height
						* height + width * width), foreground.getCurrentColor());
			}
		};
		buttons[1].setText("Empty Circle");

		buttons[2] = new ObjectCreatorButton() {
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
