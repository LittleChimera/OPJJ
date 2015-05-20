package hr.fer.zemris.java.hw10.jnotepadpp;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JTabbedEditor extends JTabbedPane {	
	
	private Action tabCloseAction;
	
	public JTabbedEditor(Action tabCloseAction) {
		this.tabCloseAction = tabCloseAction;
	}
	
	public Component addTab(JFileEditor editor) {
		JScrollPane scrollableEditor = new JScrollPane(editor);
		Component tab = add(scrollableEditor);
		setSelectedComponent(tab);
				
		JTabComponent tabComponent = new JTabComponent(editor);
		setTabComponentAt(getSelectedIndex(), tabComponent);
		//saveActiveTabContents();
		
		return tab;
	}

	
	private class JTabComponent extends JPanel {
		
		private JFileEditor editor;
		private JLabel fileName;
		private boolean modified;

		public JTabComponent(JFileEditor editor) {
			this.editor = editor;
			this.modified = false;
			editor.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					docChanged();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					docChanged();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					docChanged();
				}
				
				private void docChanged() {
					setIcon(notSaved);
					modified = true;
				}
			});
			setLayout(new FlowLayout());
			initGUI();
		}
		
		private void initGUI() {
			setOpaque(false);
			
			JButton closeButton = new JButton("x");
			closeButton.addActionListener(e -> {
				if (modified) {
					setSelectedIndex(indexOfTabComponent(this));
					tabCloseAction.actionPerformed(null);
				} else {
					JTabbedEditor.this.remove(indexOfTabComponent(this));					
				}
			});
			final int buttonBorder = 3;
			closeButton.setBorder(
					new EmptyBorder(buttonBorder, buttonBorder, buttonBorder, buttonBorder)
			);
			add(closeButton);
			
			fileName = new JLabel(editor.getName());
			// sets icon to right
			fileName.setHorizontalTextPosition(JLabel.LEFT);
			add(fileName);
		}
		
		public void updateDisplayName() {
			fileName.setText(editor.getName());
		}
		
		private void setIcon(Icon icon) {
			fileName.setIcon(icon);
		}
		
	}
	
	public void saveActiveTabContents() {
		JTabComponent selectedTabComponent = (JTabComponent)getTabComponentAt(getSelectedIndex());
		
		Path tooltip = selectedTabComponent.editor.getFilePath();
		if (tooltip != null) {
			setToolTipTextAt(getSelectedIndex(), tooltip.toString());			
		}
		
		selectedTabComponent.updateDisplayName();
		selectedTabComponent.setIcon(saved);
		selectedTabComponent.modified = false;
	}
	
	public boolean isSelectedSaved() {
		return !((JTabComponent) getTabComponentAt(getSelectedIndex())).modified;
	}
	
	private static ImageIcon saved = new ImageIcon("icons/saved.png", "File saved");
	private static ImageIcon notSaved = new ImageIcon("icons/not_saved.png", "File not saved");
	
	
}
