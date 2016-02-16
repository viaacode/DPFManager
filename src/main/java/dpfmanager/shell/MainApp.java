package dpfmanager.shell;

import dpfmanager.shell.interfaces.Gui.resources.DpfStyles;
import dpfmanager.shell.interfaces.Gui.ui.main.MainModel;
import dpfmanager.shell.interfaces.Cli.CommandLine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.sun.javafx.application.ParametersImpl;

import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.core.application.DefaultApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class MainApp extends DefaultApplication<StackPane> {

  private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
  private static Stage thestage;

  /**
   * Application launcher.
   *
   * @param args the command line arguments
   */
  public static void main(final String... args) {
    Parameters params = new ParametersImpl(args);
    if (params == null || params.getRaw().size() == 0 || (params.getRaw().size() > 0 && params.getRaw().contains("-gui"))) {
      // GUI
      LOG.info("Starting JavaFX application");
      boolean noDisc = params.getRaw().contains("-noDisc");
      MainModel.setNoDisc(noDisc);
      Application.launch(args);
    } else {
      // Command Line
      LOG.info("Starting command line application");
      CommandLine cl = new CommandLine(params);
      cl.launch();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Model> getFirstModelClass() {
    return MainModel.class;
  }

  @Override
  protected void customizeScene(final Scene scene) {
    addCSS(scene, DpfStyles.MAIN);
  }

  @Override
  protected void customizeStage(final Stage stage){
    thestage = stage;
    thestage.setMinWidth(400);
  }

  public static Stage getMyStage() {
    return thestage;
  }

}
