package com.axonivy.utils.bpmnstatistic.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.utils.bpmnstatistic.bo.Arrow;
import com.axonivy.utils.bpmnstatistic.internal.ProcessUtils;
import com.axonivy.utils.bpmnstatistic.utils.ProcessesMonitorUtils;

import ch.ivyteam.ivy.process.viewer.api.ProcessViewer;
import ch.ivyteam.ivy.workflow.start.IProcessWebStartable;
import ch.ivyteam.ivy.workflow.start.IWebStartable;

@ManagedBean
@ViewScoped
public class ProcessesMonitorBean {
  private Map<String, List<IProcessWebStartable>> processesMap = new HashMap<>();
  private String selectedProcessName;
  private String selectedModuleName;
  private String selectedProcessDiagramUrl;
  private String selectedPid;
  private List<Arrow> arrows;

  @PostConstruct
  private void init() {
    processesMap = ProcessUtils.getProcessesWithPmv();
  }

  public void onChangeSelectedModule() {
    if (StringUtils.isBlank(selectedModuleName)) {
      selectedModuleName = null;
      selectedProcessName = null;
      arrows = null;
      selectedProcessDiagramUrl = null;
    }
  }

  public void onChangeSelectedProcess() {
    if (StringUtils.isNotBlank(selectedProcessName) && StringUtils.isNotBlank(selectedModuleName)) {
      Optional.ofNullable(getSelectedIProcessWebStartable()).ifPresent(process -> {
        selectedPid = process.pid().getParent().toString();
        selectedProcessDiagramUrl = ProcessViewer.of(process).url().toWebLink().getAbsolute();
        arrows = ProcessesMonitorUtils.getStatisticData(getSelectedIProcessWebStartable());
        for (Arrow arrow : arrows) {
          arrow.setMedianDuration(Math.floor(arrow.getMedianDuration() * 100) / 100);
          arrow.setRatio((float) (Math.floor(arrow.getRatio() * 100) / 100));
        }
      });
    }
  }

  public void showStatisticData() {
    if (StringUtils.isNoneBlank(selectedPid)) {
      ProcessesMonitorUtils.showStatisticData(selectedPid);
      // Mock data for instances count from a time range. Remove it when implement
      // feature of time filter
      ProcessesMonitorUtils.showAdditionalInformation("15", "11.02", "12.08");
    }
  }

  private IProcessWebStartable getSelectedIProcessWebStartable() {
    return processesMap.get(selectedModuleName).stream()
        .filter(process -> process.getDisplayName().equalsIgnoreCase(selectedProcessName)).findAny().orElse(null);
  }

  public List<String> getProcessNames() {
    if (StringUtils.isBlank(selectedModuleName)) {
      return new ArrayList<>();
    }
    return processesMap.get(selectedModuleName).stream().map(IWebStartable::getDisplayName)
        .collect(Collectors.toList());
  }

  public Set<String> getPmvNames() {
    return processesMap.keySet();
  }

  public void setSelectedProcessDiagramUrl(String selectedProcessDiagramUrl) {
    this.selectedProcessDiagramUrl = selectedProcessDiagramUrl;
  }

  public String getSelectedModuleName() {
    return selectedModuleName;
  }

  public void setSelectedModuleName(String selectedModuleName) {
    this.selectedModuleName = selectedModuleName;
  }

  public String getSelectedProcessName() {
    return selectedProcessName;
  }

  public void setSelectedProcessName(String selectedProcessName) {
    this.selectedProcessName = selectedProcessName;
  }

  public String getSelectedProcessDiagramUrl() {
    return selectedProcessDiagramUrl;
  }

  public List<Arrow> getArrows() {
    return arrows;
  }

  public void setArrows(List<Arrow> arrows) {
    this.arrows = arrows;
  }
}
