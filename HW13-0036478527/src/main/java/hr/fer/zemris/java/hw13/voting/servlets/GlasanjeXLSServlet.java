package hr.fer.zemris.java.hw13.voting.servlets;

import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingDefinitionEntry;
import hr.fer.zemris.java.hw13.voting.VotingDatabaseUtility.VotingResultEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@WebServlet(name = "GlasanjeXLS", urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String definitionPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-definicija.txt");
		Map<Integer, VotingDefinitionEntry> definition = VotingDatabaseUtility
				.loadDatabaseDefintion(definitionPath);

		String resultsPath = req.getServletContext().getRealPath(
				"/WEB-INF/glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(resultsPath))) {
			VotingDatabaseUtility.createResultsFile(resultsPath,
					definition.keySet());
		}

		Collection<VotingResultEntry> results = VotingDatabaseUtility
				.getResults(resultsPath);
		req.setAttribute("results", results);

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("Band Voting results");
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("Band name");
		head.createCell(1).setCellValue("Votes");

		int rowCounter = 1;
		for (VotingResultEntry resultEntry : results) {
			HSSFRow row = sheet.createRow(rowCounter);
			rowCounter++;

			row.createCell(0).setCellValue(
					definition.get(resultEntry.getID()).getBandName());
			row.createCell(1).setCellValue(resultEntry.getVotes());
		}

		resp.setContentType("application/vnd.ms-excel");
		workbook.write(resp.getOutputStream());

		workbook.close();
	}


}