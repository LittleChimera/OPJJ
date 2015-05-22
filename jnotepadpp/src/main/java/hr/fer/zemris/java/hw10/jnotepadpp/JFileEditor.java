package hr.fer.zemris.java.hw10.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;

/**
 * JFileEditor is JTextArea which has a path to the file where contents of
 * JTextArea should be stored.
 * 
 * @author Luka Skugor
 *
 */
public class JFileEditor extends JTextArea {

	/**
	 * Path of the source or destination file.
	 */
	private Path filePath;

	/**
	 * Creates a new JFileEditor with no set path.
	 */
	public JFileEditor() {
		this(null);
	}

	/**
	 * Creates a new JFileEditor with given path.
	 * @param filePath source or destination path
	 */
	public JFileEditor(Path filePath) {
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#getName()
	 */
	@Override
	public String getName() {
		return (filePath != null) ? filePath.getFileName().toString()
				: "Untitled";
	}

	/**
	 * Gets file path.
	 * @return file path
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets file path
	 * @param filePath file path
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;

	}

	/**
	 * Forces notify on editor's {@link CaretListener}s.
	 */
	public void fireEditorUpdate() {
		fireCaretUpdate(new CaretEvent(this) {

			@Override
			public int getMark() {
				return JFileEditor.this.getCaret().getMark();
			}

			@Override
			public int getDot() {
				return JFileEditor.this.getCaret().getDot();
			}
		});
	}
}
