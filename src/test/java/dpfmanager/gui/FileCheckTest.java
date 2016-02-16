package dpfmanager.gui;

import dpfmanager.shell.MainApp;
import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import dpfmanager.shell.interfaces.Gui.ui.report.ReportsModel;
import dpfmanager.shell.reporting.ReportRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Created by Adrià Llorens on 11/01/2016.
 */
public class FileCheckTest extends ApplicationTest {

  private String inputConfigPath = "src/test/resources/ConfigFiles/configAll.dpf";
  private String inputFilePath = "src/test/resources/SmallGui.zip";
//  private String inputFilePath = "src/test/resources/Small/Bilevel.tif";

  Stage stage = null;

  @Override
  public void init() throws Exception {
    stage = launch(MainApp.class, "-gui", "-noDisc");
    scene = stage.getScene();
  }

  @Test
  public void testFileCheck() throws Exception {
    //Wait for async events
    WaitForAsyncUtils.waitForFxEvents();
    System.out.println("Running file check test...");

    //Get the current reports number
    int nReports = getCurrentReports();

    //import config file and check files
    MainModel.setTestParam("import", inputConfigPath);
    clickOnScroll("#importButton");
    clickOnImportedConfig(inputConfigPath);
    writeText("#txtBox1", inputFilePath);
    clickOnAndReload("#checkFilesButton");
    FxAssert.verifyThat("#loadingVbox", NodeMatchers.isVisible()); //Check loading screen
    waitForCheckFiles(60);

    //Check table view
    clickOnAndReload("#butReports");
    TableView<ReportRow> table = (TableView) scene.lookup("#tab_reports");
    ReportRow row = table.getItems().get(0);
    Assert.assertEquals("Reports table rows", Math.min(nReports + 1, ReportsModel.reports_loaded), table.getItems().size());
    Assert.assertEquals("Report row N files", "3", row.getNfiles());
    Assert.assertEquals("Report row N passed", "1 passed", row.getPassed());
    Assert.assertEquals("Report row N errors", "2 errors", row.getErrors());
    Assert.assertEquals("Report row N warnings", "0 warnings", row.getWarnings());

    //Check html && pdf exists
    FxAssert.verifyThat("#tab_reports #buthtml", NodeMatchers.isNotNull());
    clickOnAndReload("#tab_reports #buthtml");
    FxAssert.verifyThat("#webViewReport", NodeMatchers.isNotNull());

    //Check xml
    clickOnAndReload("#butReports");
    clickOnAndReload("#tab_reports #butxml");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    TextArea textArea = (TextArea) scene.lookup("#textArea");
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String initial = textArea.getText().substring(0,expected.length());
    Assert.assertEquals("Report xml", expected, initial);

    //Check json
    clickOnAndReload("#butReports");
    clickOnAndReload("#tab_reports #butjson");
    FxAssert.verifyThat("#textArea", NodeMatchers.isNotNull());
    textArea = (TextArea) scene.lookup("#textArea");
    JsonObject jObj = new JsonParser().parse(textArea.getText()).getAsJsonObject();
    Assert.assertTrue("Report json", (jObj.has("individualreports") && jObj.has("stats")));
  }
}

