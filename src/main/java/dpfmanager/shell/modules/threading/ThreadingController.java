package dpfmanager.shell.modules.threading;

import dpfmanager.shell.application.launcher.noui.ApplicationParameters;
import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.modules.threading.core.ThreadingService;
import dpfmanager.shell.modules.threading.messages.GlobalStatusMessage;
import dpfmanager.shell.modules.threading.messages.IndividualStatusMessage;
import dpfmanager.shell.modules.threading.messages.RunnableMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_THREADING)
public class ThreadingController extends DpfSpringController {

  @Autowired
  private ThreadingService service;

  @Autowired
  private ApplicationContext appContext;

  @Value("#{springProperties['mode']}")
  private String mode;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualStatusMessage.class)) {
      IndividualStatusMessage sm = dpfMessage.getTypedMessage(IndividualStatusMessage.class);
      service.finishIndividual(sm.getIndividual(), sm.getUuid());
    } else if (dpfMessage.isTypeOf(GlobalStatusMessage.class)) {
      GlobalStatusMessage gm = dpfMessage.getTypedMessage(GlobalStatusMessage.class);
      boolean silence = true;
      if (params != null){
        silence = params.isSilence();
      }
      service.handleGlobalStatus(gm, silence);
      // Now close application only if it is not server mode
      if (gm.isFinish() && mode.equals("CMD")) {
        AppContext.close();
        ConsoleLauncher.setFinished(true);
      }
    } else if (dpfMessage.isTypeOf(RunnableMessage.class)) {
      RunnableMessage rm = dpfMessage.getTypedMessage(RunnableMessage.class);
      service.run(rm.getRunnable(), rm.getUuid());
    } else if (dpfMessage.isTypeOf(ThreadsMessage.class)){
      ThreadsMessage tm = dpfMessage.getTypedMessage(ThreadsMessage.class);
      service.processThreadMessage(tm);
    }
  }

  @PostConstruct
  public void init() {
    service.setContext(new ConsoleContext(appContext));
  }

}
