package hr.fer.zemris.java.hw10.jnotepadpp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.Timer;

public class StatusBar extends JToolBar {

	private JLabelValue length;
	private JLabelValue caretLine;
	private JLabelValue caretColumn;
	private JLabelValue selected;
	private JLabel clock;
	
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
		
		clock = new JLabel(new Date().toString());
		
		add(Box.createHorizontalGlue());
		add(clock);
		
	}
	
	public void updateCaret(int caretLine, int caretColumn, int selected) {
		this.caretLine.setValue(caretLine);
		this.caretColumn.setValue(caretColumn);
		this.selected.setValue(selected);
	}
	
	public void updateLength(int length) {
		this.length.setValue(length);		
	}
	
	private class JLabelValue extends JLabel {
		
		private String name;

		public JLabelValue(String name) {
			super();
			this.name = name;
		}
		
		public void setValue(int value) {
			setText(name + ": " + Integer.toString(value));
		}
		
	}
	
	public void updateClock(Date time) {
		clock.setText(time.toString());
		clock.repaint();
	}


}
