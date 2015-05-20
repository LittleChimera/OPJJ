package hr.fer.zemris.java.hw10.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;

public class JFileEditor extends JTextArea {

	private Path filePath;
	
	public JFileEditor() {
		this(null);
	}
	
	public JFileEditor(Path filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public String getName() {
		return (filePath != null)?filePath.getFileName().toString():"Untitled";
	}
	
	public Path getFilePath() {
		return filePath;
	}
	
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
		
	}
	
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
