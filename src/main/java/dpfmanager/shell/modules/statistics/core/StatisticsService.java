/**
 * <h1>ReportService.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.statistics.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.statistics.messages.StatisticsMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Created by Adria Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_STATISTICS)
@Scope("singleton")
public class StatisticsService extends DpfService {

  @PostConstruct
  public void init() {
    // No context yet
  }

  @Override
  protected void handleContext(DpfContext context) {
  }

  public void generateStadistics() {
    StatisticsObject so = new StatisticsObject();
    for (IndividualReport ir : loadIndividualReportsFromDir()) {
      so.parseIndividualReport(ir);
    }
    getContext().send(GuiConfig.COMPONENT_STATISTICS, new StatisticsMessage(StatisticsMessage.Type.RENDER, so));
  }

  private List<GlobalReport> loadGlobalReportsFromDir() {
    List<GlobalReport> grList = new ArrayList<>();
    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list((current, name) -> new File(current, name).isDirectory());
      for (int i = 0; i < directories.length; i++) {
        String reportDay = directories[i];
        File reportDayFile = new File(baseDir + "/" + reportDay);
        String[] directoriesDay = reportDayFile.list((current, name) -> new File(current, name).isDirectory());
        for (int j = 0; j < directoriesDay.length; j++) {
          String reportDir = directoriesDay[j];
          GlobalReport globalReport = (GlobalReport) GlobalReport.read(baseDir + "/" + reportDay + "/" + reportDir + "/summary.ser");
          if (globalReport != null) {
            grList.add(globalReport);
          }
        }
      }
    }
    return grList;
  }

  private List<IndividualReport> loadIndividualReportsFromDir() {
    List<IndividualReport> irList = new ArrayList<>();
    String baseDir = ReportGenerator.getReportsFolder();
    File reportsDir = new File(baseDir);
    if (reportsDir.exists()) {
      String[] directories = reportsDir.list((current, name) -> new File(current, name).isDirectory());
      for (int i = 0; i < directories.length; i++) {
        String reportDay = directories[i];
        File reportDayFile = new File(baseDir + "/" + reportDay);
        String[] directoriesDay = reportDayFile.list((current, name) -> new File(current, name).isDirectory());
        for (int j = 0; j < directoriesDay.length; j++) {
          String reportDir = directoriesDay[j];
          File reportDirFile = new File(baseDir + "/" + reportDay + "/" + reportDir + "/serialized");
          String[] serializedReports = reportDirFile.list((current, name) -> new File(current, name).isFile());
          if (serializedReports == null) continue;
          for (int k = 0; k < serializedReports.length; k++) {
            String reportSer = serializedReports[k];
            IndividualReport ir = (IndividualReport) IndividualReport.read(baseDir + "/" + reportDay + "/" + reportDir + "/serialized/" + reportSer);
            if (ir != null) {
              irList.add(ir);
            }
          }
        }
      }
    }
    return irList;
  }

}