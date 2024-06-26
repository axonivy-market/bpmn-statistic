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

	@PostConstruct
	private void init() {
		processesMap = ProcessesMonitorUtils.getInstance().getProcessesWithPmv();
	}

	public void onChangeSelectedProcessName() {
		if (StringUtils.isNotBlank(selectedProcessName) && StringUtils.isNotBlank(selectedModuleName)) {
			Optional.ofNullable(getSelectedIProcessWebStartable()).ifPresent(process -> {
				selectedPid = process.pid().getParent().toString();
				selectedProcessDiagramUrl = ProcessViewer.of(process).url().toWebLink().getAbsolute();
			});
		}
	}

	public void showStatisticData() {
		if (StringUtils.isNoneBlank(selectedPid)) {
			ProcessesMonitorUtils.getInstance().showStatisticData(selectedPid);
		}
	}

	private IProcessWebStartable getSelectedIProcessWebStartable() {
		return processesMap.get(selectedModuleName).stream()
				.filter(process -> process.getDisplayName().equalsIgnoreCase(selectedProcessName)).findAny()
				.orElse(null);
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
}
