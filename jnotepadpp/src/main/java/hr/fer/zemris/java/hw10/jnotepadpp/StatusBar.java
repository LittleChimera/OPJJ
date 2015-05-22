package hr.fer.zemris.java.hw10.jnotepadpp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.Timer;

/**
 * Status bar of the JNotepadPP. It display length, line, column, selected text
 * length of active document and displays real time in format 2015/05/15
 * 15:35:24.
 * 
 * @author Luka Skugor
 *
 */
public class StatusBar extends JToolBar {

	/**
	 * Label displaying length of active document.
	 */
	private JLabelValue length;
	/**
	 * Label displaying line of caret position in active document.
	 */
	private JLabelValue caretLine;
	/**
	 * Label displaying column of caret position in active document.
	 */
	private JLabelValue caretColumn;
	/**
	 * Label displaying length of selected text in active document.
	 */
	private JLabelValue selected;
	/**
	 * Label displaying current time in format: 2015/05/15 15:35:24
	 */
	private JLabel clock;

	/**
	 * Creates a new status bar.
	 */
	public StatusBar() {
		length = new JLabelValue("length");
		caretLine = new JLabelValue("Ln");
		caretColumn = new JLabelValue("Col");
		selected = new JLabelValue("Sel");

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		add(length);
		addSeparator();

		add(caretLine);
		add(caretColumn);
		add(selected);
		addSeparator();

		clock = new JLabel();
		updateClock();

		add(Box.createHorizontalGlue());
		add(clock);

	}

	/**
	 * Updates caret depending values.
	 * @param caretLine line where caret dot is located
	 * @param caretColumn column where caret dot is located
	 * @param selected length of selected text
	 */
	public void updateCaret(int caretLine, int caretColumn, int selected) {
		this.caretLine.setValue(caretLine);
		this.caretColumn.setValue(caretColumn);
		this.selected.setValue(selected);
	}

	/**
	 * Updates length of active document.
	 * @param length length of active document
	 */
	public void updateLength(int length) {
		this.length.setValue(length);
	}

	/**
	 * JLabelValue is a JLabel which displays a parameter and it's value. E.g. length: 930
	 * 
	 * @author Luka Skugor
	 *
	 */
	private class JLabelValue extends JLabel {

		/**
		 * Parameter name
		 */
		private String name;

		/**
		 * Creates a new JLabelValue with given parameter name
		 * @param name parameter name
		 */
		public JLabelValue(String name) {
			super();
			this.name = name;
		}

		/**
		 * Sets parameter value
		 * @param value parameter value
		 */
		public void setValue(int value) {
			setText(name + ": " + Integer.toString(value));
		}

	}

	/**
	 * Updates clock.
	 */
	public void updateClock() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"YYYY/MM/dd HH:mm:ss");
		clock.setText(timeFormat.format(cal.getTime()));
		clock.repaint();
	}

}
