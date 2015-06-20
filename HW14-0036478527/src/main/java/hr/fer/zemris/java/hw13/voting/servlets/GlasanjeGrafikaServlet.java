package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.dao.DAOProvider;
import hr.fer.zemris.java.hw13.model.PollEntry;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * GlasanjeGrafikaServlet generates a pie chart of voting results.
 * 
 * @author Luka Skugor
 *
 */
@WebServlet(name = "GlasanjeStatistics", urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Width of the pie chart.
	 */
	private final int WIDTH = 600;
	/**
	 * Height of the pie chart.
	 */
	private final int HEIGHT = 400;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		JFreeChart chart = createChart(createDataset(DAOProvider.getDao()
				.getPollEntries(Long.parseLong(req.getParameter("pollId")))),
				"Voting results");

		BufferedImage image = chart.createBufferedImage(WIDTH, HEIGHT);

		final String imageExtension = "png";
		resp.setContentType("image/" + imageExtension);

		OutputStream os = resp.getOutputStream();
		ImageIO.write(image, imageExtension, os);
		os.flush();
	}

	/**
	 * Creates a data set of voting results.
	 * 
	 * @param entries
	 *            voting results
	 * @param definition
	 *            voting database definition
	 * @return creates data set
	 */
	private PieDataset createDataset(Collection<PollEntry> entries) {
		DefaultPieDataset result = new DefaultPieDataset();

		for (PollEntry entry : entries) {
			result.setValue(entry.getOptionTitle(), entry.getVotesCount());
		}

		return result;

	}

	/**
	 * Creates a pie chart for given data set and chart title.
	 * 
	 * @param dataset
	 *            data of the pie chart
	 * @param title
	 *            title of the chart
	 * @return created pie chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true,
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}

}