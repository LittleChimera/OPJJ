package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("image/png");

		int width = 200, height = 200;
		BufferedImage bim = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		final int radiusDecrement = 6;
		for (int i = 0; height > 0 && width > 0; i++) {
			g2d.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
			int iRadiusDecrement = i*radiusDecrement/2;
			width -= radiusDecrement;
			height -= radiusDecrement;
			g2d.fillOval(iRadiusDecrement, iRadiusDecrement, width, height );
		}
		g2d.dispose();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
