package dpfmanager.shell.modules.messages;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.util.TextAreaAppender;
import dpfmanager.shell.modules.messages.core.AlertsManager;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_MESSAGE,
    name = BasicConfig.MODULE_MESSAGE,
    active = true)
public class MessagesModule extends DpfModule {

  @Resource
  protected Context context;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(LogMessage.class)){
      tractLogMessage(dpfMessage.getTypedMessage(LogMessage.class));
    } else if (dpfMessage.isTypeOf(AlertMessage.class)){
      tractAlertMessage(dpfMessage.getTypedMessage(AlertMessage.class));
    }
  }

  private void tractLogMessage(LogMessage lm){
    if (lm.hasTextArea() && !TextAreaAppender.hasTextArea()){
      // Init text area handler
      TextAreaAppender.setTextArea(lm.getTextArea());
    }
    else {
      // Log message
      String clazz = lm.getMyClass().toString();
      clazz = clazz.substring(clazz.lastIndexOf(".") + 1, clazz.length());
      if (lm.getLevel().equals(Level.DEBUG)) {
        // use marker for custom pattern
        LogManager.getLogger(clazz).log(lm.getLevel(), MarkerManager.getMarker("PLAIN"), lm.getMessage());
      } else {
        // Default pattern
        LogManager.getLogger(clazz).log(lm.getLevel(), lm.getMessage());
      }
    }
  }

  private void tractAlertMessage(AlertMessage am){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Alert alert;
        // Create alert
        if (am.getType().equals(AlertMessage.Type.EXCEPTION)){
          alert = AlertsManager.createExceptionAlert(am);
        } else if (am.getType().equals(AlertMessage.Type.CONFIRMATION)){
          alert = AlertsManager.createConfirmationAlert(am);
        } else{
          alert = AlertsManager.createSimpleAlert(am);
        }

        // Show alert
        if (!am.getType().equals(AlertMessage.Type.CONFIRMATION)){
          alert.show();
        } else{
          Optional<ButtonType> result = alert.showAndWait();
          am.setResult(result.get().getButtonData().equals(ButtonData.YES));
          context.send(am.getSourceId(), am);
        }
      }
    });
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
  }

  @Override
  public Context getContext(){
    return context;
  }

}