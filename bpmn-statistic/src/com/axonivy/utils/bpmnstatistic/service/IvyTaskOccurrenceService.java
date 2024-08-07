package com.axonivy.utils.bpmnstatistic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.utils.bpmnstatistic.bo.TaskOccurrence;
import com.axonivy.utils.bpmnstatistic.enums.IvyVariable;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.query.TaskQuery;

public class IvyTaskOccurrenceService {
  private static final String LIKE_TEXT_SEARCH = "%%%s%%";
  private static final String SLASH = "/";

  private IvyTaskOccurrenceService() {
  }

  public static HashMap<String, Integer> countTaskOccurrencesByProcessId(String processId) {
    HashMap<String, TaskOccurrence> taskOccurrenceMap = getHashMapTaskOccurrencesByProcessId(processId);
    return correctTaskOccurrences(taskOccurrenceMap);
  }

  private static HashMap<String, TaskOccurrence> getHashMapTaskOccurrencesByProcessId(String processId) {
    return Sudo.get(() -> {
      TaskQuery taskQuery = TaskQuery.create().where().requestPath().isLike(String.format(LIKE_TEXT_SEARCH, processId))
          .orderBy().startTaskSwitchEventId();
      HashMap<String, TaskOccurrence> map = new HashMap<>();
      countTaskOccurrencesByTaskQuery(map, taskQuery);
      return map;
    });
  }

  private static void countTaskOccurrencesByTaskQuery(HashMap<String, TaskOccurrence> map, TaskQuery taskQuery) {
    List<ITask> tasks = new ArrayList<>();
    int maxQueryResults = Integer.valueOf(Ivy.var().get(IvyVariable.MAX_QUERY_RESULTS.getVariableName()));
    Integer startIndex = 0;
    do {
      tasks = Ivy.wf().getTaskQueryExecutor().getResults(taskQuery, startIndex, maxQueryResults);
      countTaskOccurrences(map, tasks);
      startIndex += maxQueryResults;
    } while (maxQueryResults == tasks.size());
  }

  private static void countTaskOccurrences(HashMap<String, TaskOccurrence> taskOccurrenceMap, List<ITask> tasks) {
    for (ITask iTask : tasks) {
      String taskElementId = getTaskElementIdFromRequestPath(iTask.getRequestPath());
      updateTaskOccurrencesMap(taskOccurrenceMap, taskElementId, iTask.getStartSwitchEvent().getId());
    }
  }

  private static String getTaskElementIdFromRequestPath(String requestPath) {
    String[] arr = requestPath.split(SLASH);
    // Request Path contains: {PROCESS ID}/.../{NAME OF TASK}
    // So we have get the node before /{NAME OF TASK}
    // Ignore case {PROCESS ID}/{NAME OF TASK}
    return arr.length > 2 ? arr[arr.length - 2] : StringUtils.EMPTY;
  }

  private static void updateTaskOccurrencesMap(HashMap<String, TaskOccurrence> taskOccurrenceMap, String taskElementId,
      Long startTaskSwitchEventId) {
    if (StringUtils.isNotBlank(taskElementId)) {
      TaskOccurrence taskOccurrence = getCountedTaskOccurrence(taskOccurrenceMap, taskElementId,
          startTaskSwitchEventId);
      taskOccurrenceMap.put(taskElementId, taskOccurrence);
    }
  }

  private static TaskOccurrence getCountedTaskOccurrence(HashMap<String, TaskOccurrence> taskOccurrenceMap,
      String taskElementId, Long startTaskSwitchEventId) {
    TaskOccurrence taskOccurrence = taskOccurrenceMap.get(taskElementId);
    if (taskOccurrence != null) {
      if (startTaskSwitchEventId != null && !taskOccurrence.getStartSwitchEventId().equals(startTaskSwitchEventId)) {
        taskOccurrence.setOccurrence(taskOccurrence.getOccurrence() + 1);
        taskOccurrence.setStartSwitchEventId(startTaskSwitchEventId);
      }
    } else {
      taskOccurrence = new TaskOccurrence(startTaskSwitchEventId, 1);
    }

    return taskOccurrence;
  }

  private static HashMap<String, Integer> correctTaskOccurrences(HashMap<String, TaskOccurrence> taskOccurrenceMap) {
    HashMap<String, Integer> result = new HashMap<>();
    for (Map.Entry<String, TaskOccurrence> entry : taskOccurrenceMap.entrySet()) {
      result.put(entry.getKey(), entry.getValue().getOccurrence());
    }

    return result;
  }
}
