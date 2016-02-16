package dpfmanager.gui;

import dpfmanager.shell.MainApp;
import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import dpfmanager.shell.interfaces.Gui.ui.report.ReportsModel;
import dpfmanager.shell.reporting.ReportRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 12/01/2016.
 */
public class ReportParserTest extends ApplicationTest {

  private String configHtml = "src/test/resources/ConfigFiles/SimpleHtml.dpf";
  private String configXml = "src/test/resources/ConfigFiles/SimpleXml.dpf";
  private String configJson = "src/test/resources/ConfigFiles/SimpleJson.dpf";
  private String inputFiles = "src/test/resources/OneOffEach.zip";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testReportParser() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running report parser test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    // --
    // First Run the 3 check
    // --
    List<String> list = Arrays.asList(configHtml, configJson, configXml);
    for (String configFile : list) {
      MainModel.setTestParam("import", configFile);
      clickOnScroll("#importButton");
      clickOnImportedConfig(configFile);
      writeText("#txtBox1", inputFiles);
      clickOnAndReload("#checkFilesButton");
      waitForCheckFiles(60);
      System.out.println("Current config: "+configFile);
      clickOnAndReload("#butDessign");
    }

    // Go to reports and check them
    clickOnAndReload("#butReports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tab_reports");
    Assert.assertEquals("Reports table rows", Math.min(nReports + 3, ReportsModel.reports_loaded), table.getItems().size());
    checkValidRow(table.getItems().get(0), "XML"); //Xml
    checkValidRow(table.getItems().get(1), "JSON"); //Json
    checkValidRow(table.getItems().get(2), "HTML"); //Html
  }

  private void checkValidRow(ReportRow row, String type){
    Assert.assertEquals("Report row N files ("+type+")", "2", row.getNfiles());
    Assert.assertEquals("Report row N passed ("+type+")", "1 passed", row.getPassed());
    Assert.assertEquals("Report row N errors ("+type+")", "1 errors", row.getErrors());
    Assert.assertEquals("Report row N warnings ("+type+")", "1 warnings", row.getWarnings());
    Assert.assertEquals("Report row score (" + type + ")", "50%", row.getScore());
  }
}

