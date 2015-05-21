package hr.fer.zemris.java.hw10.jnotepadpp.localization;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class FormLocalizationProvider extends LocalizationProviderBridge {

	public FormLocalizationProvider(ILocalizationProvider localizationProvider,
			JFrame frame) {
		super(localizationProvider);
		
		connect();
		
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}

		});
	}

}
