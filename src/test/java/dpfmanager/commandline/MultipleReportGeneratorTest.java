package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by Easy on 20/07/2015.
 */
public class MultipleReportGeneratorTest extends CommandLineTest {
  @Test
  public void testReportsXML() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-f";
    args[2] = "xml";
    args[3] = "-s";
    args[4] = "src/test/resources/Small/";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains(".mets") && !file.contains("serialized") && !file.contains(".ser")) n++;
    }

    assertEquals(7, n);
  }

  @Test
  public void testReportsKoPdf() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-f";
    args[2] = "pdf";
    args[3] = "-s";
    args[4] = "src/test/resources/Block/Bad alignment Big E.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains("Mets") && !file.contains(".ser") && !file.equals("serialized") && !file.equals("html")) {
        n++;
      }
    }

    assertEquals(2, n);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(1, l.size());
    doc.close();
  }

  @Test
  public void testReportsZKoPdf() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "--format";
    args[2] = "pdf";
    args[3] = "-s";
    args[4] = "src/test/resources/Block/Bad alignment Big E.tif";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (!file.contains("Mets") && !file.contains(".ser") && !file.equals("serialized") && !file.equals("html")) n++;
    }

    assertEquals(2, n);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(1, l.size());
    doc.close();
  }

  @Test
  public void testReportsPDF() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[5];
    args[0] = "check";
    args[1] = "-f";
    args[2] = "pdf";
    args[3] = "-s";
    args[4] = "src/test/resources/Small/";

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    String path = getPath();
    File directori = new File(path);
    int n=0;
    for (String file : directori.list()) {
      if (file.endsWith(".pdf")) n++;
    }

    assertEquals(7, n);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(2, l.size());
    doc.close();
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}
