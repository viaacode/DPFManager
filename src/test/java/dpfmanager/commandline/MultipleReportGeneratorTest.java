package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by Easy on 20/07/2015.
 */
public class MultipleReportGeneratorTest extends CommandLineTest {
  @Test
  public void testReportsXML() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "xml";

    MainConsoleApp.main(args);

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);
  }

  @Test
  public void testReportsKoPdf() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Block/Bad alignment Big E.tif";
    args[1] = "-reportformat";
    args[2] = "pdf";

    MainConsoleApp.main(args);

    String path = getPath();
    File directori = new File(path);
    assertEquals(2, directori.list().length);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(2, l.size());
    doc.close();
  }

  @Test
  public void testReportsPDF() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "pdf";

    MainConsoleApp.main(args);

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(19, l.size());
    doc.close();
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}
