package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingResultEntry;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

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

@WebServlet(name = "GlasanjeStatistics", urlPatterns = { "/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String definitionPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		String resultsPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");

		JFreeChart chart = createChart(
				createDataset(VotingDatabaseUtility.getResults(resultsPath),
						VotingDatabaseUtility
								.loadDatabaseDefintion(definitionPath)),
				"Voting results");

		final int width = 600, height = 400;
		BufferedImage image = chart.createBufferedImage(width, height);

		final String imageExtension = "png";
		resp.setContentType("image/" + imageExtension);

		OutputStream os = resp.getOutputStream();
		ImageIO.write(image, imageExtension, os);
		os.flush();
	}

	private PieDataset createDataset(Collection<VotingResultEntry> results,
			Map<Integer, VotingDefinitionEntry> definition) {
		DefaultPieDataset result = new DefaultPieDataset();

		for (VotingResultEntry entry : results) {
			result.setValue(definition.get(entry.getID()).getBandName(),
					entry.getVotes());
		}

		return result;

	}

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